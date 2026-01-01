from __future__ import annotations

import math
import matplotlib.pyplot as plt
from matplotlib.patches import Polygon
from networkx import edges

from coherent_space_py.geometry import get_positions, polygon_points_in_angle_order
from coherent_space_py.model.enums import InformationalStreamVectorDirection as VD
from coherent_space_py.model.enums import StreamProcessType as SPT
from coherent_space_py.graph import build_star_overlay, draw_star_overlay
from coherent_space_py.model.topology_rules import get_upstream_connections, get_downstream_connections
from dataclasses import dataclass

DEFAULT_DOWNSTREAM_SCALE = 1.0 / math.sqrt(3)

# -----------------------------
# Coordinate domain (closed-form, no BFS)
# -----------------------------

def axial_hex_radius(radius: int):
    """Return axial coords (q, r) within hex radius (closed-form enumeration)."""
    for q in range(-radius, radius + 1):
        r1 = max(-radius, -q - radius)
        r2 = min(radius, -q + radius)
        for r in range(r1, r2 + 1):
            yield (q, r)


def uv_center_xy_vertex_sharing(q: int, r: int, cell_R: float) -> tuple[float, float]:
    """
    UpstreamVertex placement for vertex-sharing hexagons.

    cell_R is the upstream circumradius (center -> vertex) used consistently for:
      - lattice spacing (neighbor center distance = 2*cell_R)
      - subgraph size (get_positions upstream_radius = cell_R)
    """
    x = math.sqrt(3) * cell_R * q
    y = cell_R * q + 2.0 * cell_R * r
    return x, y

def ue_center_xy_edge_sharing(q: int, r: int, cell_R: float) -> tuple[float, float]:
    """
    UpstreamEdge placement for EDGE-SHARING pointy-top hexagons.

    For pointy-top hexes:
      - horizontal spacing = sqrt(3) * R
      - vertical spacing   = 3/2 * R
      - axial to pixel:
          x = sqrt(3) * R * (q + r/2)
          y = 1.5 * R * r
    """
    x = math.sqrt(3) * cell_R * (q + r / 2.0)
    y = 1.5 * cell_R * r
    return x, y

# -----------------------------
# Parity rule (per-cell)
# -----------------------------

def parity_for_cell(origin_vd: VD, q: int, r: int) -> VD:
    """
    Return CornerParity or SideParity for the cell at axial (q,r).

    Rule:
      - origin parity is given (Corner or Side)
      - each step flips parity, so parity is determined by (q + r) mod 2
      - you can change (q + r) to another invariant if your Java does it differently,
        but this is the standard "checkerboard" parity on axial coordinates.
    """
    origin_bit = 0 if origin_vd == VD.CornerParity else 1
    cell_bit = (q + r + origin_bit) & 1
    return VD.CornerParity if cell_bit == 0 else VD.SideParity


# -----------------------------
# Gaussian ID (stable debug identifier)
# -----------------------------

def gauss_sum6(k: int) -> int:
    # Java InformationalStreamUtils.getNGaussSum(6,k) = 3*k*(k+1)
    return 3 * k * (k + 1)

def cube_from_axial(q: int, r: int) -> tuple[int, int, int]:
    x = q
    z = r
    y = -x - z
    return x, y, z

def hex_ring(q: int, r: int) -> int:
    x, y, z = cube_from_axial(q, r)
    return max(abs(x), abs(y), abs(z))

# 6 cube directions (pointy-top axial). You can rotate these if the numbering order differs vs Java.
CUBE_DIRS = [
    (1, -1, 0),   # dir0
    (1, 0, -1),   # dir1
    (0, 1, -1),   # dir2
    (-1, 1, 0),   # dir3
    (-1, 0, 1),   # dir4
    (0, -1, 1),   # dir5
]

def add_cube(a, b):
    return (a[0]+b[0], a[1]+b[1], a[2]+b[2])

def mul_cube(d, k: int):
    return (d[0]*k, d[1]*k, d[2]*k)

def axial_from_cube(x: int, y: int, z: int) -> tuple[int, int]:
    return (x, z)

def rotate_dirs(shift: int):
    s = shift % 6
    return CUBE_DIRS[s:] + CUBE_DIRS[:s]

def uv_ring_spiral_id(q: int, r: int, *, start_dir_shift: int = 0) -> int:
    """
    Return discontinuous Java-like ID based purely on ring spiral indexing.
    - 0 at center
    - ring n has ids (gauss_sum6(n-1)+1) .. gauss_sum6(n)
    The only degree of freedom is orientation: start_dir_shift rotates numbering around the ring.
    """
    n = hex_ring(q, r)
    if n == 0:
        return 0

    dirs = rotate_dirs(start_dir_shift)

    # Choose a start corner for ring n.
    # Standard spiral starts at cube = dir4 * n (one corner), then walks 6 sides.
    start_corner = mul_cube(dirs[4], n)

    # Walk around the ring and find the coordinate index.
    idx = 0
    cur = start_corner
    for side in range(6):
        step_dir = dirs[side]
        for _ in range(n):
            aq, ar = axial_from_cube(*cur)
            if aq == q and ar == r:
                return gauss_sum6(n - 1) + idx + 1
            cur = add_cube(cur, step_dir)
            idx += 1

    # Should never happen for valid ring coords
    raise ValueError("Coordinate not found on computed ring; check axial/cube mapping.")

# -----------------------------
# Labels / drawing helpers
# -----------------------------

def abbrev(t: SPT) -> str:
    """Short label: e.g. UpstreamSelectorFunction -> SF."""
    name = t.name
    caps = "".join([c for c in name if c.isupper()])
    caps = caps.replace("U", "").replace("D", "")
    return caps


def translate_positions(pos_map, dx: float, dy: float):
    """Translate all (x,y) by (dx,dy)."""
    return {t: (p[0] + dx, p[1] + dy) for t, p in pos_map.items()}


# -----------------------------
# Downstream arrow exclusion (simple pair list)
# -----------------------------

# Exclude unwanted downstream arrows (direction-agnostic pairs).
DOWN_ARROW_EXCLUSION = [
    frozenset({SPT.DownstreamSelectorFunction, SPT.DownstreamDetectorFunction}),
    frozenset({SPT.DownstreamSelectorFunction, SPT.DownstreamConsumerFunction}),
    frozenset({SPT.DownstreamDetectorFunction, SPT.DownstreamConsumerFunction}),
    frozenset({SPT.DownstreamSelectorSystem, SPT.DownstreamDetectorSystem}),
    frozenset({SPT.DownstreamSelectorSystem, SPT.DownstreamConsumerSystem}),
    frozenset({SPT.DownstreamDetectorSystem, SPT.DownstreamConsumerSystem}),
]
DOWN_ARROW_EXCLUSION_SET = set(DOWN_ARROW_EXCLUSION)


def downstream_filter_targets(source_type: SPT, targets: list[SPT]) -> list[SPT]:
    """Remove downstream edges that match an excluded unordered pair."""
    out: list[SPT] = []
    for t in targets:
        if frozenset({source_type, t}) in DOWN_ARROW_EXCLUSION_SET:
            continue
        out.append(t)
    return out

# Exclude unwanted upstream arrows (direction-agnostic pairs).
UP_ARROW_EXCLUSION = [
    frozenset({SPT.UpstreamSelectorFunction, SPT.UpstreamDetectorFunction}),
    frozenset({SPT.UpstreamSelectorFunction, SPT.UpstreamConsumerFunction}),
    frozenset({SPT.UpstreamDetectorFunction, SPT.UpstreamConsumerFunction}),
    frozenset({SPT.UpstreamSelectorSystem, SPT.UpstreamDetectorSystem}),
    frozenset({SPT.UpstreamSelectorSystem, SPT.UpstreamConsumerSystem}),
    frozenset({SPT.UpstreamDetectorSystem, SPT.UpstreamConsumerSystem}),
]
UP_ARROW_EXCLUSION_SET = set(UP_ARROW_EXCLUSION)


def upstream_filter_targets(source_type: SPT, targets: list[SPT]) -> list[SPT]:
    """Remove upstream edges that match an excluded unordered pair."""
    out: list[SPT] = []
    for t in targets:
        if frozenset({source_type, t}) in UP_ARROW_EXCLUSION_SET:
            continue
        out.append(t)
    return out


# -----------------------------
# Cell rendering
# -----------------------------

def draw_cell(
    ax,
    center_xy: tuple[float, float],
    origin_vd: VD, 
    vd_cell: VD,
    cell_R: float,
    mode: str = "BOTH",
    draw_labels: bool = False,
):
    """
    Draw one cell at center_xy using vd_cell (per-cell parity).
    - Geometry is scaled by cell_R (upstream circumradius).
    - Parity affects arrows via topology rules.
    - No inter-cell links yet.
    """
    cx, cy = center_xy

    # Scale upstream geometry by cell_R so it matches the lattice.
    local = get_positions(
        mode=mode,
        upstream_radius=cell_R,
        downstream_radius=cell_R * DEFAULT_DOWNSTREAM_SCALE,
    )
    local = translate_positions(local, cx, cy)

    up_nodes = {t: p for t, p in local.items() if "Upstream" in t.name}
    down_nodes = {t: p for t, p in local.items() if "Downstream" in t.name}

    # Draw only rings (no diagonals / no triangles)
    if mode in ("UP", "BOTH") and up_nodes:
        up_ring = [
            SPT.UpstreamSelectorFunction,
            SPT.UpstreamConsumerSystem,
            SPT.UpstreamDetectorFunction,
            SPT.UpstreamSelectorSystem,
            SPT.UpstreamConsumerFunction,
            SPT.UpstreamDetectorSystem,
        ]
        pts = [up_nodes[t] for t in up_ring if t in up_nodes]
        # if len(pts) >= 3:
        #     xs = [p[0] for p in pts] + [pts[0][0]]
        #     ys = [p[1] for p in pts] + [pts[0][1]]
        #     ax.plot(xs, ys, linewidth=1.2, linestyle="-", color="black", alpha=0.9, zorder=2)

    if mode in ("DOWN", "BOTH") and down_nodes:
        down_ring = [
            SPT.DownstreamSelectorSystem,
            SPT.DownstreamConsumerFunction,
            SPT.DownstreamDetectorSystem,
            SPT.DownstreamSelectorFunction,
            SPT.DownstreamConsumerSystem,
            SPT.DownstreamDetectorFunction,
        ]
        pts = [down_nodes[t] for t in down_ring if t in down_nodes]
        # if len(pts) >= 3:
        #     xs = [p[0] for p in pts] + [pts[0][0]]
        #     ys = [p[1] for p in pts] + [pts[0][1]]
        #     ax.plot(xs, ys, linewidth=1.1, linestyle="--", color="black", alpha=0.9, zorder=2)

    # Nodes
    for t, (x, y) in up_nodes.items():
        color = "green" if "System" in t.name else "blue"
        ax.add_patch(plt.Circle((x, y), 0.06 * cell_R, color=color, zorder=3))
        ax.text(x, y, abbrev(t), ha="center", va="center", color="white", fontsize=7, zorder=4)
        if draw_labels:
            ax.text(x, y + 0.10 * cell_R, t.name.split("stream")[1], ha="center", fontsize=6)

    for t, (x, y) in down_nodes.items():
        color = "brown" if "System" in t.name else "orange"
        ax.add_patch(plt.Circle((x, y), 0.05 * cell_R, color=color, zorder=3))
        ax.text(x, y, abbrev(t), ha="center", va="center", color="white", fontsize=7, zorder=4)
        if draw_labels:
            ax.text(x, y + 0.09 * cell_R, t.name.split("stream")[1], ha="center", fontsize=6)

    # Generic arrow renderer with optional filtering
    def draw_arrows(node_map, get_targets_fn, *, origin_vd, target_filter=None):
        for s_type, s_pos in node_map.items():
            targets = list(get_targets_fn(s_type, vd_cell, origin_vd))
            if target_filter is not None:
                targets = target_filter(s_type, targets)
            for t_type in targets:
                if t_type in node_map:
                    ax.annotate(
                        "",
                        xy=node_map[t_type], xycoords="data",
                        xytext=s_pos, textcoords="data",
                        arrowprops=dict(arrowstyle="-|>", color="black", lw=1.8, mutation_scale=12),
                        zorder=5,
                    )

    # Upstream arrows: keep all (full rules), parity varies per cell via vd_cell
    if up_nodes:
        draw_arrows(up_nodes, get_upstream_connections, origin_vd=origin_vd) #, target_filter=upstream_filter_targets)

    # Build SPT -> (x,y) positions for this cell (needed by star overlay)
    spt_positions = {}
    spt_positions.update(up_nodes)
    spt_positions.update(down_nodes)

    inter_pos, star_edges = build_star_overlay(
        spt_positions,               # dict[SPT,(x,y)]
        origin_vd=origin_vd,
        cell_vd=vd_cell,
    )

    # draw_star_overlay(
    #     ax,
    #     sat_positions=spt_positions,
    #     intersection_positions=inter_pos,
    #     edges=star_edges,
    #     origin_vd=origin_vd,
    #     cell_vd=vd_cell,
    #     draw_intersection_nodes=True,
    #     draw_seg_edges=True,
    #     draw_skip_edges=True,
    #     draw_full_edges=True,
    # )


# -----------------------------
# UV visualization
# -----------------------------

def visualize_uv_radius1(
    origin_vd: VD = VD.CornerParity,
    radius: int = 1,
    cell_R: float = 1.0,
    mode: str = "BOTH",
    out_path: str = "uv_radius1.png",
):
    """
    UV netting radius=radius with vertex-sharing placement.
    Parity is computed per cell: vd_cell = parity_for_cell(origin_vd, q, r).
    """
    coords = list(axial_hex_radius(radius))

    fig, ax = plt.subplots(1, 1, figsize=(10, 10))
    ax.set_aspect("equal")
    ax.axis("off")

    for (q, r) in coords:
        cx, cy = uv_center_xy_vertex_sharing(q, r, cell_R)

        vd_cell = parity_for_cell(origin_vd, q, r)

        draw_cell(
            ax,
            (cx, cy),
            origin_vd=origin_vd,
            vd_cell=vd_cell,
            cell_R=cell_R,
            mode=mode,
            draw_labels=False,
        )

        # Debug label: gaussian id + parity marker
        _id = uv_ring_spiral_id(q, r, start_dir_shift=0)
        vd_cell = parity_for_cell(origin_vd, q, r)   # asta e paritatea ta, depinde de origine
        mark = "C" if vd_cell == VD.CornerParity else "S"


        # If you still want to show gaussian a+bi too, show both:
        ax.text(cx, cy, f"{_id}\n{mark}", ha="center", va="center", fontsize=8, color="black", zorder=10)

        # Optional: show your a+bi under it
        # ax.text(cx, cy - 0.35 * cell_R, gaussian_label(q, r), ha="center", va="center", fontsize=7, color="black", zorder=10)

    xs, ys = [], []
    for (q, r) in coords:
        cx, cy = uv_center_xy_vertex_sharing(q, r, cell_R)
        xs.append(cx)
        ys.append(cy)

    pad = 2.4 * cell_R
    ax.set_xlim(min(xs) - pad, max(xs) + pad)
    ax.set_ylim(min(ys) - pad, max(ys) + pad)

    ax.set_title(f"UV vertex-sharing | radius={radius} | mode={mode} | origin_vd={origin_vd.name} | cell_R={cell_R}")
    plt.tight_layout()
    plt.savefig(out_path, dpi=250)
    print(f"Saved: {out_path}")

def visualize_uv_level(
    origin_vd: VD,
    base_radius: int,
    stream_distance: float,
    step: int,
    mode: str,
    out_path: str,
    start_dir_shift: int = 0,
):
    # Java-like: step shrinks cell size by 3^step
    cell_R = stream_distance / (3 ** step)

    # Keep the same physical coverage by scaling radius
    radius = base_radius * (3 ** step)

    visualize_uv_radius1(
        origin_vd=origin_vd,
        radius=radius,
        cell_R=cell_R,
        mode=mode,
        out_path=out_path,
    )

def visualize_ue_radius1(
    origin_vd: VD = VD.CornerParity,
    radius: int = 1,
    cell_R: float = 1.0,
    mode: str = "BOTH",
    out_path: str = "ue_radius1.png",
    start_dir_shift: int = 0,
):
    """
    UE netting radius=radius with edge-sharing placement.
    Parity is computed per cell: vd_cell = parity_for_cell(origin_vd, q, r).
    """
    coords = list(axial_hex_radius(radius))

    fig, ax = plt.subplots(1, 1, figsize=(10, 10))
    ax.set_aspect("equal")
    ax.axis("off")

    for (q, r) in coords:
        cx, cy = ue_center_xy_edge_sharing(q, r, cell_R)
        vd_cell = parity_for_cell(origin_vd, q, r)

        draw_cell(
            ax,
            (cx, cy),
            origin_vd=origin_vd,
            vd_cell=vd_cell,
            cell_R=cell_R,
            mode=mode,
            draw_labels=False,
        )

        # Same unique ring-spiral id (orientation adjustable)
        _id = uv_ring_spiral_id(q, r, start_dir_shift=start_dir_shift)
        mark = "C" if vd_cell == VD.CornerParity else "S"
        ax.text(cx, cy, f"{_id}\n{mark}", ha="center", va="center", fontsize=8, color="black", zorder=10)

    xs, ys = [], []
    for (q, r) in coords:
        cx, cy = ue_center_xy_edge_sharing(q, r, cell_R)
        xs.append(cx)
        ys.append(cy)

    pad = 2.4 * cell_R
    ax.set_xlim(min(xs) - pad, max(xs) + pad)
    ax.set_ylim(min(ys) - pad, max(ys) + pad)

    ax.set_title(f"UE edge-sharing | radius={radius} | mode={mode} | origin_vd={origin_vd.name} | cell_R={cell_R}")
    plt.tight_layout()
    plt.savefig(out_path, dpi=250)
    print(f"Saved: {out_path}")

def visualize_ue_level(
    origin_vd: VD,
    base_radius: int,
    stream_distance: float,
    step: int,
    mode: str,
    out_path: str,
    start_dir_shift: int = 0,
):
    # Java-like: shrink cell size by 3^step
    cell_R = stream_distance / (3 ** step)

    # Keep same physical coverage by scaling radius
    radius = base_radius * (3 ** step)

    visualize_ue_radius1(
        origin_vd=origin_vd,
        radius=radius,
        cell_R=cell_R,
        mode=mode,
        out_path=out_path,
        start_dir_shift=start_dir_shift,
    )

if __name__ == "__main__":
    # Level 0 (streamDistance=3)
    visualize_uv_level(
        origin_vd=VD.CornerParity,
        base_radius=1,
        stream_distance=3.0,
        step=0,
        mode="BOTH",
        out_path="uv_level0.png",
    )

    # Level 1 (3x denser)
    visualize_uv_level(
        origin_vd=VD.CornerParity,
        base_radius=1,
        stream_distance=3.0,
        step=1,
        mode="BOTH",
        out_path="uv_level1.png",
    )
    # UE Level 0 (big cells)
    visualize_ue_level(
        origin_vd=VD.CornerParity,
        base_radius=1,
        stream_distance=3.0,
        step=0,
        mode="BOTH",
        out_path="ue_level0.png",
        start_dir_shift=0,
    )

    # UE Level 1 (3x denser)
    visualize_ue_level(
        origin_vd=VD.CornerParity,
        base_radius=1,
        stream_distance=3.0,
        step=1,
        mode="BOTH",
        out_path="ue_level1.png",
        start_dir_shift=0,
    )    