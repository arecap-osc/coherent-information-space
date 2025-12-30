from __future__ import annotations

from dataclasses import dataclass, field
from typing import List, Tuple


@dataclass
class MultivalentNode:
    """A logical overlay for a physical hex node.

    Each physical node can host multiple logical roles (e.g. function/system)
    that reuse the same geometric position and neighbor relationships but shift
    the trivalent selector/detector/consumer assignment.
    """

    physical_id: int
    logic_layer: str  # e.g. "function" or "system"
    logic_role: str  # selector | detector | consumer
    position: Tuple[float, float]
    orientation: str
    vector_direction: str
    neighbors: List[int] = field(default_factory=list)

    def to_dict(self) -> dict:
        return {
            "physical_id": self.physical_id,
            "logic_layer": self.logic_layer,
            "logic_role": self.logic_role,
            "position": self.position,
            "orientation": self.orientation,
            "vector_direction": self.vector_direction,
            "neighbors": list(self.neighbors),
        }


__all__ = ["MultivalentNode"]
