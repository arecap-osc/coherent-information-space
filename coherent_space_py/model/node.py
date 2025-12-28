from __future__ import annotations

from dataclasses import dataclass, field
from typing import List, Tuple


@dataclass
class Node:
    """A single point in the coherent hexagonal space."""

    id: int
    position: Tuple[float, float]
    type: str
    vector_direction: str
    orientation: str
    neighbors: List[int] = field(default_factory=list)

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "position": self.position,
            "type": self.type,
            "vector_direction": self.vector_direction,
            "orientation": self.orientation,
            "neighbors": list(self.neighbors),
        }
