from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, Tuple, List

from coherent_space_py.model.enums import InformationalStreamProcessType as SPT
from coherent_space_py.model.enums import InformationalStreamVectorDirection as VD
from coherent_space_py.model.topology_rules import get_upstream_connections

# --------------------------------------------
# Diagonals in the upstream "Star of David"
# --------------------------------------------
# Two triangles:
#   Function triangle:  USF-UDF-UCF
#   System triangle:    USS-UDS-UCS
#
# We will treat a diagonal as an ORDERED pair (u0 -> u1)
# and define the two intersection SPTs at 1/3 and 2/3:
#   u0 --(1/3)-> I1 --(1/3)-> I2 --(1/3)-> u1
#
# Your provided mapping (corner-mode baseline):
#   (USF,UDF)->(DDS,DSF)
#   (USF,UCF)->(DCF,DSS)
#   (UDF,UCF)->(DCS,DDF)
#   (USS,UDS)->(DCS,DSF)
#   (UCS,USS)->(DDF,DSS)
#   (UDS,UCS)->(DDS,DCF)
#
# --------------------------------------------

Diagonal = Tuple[SPT, SPT]
Intersections = Tuple[SPT, SPT]

BASE_CORNER_MAP: Dict[Diagonal, Intersections] = {
    (SPT.UpstreamDetectorFunction, SPT.UpstreamSelectorFunction): (SPT.DownstreamSelectorSystem, SPT.DownstreamConsumerFunction),
    (SPT.UpstreamSelectorFunction, SPT.UpstreamConsumerFunction): (SPT.DownstreamDetectorSystem, SPT.DownstreamSelectorFunction),
    (SPT.UpstreamConsumerFunction, SPT.UpstreamDetectorFunction): (SPT.DownstreamConsumerSystem, SPT.DownstreamDetectorFunction),

    (SPT.UpstreamSelectorSystem,   SPT.UpstreamDetectorSystem):   (SPT.DownstreamConsumerSystem, SPT.DownstreamSelectorFunction),
    (SPT.UpstreamSelectorSystem,   SPT.UpstreamConsumerSystem):   (SPT.DownstreamDetectorFunction, SPT.DownstreamSelectorSystem),
    (SPT.UpstreamConsumerSystem,   SPT.UpstreamDetectorSystem):   (SPT.DownstreamDetectorSystem, SPT.DownstreamConsumerFunction),
}

DIAGONALS: List[Diagonal] = list(BASE_CORNER_MAP.keys())

def is_vertex_mode(origin_vd: VD, cell_vd: VD) -> bool:
    """
    Match your upstream topology_rules meaning:
    vertex-mode = (origin CornerParity and cell CornerParity)
    """
    return (origin_vd == VD.CornerParity and cell_vd == VD.CornerParity)

def canonical_forward(origin_vd: VD, cell_vd: VD, u0: SPT, u1: SPT) -> bool:
    """
    Decide if the 'forward' direction should be u0->u1.
    We use upstream topology_rules to align with the ring's directed space:
      if u1 is in targets(u0) => forward
      elif u0 is in targets(u1) => reverse
      else => keep u0->u1 (should not happen for star diagonals if model is coherent)
    """
    if u1 in set(get_upstream_connections(u0, cell_vd, origin_vd)):
        return True
    if u0 in set(get_upstream_connections(u1, cell_vd, origin_vd)):
        return False
    return True

def star_intersections(origin_vd: VD, cell_vd: VD, u0: SPT, u1: SPT) -> Tuple[SPT, SPT, bool]:
    """
    Return (I1, I2, forward_is_u0_to_u1).

    - Uses your BASE_CORNER_MAP as the semantic definition.
    - If the diagonal is requested reversed (u1,u0), we swap intersections (I2,I1).
    - If parity implies a global direction inversion, we invert using canonical_forward() alignment.

    NOTE:
      Right now we assume BASE_CORNER_MAP is the correct semantic mapping.
      Parity effects are handled by direction alignment, not by changing which intersections exist.
    """
    # Find mapping for (u0,u1) or (u1,u0)
    if (u0, u1) in BASE_CORNER_MAP:
        I1, I2 = BASE_CORNER_MAP[(u0, u1)]
        base_forward = True
    elif (u1, u0) in BASE_CORNER_MAP:
        # reversed diagonal: swap intersections
        I2, I1 = BASE_CORNER_MAP[(u1, u0)]
        base_forward = True  # because we already adapted to u0->u1 by swapping
    else:
        raise KeyError(f"Missing star diagonal mapping for {u0.name} <-> {u1.name}")

    # Align direction with upstream ring space
    forward = canonical_forward(origin_vd, cell_vd, u0, u1)

    # If forward is False, we are conceptually going u1->u0.
    # For geometry building we will swap endpoints and also swap intersections.
    if not forward:
        return (I2, I1, False)

    return (I1, I2, True)
