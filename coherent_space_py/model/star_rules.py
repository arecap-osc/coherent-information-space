from __future__ import annotations

from dataclasses import dataclass
from fractions import Fraction
from typing import Dict, List, Tuple, Set

from coherent_space_py.model.enums import InformationalStreamProcessType as SPT
from coherent_space_py.model.enums import InformationalStreamVectorDirection as VD
from coherent_space_py.model.topology_rules import get_upstream_connections, get_downstream_connections

# ---------------------------------
# Star template: upstream ring order
# ---------------------------------

UP_RING: List[SPT] = [
    SPT.UpstreamSelectorFunction,
    SPT.UpstreamConsumerSystem,
    SPT.UpstreamDetectorFunction,
    SPT.UpstreamSelectorSystem,
    SPT.UpstreamConsumerFunction,
    SPT.UpstreamDetectorSystem,
]

# Two triangles inside the star (indices in UP_RING)
TRI_A = (0, 2, 4)  # (SF, DF, CF)
TRI_B = (1, 3, 5)  # (CS, SS, DS)

def _tri_edges(tri: Tuple[int, int, int]) -> List[Tuple[int, int]]:
    a, b, c = tri
    return [(a, b), (b, c), (c, a)]

# 6 "big diagonals" = edges of the two triangles
DIAGONALS: List[Tuple[SPT, SPT]] = [
    (UP_RING[i0], UP_RING[i1]) for (i0, i1) in (_tri_edges(TRI_A) + _tri_edges(TRI_B))
]

# ---------------------------------
# Helper: role extraction
# ---------------------------------

def _role(s: SPT) -> str:
    n = s.name
    if "Selector" in n: return "Selector"
    if "Detector" in n: return "Detector"
    if "Consumer" in n: return "Consumer"
    raise ValueError(n)

def _fs(s: SPT) -> str:
    n = s.name
    if "Function" in n: return "Function"
    if "System" in n: return "System"
    raise ValueError(n)

def _mk_down(role: str, fs: str) -> SPT:
    # Downstream + Role + Function/System
    name = f"Downstream{role}{fs}"
    return getattr(SPT, name)

def _mk_up(role: str, fs: str) -> SPT:
    name = f"Upstream{role}{fs}"
    return getattr(SPT, name)

# ---------------------------------
# YOUR KEY RULE: which downstream SPTs are the "two intersections" on a diagonal
# ---------------------------------

def star_intersection_spts(a_up: SPT, b_up: SPT) -> Tuple[SPT, SPT]:
    """
    Return (I1_spt, I2_spt) for the diagonal between a_up and b_up.

    Design goal:
      - Deterministic, fast, no geometry.
      - Must match your model: e.g. USF--UCF intersects DDS and DSF.

    Current convention (easy to change in one place):
      - a_up and b_up are both in the same triangle (both Function OR both System)
      - missing role = the third role not in {role(a), role(b)}
      - I1 = Downstream(missing_role, opposite FS)
      - I2 = Downstream(role(a), same FS)

    Example:
      a=UpstreamSelectorFunction, b=UpstreamConsumerFunction
        roles = {Selector, Consumer} => missing Detector
        fs = Function => opposite System
        I1 = DownstreamDetectorSystem  (DDS)  ✅ matches your example
        I2 = DownstreamSelectorFunction (DSF) ✅ matches your example (anchored to a)
    """
    ra, rb = _role(a_up), _role(b_up)
    fsa, fsb = _fs(a_up), _fs(b_up)
    if fsa != fsb:
        raise ValueError("Star diagonal endpoints must belong to same triangle (same FS).")

    roles = {"Selector", "Detector", "Consumer"}
    missing = list(roles - {ra, rb})[0]
    opposite_fs = "System" if fsa == "Function" else "Function"

    I1 = _mk_down(missing, opposite_fs)
    I2 = _mk_down(ra, fsa)
    return (I1, I2)

# ---------------------------------
# Direction resolution: use topology_rules where applicable
# ---------------------------------

def _ring_dir(origin_vd: VD, cell_vd: VD, a: SPT, b: SPT) -> int:
    """
    Return +1 if a->b per topology_rules, -1 if b->a, else 0.
    """
    if "Upstream" in a.name:
        if b in set(get_upstream_connections(a, cell_vd, origin_vd)): return +1
    else:
        if b in set(get_downstream_connections(a, cell_vd, origin_vd)): return +1

    if "Upstream" in b.name:
        if a in set(get_upstream_connections(b, cell_vd, origin_vd)): return -1
    else:
        if a in set(get_downstream_connections(b, cell_vd, origin_vd)): return -1

    return 0

# ---------------------------------
# Build the 36 star edges as SAT->SAT rules
# ---------------------------------

# STAR_RULES[(origin_vd, cell_vd)][src] = set(dsts)
STAR_RULES: Dict[Tuple[VD, VD], Dict[SPT, Set[SPT]]] = {}

def build_star_rules(origin_vd: VD, cell_vd: VD) -> Dict[SPT, Set[SPT]]:
    """
    Build the full 36 directed edges (6 diagonals × 6 vectors/diagonal) as SPT->SPT.
    """
    rules: Dict[SPT, Set[SPT]] = {}

    def add(u: SPT, v: SPT):
        rules.setdefault(u, set()).add(v)

    for (a_up, b_up) in DIAGONALS:
        # Decide base diagonal orientation using topology_rules if possible.
        d = _ring_dir(origin_vd, cell_vd, a_up, b_up)
        if d < 0:
            a_up, b_up = b_up, a_up  # swap to keep (a->b) consistent

        I1, I2 = star_intersection_spts(a_up, b_up)

        # 6 vectors per diagonal (your definition):
        # dim 1/3 segments:
        add(a_up, I1)
        add(I1, I2)
        add(I2, b_up)

        # dim 2/3:
        add(a_up, I2)

        # dim 1:
        add(a_up, b_up)

    return rules

def init_star_rules():
    for o in (VD.CornerParity, VD.SideParity):
        for c in (VD.CornerParity, VD.SideParity):
            STAR_RULES[(o, c)] = build_star_rules(o, c)

init_star_rules()

def star_dir(origin_vd: VD, cell_vd: VD, a: SPT, b: SPT) -> int:
    """
    Star direction oracle:
      +1 if a->b is in STAR_RULES,
      -1 if b->a is in STAR_RULES,
       0 otherwise.
    """
    m = STAR_RULES.get((origin_vd, cell_vd), {})
    if b in m.get(a, set()): return +1
    if a in m.get(b, set()): return -1
    return 0
