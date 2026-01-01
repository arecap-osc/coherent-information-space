from __future__ import annotations

from dataclasses import dataclass
from fractions import Fraction
from typing import Dict, Iterable, List, Tuple, Union, Optional
import math

from coherent_space_py.model.enums import InformationalStreamProcessType as SPT
from coherent_space_py.model.enums import InformationalStreamVectorDirection as VD
from coherent_space_py.model.star_rules import star_dir, star_intersection_spts


# -----------------------------
# SAT-only NodeId (intersections are SAT too)
# -----------------------------

@dataclass(frozen=True)
class NodeId:
    """
    SAT-only node id.
    - Base SAT nodes use diag=-1 and frac=0.
    - Intersection SAT nodes use diag in [0..5] and frac in {1,2} for 1/3 and 2/3.
    """
    sat: SPT
    diag: int
    frac: int  # 0 (base), 1 (1/3), 2 (2/3)


NodeRef = Union[SPT, NodeId]


@dataclass(frozen=True)
class EdgeSpec:
    src: NodeRef
    dst: NodeRef
    dim: Fraction   # Fraction(1,3), Fraction(2,3), Fraction(1,1)
    kind: str       # "seg" | "skip" | "full" | "base"


# -----------------------------
# Helpers
# -----------------------------

def _as_node_id(s: SPT) -> NodeId:
    return NodeId(sat=s, diag=-1, frac=0)


def _lerp(a: Tuple[float, float], b: Tuple[float, float], t: float) -> Tuple[float, float]:
    return (a[0] + (b[0] - a[0]) * t, a[1] + (b[1] - a[1]) * t)


# -----------------------------
# Star template (UP ring order must match your geometry)
# -----------------------------

UP_RING: List[SPT] = [
    SPT.UpstreamSelectorFunction,
    SPT.UpstreamConsumerSystem,
    SPT.UpstreamDetectorFunction,
    SPT.UpstreamSelectorSystem,
    SPT.UpstreamConsumerFunction,
    SPT.UpstreamDetectorSystem,
]

# Two triangles inside the star: indices in UP_RING
TRI_A = (0, 2, 4)
TRI_B = (1, 3, 5)

def _triangle_edges(tri: Tuple[int, int, int]) -> List[Tuple[int, int]]:
    a, b, c = tri
    return [(a, b), (b, c), (c, a)]

DIAGONALS: List[Tuple[int, int]] = _triangle_edges(TRI_A) + _triangle_edges(TRI_B)  # 6 diagonals


# -----------------------------
# Overlay builder: intersection SAT nodes + edges
# -----------------------------

def build_star_overlay(
    sat_positions: Dict[SPT, Tuple[float, float]],
    *,
    origin_vd: VD,
    cell_vd: VD,
    use_downstream_candidates_only: bool = True,
) -> Tuple[Dict[NodeId, Tuple[float, float]], List[EdgeSpec]]:
    """
    Build intersections + edges for a single cell.

    - Intersections are modeled as SAT nodes via NodeId(sat=..., diag=..., frac=1|2).
    - SAT is assigned by nearest-candidate rule (default: downstream-only candidates).
    - Returns:
        (intersection_positions, edge_specs)
    """

    # Candidate SATs for assigning intersection SAT type
    if use_downstream_candidates_only:
        candidates = [s for s in sat_positions.keys() if "Downstream" in s.name]
    else:
        candidates = list(sat_positions.keys())

    # Intersection nodes: two per diagonal (t=1/3 and t=2/3)
    inter_pos: Dict[NodeId, Tuple[float, float]] = {}
    edges: List[EdgeSpec] = []

    for diag_id, (i0, i1) in enumerate(DIAGONALS):
        u0 = UP_RING[i0]
        u1 = UP_RING[i1]

        # Align (u0 -> u1) with star_rules orientation for this (origin_vd, cell_vd)
        d = star_dir(origin_vd, cell_vd, u0, u1)
        if d < 0:
            u0, u1 = u1, u0
            i0, i1 = i1, i0  # only if you use i0/i1 later; safe to keep

        p0 = sat_positions[u0]
        p1 = sat_positions[u1]

        # Intersection points at 1/3 and 2/3 along the diagonal
        I1 = _lerp(p0, p1, 1.0 / 3.0)
        I2 = _lerp(p0, p1, 2.0 / 3.0)

        # Deterministic intersection SPTs from star_rules (NO geometry guessing)
        spt_I1, spt_I2 = star_intersection_spts(u0, u1)

        nI1 = NodeId(sat=spt_I1, diag=diag_id, frac=1)
        nI2 = NodeId(sat=spt_I2, diag=diag_id, frac=2)

        inter_pos[nI1] = I1
        inter_pos[nI2] = I2

        # 6 vectors per diagonal (your model)
        edges.append(EdgeSpec(src=_as_node_id(u0), dst=nI1, dim=Fraction(1, 3), kind="seg"))
        edges.append(EdgeSpec(src=nI1, dst=nI2, dim=Fraction(1, 3), kind="seg"))
        edges.append(EdgeSpec(src=nI2, dst=_as_node_id(u1), dim=Fraction(1, 3), kind="seg"))

        edges.append(EdgeSpec(src=_as_node_id(u0), dst=nI2, dim=Fraction(2, 3), kind="skip"))

        # Keep this edge if you still want the "I1 -> downstream base" jump.
        # NOTE: direction cannot be decided by star_dir when sat is same; we keep default orientation in draw.
        edges.append(EdgeSpec(src=nI1, dst=_as_node_id(spt_I1), dim=Fraction(2, 3), kind="skip"))

        edges.append(EdgeSpec(src=_as_node_id(u0), dst=_as_node_id(u1), dim=Fraction(1, 1), kind="full"))

    # NOTE: "base 6 edges" are NOT added here. You can add them outside depending on what you define as base.
    return inter_pos, edges


# -----------------------------
# Drawing helper for overlay
# -----------------------------

def draw_star_overlay(
    ax,
    sat_positions: Dict[SPT, Tuple[float, float]],
    intersection_positions: Dict[NodeId, Tuple[float, float]],
    edges: List[EdgeSpec],
    *,
    origin_vd: VD,
    cell_vd: VD,
    draw_intersection_nodes: bool = True,
    draw_full_edges: bool = True,
    draw_skip_edges: bool = True,
    draw_seg_edges: bool = True,
):
    """
    Draw star overlay edges and intersection nodes on an existing cell plot.
    """

    def pos_of(n: NodeRef) -> Tuple[float, float]:
        if isinstance(n, NodeId):
            if n.diag == -1 and n.frac == 0:
                return sat_positions[n.sat]
            return intersection_positions[n]
        return sat_positions[n]

    def sat_of(n: NodeRef) -> SPT:
        if isinstance(n, NodeId):
            return n.sat
        return n
    
    # Edges
    for e in edges:
        if e.kind == "seg" and not draw_seg_edges:
            continue
        if e.kind == "skip" and not draw_skip_edges:
            continue
        if e.kind == "full" and not draw_full_edges:
            continue

        a_sat = sat_of(e.src)
        b_sat = sat_of(e.dst)

        if a_sat != b_sat:
            d = star_dir(origin_vd, cell_vd, a_sat, b_sat)
            if d < 0:
                src, dst = e.dst, e.src
            else:
                src, dst = e.src, e.dst
        else:
            # Same SAT type: keep the builder direction (NodeId vs base NodeId)
            src, dst = e.src, e.dst
        a = pos_of(src)
        b = pos_of(dst)

        ax.annotate(
            "",
            xy=b, xycoords="data",
            xytext=a, textcoords="data",
            arrowprops=dict(arrowstyle="-|>", color="black", lw=1.6, mutation_scale=10),
            zorder=6,
        )


    # Intersection nodes
    if draw_intersection_nodes:
        for nid, (x, y) in intersection_positions.items():
            # Intersection is SAT-only; color can be tied to SAT family if you want
            ax.plot([x], [y], marker="o", markersize=3.5, color="black", zorder=7)
