
"""Infinite Coherent Grid Model."""
from typing import Dict, List, Optional

from coherent_space_py.model.node import Node
from coherent_space_py.math.lattice import (
    LatticeConfig,
    LatticeMath,
    UpstreamEdgeLattice,
    DownstreamEdgeLattice,
    UpstreamVertexLattice,
    DownstreamVertexLattice,
)
from coherent_space_py.model.enums import StreamApplicationType
from coherent_space_py.model.topology_rules import (
    get_downstream_connections,
    get_upstream_connections,
)

class InfiniteCoherentGraph:
    """
    Virtual Graph that computes nodes on-the-fly using Quad Lattice math and
    post-processes them with hexavalent connection rules mirrored from
    org.arecap.eden.ia.console.informationalstream.
    """
    
    def __init__(self, stream_distance: float = 1.0, scale: float = 1.0, namespace: str = "org.arecap.eden.ia.console.informationalstream.api"):
        self.config = LatticeConfig(
            stream_distance=stream_distance,
            step=0, # Base step
            scale=scale
        )
        self.namespace = namespace
        
        # Initialize Lattices
        self.lattices = [
            UpstreamEdgeLattice(self.config),
            DownstreamEdgeLattice(self.config),
            UpstreamVertexLattice(self.config),
            DownstreamVertexLattice(self.config)
        ]
        
    def get_patch(self, q_range: range, r_range: range, origin: complex = 0j) -> List[Node]:
        """
        Generates nodes for a specific patch of the infinite grid.
        
        Args:
            q_range: Range of 'real' steps (e.g. range(0, 5))
            r_range: Range of 'imaginary' steps (e.g. range(0, 5))
            origin: The complex origin for this patch.
            
        Returns:
            List of superimposed nodes.
        """
        all_nodes = []
        
        for lat in self.lattices:
            # Apply specific scroll for each lattice?
            # In Java, the scroll determines the 'quota' for step calculation.
            # Here we assume q,r ARE the steps.
            # But we must apply the scroll offset to the final position relative to origin?
            # Or is the scroll intrinsic to the lattice zero-point?
            
            # Java: result.setReal(origin.getReal()); ... for ID 0.
            # So (0,0) is at Origin.
            # But the Lattice definition might imply (0,0) IS the scroll point?
            # Checking Java: "ComplexPlane scroll = getScroll(root);"
            # "Double realQuota = scroll.getReal();"
            # "Integer realSteps = (graphPosition - origin) / realQuota"
            
            # So the STEP SIZE is determined by the Scroll.
            # And the lattice 0,0 is at Origin.
            
            # So valid nodes are generated relative to 'origin' using q,r.
            
            for q in q_range:
                for r in r_range:
                    nodes = lat.get_nodes(q, r, origin)
                    all_nodes.extend(nodes)

        self._attach_hexavalent_links(all_nodes)
        return all_nodes

    # --- Hexavalent / informational-stream aware helpers ---

    def _as_complex(self, position) -> complex:
        """Normalize a node position to complex arithmetic for distance checks."""
        if isinstance(position, complex):
            return position
        if isinstance(position, (tuple, list)) and len(position) >= 2:
            return complex(position[0], position[1])
        raise TypeError(f"Unsupported position type: {type(position)}")

    def _nearest_target(self, origin: Node, candidates: List[Node]) -> Optional[Node]:
        """Select the closest node (Euclidean distance) that is not the origin itself."""
        origin_pos = self._as_complex(origin.position)
        filtered = [cand for cand in candidates if cand.id != origin.id]
        if not filtered:
            return None
        return min(filtered, key=lambda cand: abs(self._as_complex(cand.position) - origin_pos))

    def _group_by_type(self, nodes: List[Node]) -> Dict[StreamApplicationType, List[Node]]:
        grouped: Dict[StreamApplicationType, List[Node]] = {}
        for node in nodes:
            grouped.setdefault(node.stream_application_type, []).append(node)
        return grouped

    def _attach_hexavalent_links(self, nodes: List[Node]) -> None:
        """
        Populate neighbor/connection maps using the same rules as the Java
        informational stream builders. Each connection is also exposed under a
        fully-qualified key rooted in org.arecap.eden.ia.console.informationalstream.
        """
        nodes_by_type = self._group_by_type(nodes)

        for node in nodes:
            node.namespace = self.namespace
            is_upstream = node.stream_application_type.value.startswith("Upstream")
            target_types = (
                get_upstream_connections(node.stream_application_type, node.vector_direction)
                if is_upstream
                else get_downstream_connections(node.stream_application_type, node.vector_direction)
            )

            for target_type in target_types:
                candidates = nodes_by_type.get(target_type, [])
                target_node = self._nearest_target(node, candidates)
                if target_node is None:
                    continue

                qualified_key = f"{self.namespace}.StreamApplicationType.{target_type.name}"
                node.connections[target_type.name] = [target_node.id]
                node.network_links[qualified_key] = [target_node.id]

                if target_node.id not in node.neighbors:
                    node.neighbors.append(target_node.id)
