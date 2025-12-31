
from __future__ import annotations

from dataclasses import dataclass, field
from typing import List, Dict, Tuple

from coherent_space_py.model.enums import (
    InformationalStreamNeuronType,
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
    
    # Tipul de "neuron" logic (fost StreamApplicationType), ex: UpstreamSelectorFunction
    stream_application_type: InformationalStreamNeuronType
    
    # The netting type (e.g. UpstreamEdge)
    netting: InformationalStreamNetting
    
    # The vector direction (SelectorDetectorConsumer vs ConsumerDetectorSelector)
    vector_direction: InformationalStreamVectorDirection
    
    # Orientation string is less critical now that we have strong types, 
    # but kept for backward compat or explicit labeling if needed.
    orientation: str
    
    neighbors: List[int] = field(default_factory=list)
    
    connections: Dict[str, List[int]] = field(default_factory=dict)

    # Fully-qualified linkage to the canonical informational-stream namespace.
    # Mirrors org.arecap.eden.ia.console.informationalstream.* wiring.
    namespace: str = "org.arecap.eden.ia.console.informationalstream.api"

    # Canonicalized network links keyed by fully qualified StreamApplicationType name.
    network_links: Dict[str, List[int]] = field(default_factory=dict)

    # Axial-like lattice coordinates inferred from the builder (_steps real/imag).
    lattice_steps: Tuple[int, int] = field(default_factory=tuple)

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
            "namespace": self.namespace,
            "network_links": self.network_links,
            "lattice_steps": self.lattice_steps,
        }
