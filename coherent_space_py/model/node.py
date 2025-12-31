
from __future__ import annotations

from dataclasses import dataclass, field
from typing import List, Optional, Dict

from coherent_space_py.model.enums import (
    InformationalStreamNetting,
    InformationalStreamVectorDirection
)

@dataclass
class Node:
    """
    A single point in the coherent hexagonal space.
    Mirrors the properties found in the Java 'GraphBean' and 'Node' concepts.
    """

    id: int
    # Complex plane coordinates (Real, Imaginary)
    # Mirrors ComplexPlaneBean
    position: Tuple[float, float]
    
    # The application type (e.g. UpstreamSelectorFunction)
    stream_application_type: StreamApplicationType
    
    # The netting type (e.g. UpstreamEdge)
    netting: InformationalStreamNetting
    
    # The vector direction (SelectorDetectorConsumer vs ConsumerDetectorSelector)
    vector_direction: InformationalStreamVectorDirection
    
    # Orientation string is less critical now that we have strong types, 
    # but kept for backward compat or explicit labeling if needed.
    orientation: str
    
    neighbors: List[int] = field(default_factory=list)
    
    connections: Dict[str, List[str]] = field(default_factory=dict)

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "position": self.position,
            "stream_application_type": str(self.stream_application_type),
            "netting": str(self.netting),
            "vector_direction": str(self.vector_direction),
            "orientation": self.orientation,
            "neighbors": list(self.neighbors),
            "connections": self.connections,
        }
