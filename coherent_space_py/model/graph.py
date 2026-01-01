from __future__ import annotations
from typing import Dict, List, Optional
from dataclasses import dataclass, field

from coherent_space_py.model.node import Node
from coherent_space_py.model.enums import InformationalStreamProcessType

@dataclass
class InformationalStreamGraph:
    """
    A container representing the complete Coherent Information Space,
    holding the 12 distinct sub-graphs (Stream Application Types).
    """

    # We store nodes keyed by ID for all graphs combined, 
    # but we can also organize them by application type.
    nodes: Dict[int, Node] = field(default_factory=dict)
    
    # Index to quickly retrieve nodes by their application type
    _by_type: Dict[InformationalStreamProcessType, List[Node]] = field(default_factory=dict)

    def add_node(self, node: Node) -> None:
        """Add a node to the graph and index it."""
        self.nodes[node.id] = node
        if node.stream_application_type not in self._by_type:
            self._by_type[node.stream_application_type] = []
        self._by_type[node.stream_application_type].append(node)

    def get_nodes_by_type(self, app_type: InformationalStreamProcessType) -> List[Node]:
        """Retrieve all nodes of a specific stream application type."""
        return self._by_type.get(app_type, [])

    def get_upstreams(self) -> List[Node]:
        """Get all nodes belonging to Upstream topologies."""
        result = []
        for t in InformationalStreamProcessType:
            if t.value.startswith("Upstream"):
                result.extend(self.get_nodes_by_type(t))
        return result

    def get_downstreams(self) -> List[Node]:
        """Get all nodes belonging to Downstream topologies."""
        result = []
        for t in InformationalStreamProcessType:
            if t.value.startswith("Downstream"):
                result.extend(self.get_nodes_by_type(t))
        return result
