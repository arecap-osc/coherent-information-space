# file: draw_cell_subgraph.py
import math
import matplotlib.pyplot as plt


# ---------- config enums (lightweight) ----------
UP = "upstream"
DOWN = "downstream"
CORNER = "corner"
SIDE = "side"

PORTS = ["SF", "SS", "DF", "DS", "CF", "CS"]  # selector/detector/consumer x function/system


def rot(deg: float):
    a = math.radians(deg)
    return (math.cos(a), math.sin(a))


def scale_pt(pt, s):
    return (pt[0] * s, pt[1] * s)


def add(a, b):
    return (a[0] + b[0], a[1] + b[1])


def cell_geometry(family: str, parity: str, SD: float):
    """
    Returns:
      - hex_vertices: list[(x,y)] (6 points)
      - star_points: list[(x,y)] (12 points, inner/outer alternation)
      - port_points: dict[PORT -> (x,y)]  (6 points)
      - internal_edges: list[(p1, p2)] where p1/p2 are points (x,y)
    Notes:
      This is a BASELINE geometry. We'll tweak it to match your Java exact roots-of-unity.
    """
    # Pointy-top hex: top vertex at 90 deg
    # parity changes phase: CORNER uses vertices; SIDE uses rotated by 30deg (edges-centered phase)
    phase = 0 if parity == CORNER else 30

    # Up/Down can be modeled as additional phase or mirrored Y depending on your convention.
    # We'll start with: DOWN mirrors Y (easy to see), then adjust to your real meaning.
    mirror_y = (family == DOWN)

    # Outer hex radius
    R = SD

    # 6 hex vertices
    hex_vertices = []
    for k in range(6):
        deg = phase + 90 - k * 60
        x, y = scale_pt(rot(deg), R)
        if mirror_y:
            y = -y
        hex_vertices.append((x, y))

    # Star (David): alternate between outer-ish and inner-ish points (12 points)
    # Outer-ish radius and inner-ish radius (tweakable)
    R_outer = SD * 0.90
    R_inner = SD * 0.45

    star_points = []
    for k in range(12):
        deg = phase + 90 - k * 30
        rr = R_outer if (k % 2 == 0) else R_inner
        x, y = scale_pt(rot(deg), rr)
        if mirror_y:
            y = -y
        star_points.append((x, y))

    # Ports: 6 points (one per 60 degrees). For now place them on inner radius,
    # but we'll remap SF/SS/DF/DS/CF/CS after you confirm orientation.
    port_points = {}
    port_radius = SD * 0.62
    for i, port in enumerate(PORTS):
        deg = phase + 90 - i * 60
        x, y = scale_pt(rot(deg), port_radius)
        if mirror_y:
            y = -y
        port_points[port] = (x, y)

    # Internal edges: draw star as triangle up + triangle down (via every 4th point on 12-gon)
    # This gives a David-star-like structure.
    # Triangle A uses indices: 0, 4, 8 ; Triangle B uses indices: 2, 6, 10
    triA = [star_points[i] for i in (0, 4, 8)]
    triB = [star_points[i] for i in (2, 6, 10)]
    internal_edges = []
    for tri in (triA, triB):
        internal_edges.append((tri[0], tri[1]))
        internal_edges.append((tri[1], tri[2]))
        internal_edges.append((tri[2], tri[0]))

    # Optional: connect ports to nearest star points (placeholder)
    # We'll replace with your true role-specific connections later.
    for i, port in enumerate(PORTS):
        internal_edges.append((port_points[port], star_points[(i * 2) % 12]))

    return hex_vertices, star_points, port_points, internal_edges


def draw_cell(ax, family: str, parity: str, SD: float, show_labels=True):
    hex_vertices, star_points, port_points, internal_edges = cell_geometry(family, parity, SD)

    # draw hex outline
    hx = [p[0] for p in hex_vertices] + [hex_vertices[0][0]]
    hy = [p[1] for p in hex_vertices] + [hex_vertices[0][1]]
    ax.plot(hx, hy, linewidth=2)

    # draw star edges
    for a, b in internal_edges:
        ax.plot([a[0], b[0]], [a[1], b[1]], linewidth=1)

    # draw star points
    ax.scatter([p[0] for p in star_points], [p[1] for p in star_points], s=15)

    # draw ports
    ax.scatter([port_points[p][0] for p in PORTS], [port_points[p][1] for p in PORTS], s=45)

    if show_labels:
        for p in PORTS:
            x, y = port_points[p]
            ax.text(x, y, p, fontsize=9, ha="center", va="center")

    ax.set_aspect("equal", "box")
    ax.grid(True, linewidth=0.3)
    ax.set_title(f"{family.upper()} / {parity.upper()}")


def main():
    SD = 3.0  # use small number for screen; later replace with your 600 etc. and scaling
    fig, axes = plt.subplots(2, 2, figsize=(10, 10))

    draw_cell(axes[0][0], UP, CORNER, SD)
    draw_cell(axes[0][1], UP, SIDE, SD)
    draw_cell(axes[1][0], DOWN, CORNER, SD)
    draw_cell(axes[1][1], DOWN, SIDE, SD)

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    main()
