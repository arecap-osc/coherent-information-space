from __future__ import annotations

from typing import Dict, Iterable, List, Tuple
import numpy as np

from coherent_space_py.geometry.roots_of_unity import roots_of_12
from coherent_space_py.utils.gauss import gauss_sum
from coherent_space_py.model.node import Node
from coherent_space_py.model.graph import InformationalStreamGraph
from coherent_space_py.model.enums import (
    StreamApplicationType,
    InformationalStreamNetting,
    InformationalStreamVectorDirection
)

# Note: the ordering of directions matters for ring traversal
AXIAL_DIRECTIONS: List[Tuple[int, int]] = [
    (0, -1), (-1, 0), (-1, 1), (0, 1), (1, 0), (1, -1),
]

class CoherentSpaceBuilder:
    """
    Builder for the Coherent Information Space graph.
    Generates a graph where each physical location hosts multiple logical nodes
    (Function/System, Upstream/Downstream) based on its geometric role (Selector/Detector/Consumer).
    """

    TYPE_CYCLE = ("Selector", "Detector", "Consumer")

    def __init__(self, netting: InformationalStreamNetting):
        self.netting = netting
        # Determine offset based on netting type to match Java/Legacy logic
        # UpstreamEdge: 0, DownstreamEdge: 1, UpstreamVertex: 2...
        if netting == InformationalStreamNetting.UpstreamEdge:
            self.type_offset = 0
            self.base_orientation = "upstream_edge"
        elif netting == InformationalStreamNetting.DownstreamEdge:
            self.type_offset = 1
            self.base_orientation = "downstream_edge"
        elif netting == InformationalStreamNetting.UpstreamVertex:
            self.type_offset = 2
            self.base_orientation = "upstream_vertex"
        elif netting == InformationalStreamNetting.DownstreamVertex:
            self.type_offset = 1
            self.base_orientation = "downstream_vertex"
        else:
            self.type_offset = 0
            self.base_orientation = "unknown"

    def _axial_to_cartesian(self, q: int, r: int, step_distance: float) -> Tuple[float, float]:
        """Map axial hex coordinates to cartesian space with rotation."""
        x = step_distance * (np.sqrt(3) * (q + r / 2))
        y = step_distance * (1.5 * r)
        # Rotate 30 degrees to mirror the RootsOfUnity orientation used in Java
        rot_x, rot_y = roots_of_12(2, 1.0)
        # Standard rotation matrix
        rotation = np.array([[rot_x, -rot_y], [rot_y, rot_x]])
        result = np.matmul(rotation, np.array([x, y])).astype(float)
        return float(result[0]), float(result[1])

    def _get_vector_direction(self, q: int, r: int) -> InformationalStreamVectorDirection:
        if (q + r) % 2 == 0:
            return InformationalStreamVectorDirection.SelectorDetectorConsumer
        else:
            return InformationalStreamVectorDirection.ConsumerDetectorSelector

    def _get_geometric_role(self, radius: int, index_on_ring: int) -> str:
        idx = (radius + index_on_ring + self.type_offset) % 3
        return self.TYPE_CYCLE[idx]

    def _get_application_types(self, role: str) -> List[StreamApplicationType]:
        """
        Returns the 4 application types valid for a given geometric role (S/D/C).
        e.g. Role "Selector" -> [UpstreamSelectorFunction, UpstreamSelectorSystem, Downstream..., Downstream...]
        """
        types = []
        for t in StreamApplicationType:
            # t.value looks like "UpstreamSelectorFunction"
            if role in t.value:
                types.append(t)
        return types

    def build(self, step_count: int) -> InformationalStreamGraph:
        if step_count < 0:
            raise ValueError("step_count must be non-negative")

        graph = InformationalStreamGraph()
        
        # Temporary storage to build neighbors based on (q,r) adjacency
        # Map (q,r) -> List[Node] (since one physical point has multiple nodes)
        physical_map: Dict[Tuple[int, int], List[Node]] = {}

        next_id = 0

        def add_physical_point(q: int, r: int, radius: int, index_on_ring: int):
            nonlocal next_id
            position = self._axial_to_cartesian(q, r, step_distance=1.0)
            base_role = self._get_geometric_role(radius, index_on_ring)
            vector_dir = self._get_vector_direction(q, r)
            
            valid_types = self._get_application_types(base_role)
            
            nodes_at_point = []
            for app_type in valid_types:
                # We create a node for each valid type at this location
                # IDs are sequential. In a real DB they might be different.
                node = Node(
                    id=next_id,
                    position=position,
                    stream_application_type=app_type,
                    netting=self.netting,
                    vector_direction=vector_dir,
                    orientation=self.base_orientation
                )
                graph.add_node(node)
                nodes_at_point.append(node)
                next_id += 1
            
            physical_map[(q, r)] = nodes_at_point

        # Center node
        add_physical_point(0, 0, 0, 0)

        for radius in range(1, step_count + 1):
            start_q, start_r = radius, 0
            # Java logic for ID skipping is complex, here we just sequence IDs for now.
            # If preserving exact ID parity with Java is required, we'd need gauss_sum logic back 
            # for the base ID, but since we generate multiple nodes per point, custom logic is needed.
            
            q, r = start_q, start_r
            index_on_ring = 0
            for direction_index, (dq, dr) in enumerate(AXIAL_DIRECTIONS):
                steps = radius
                for _ in range(steps):
                    add_physical_point(q, r, radius, index_on_ring)
                    index_on_ring += 1
                    q += dq
                    r += dr

        # Build neighbors
        # We only connect nodes that satisfy the Topology rules.
        from coherent_space_py.model.topology import get_stream_topology, StreamTopology

        for (q, r), nodes in physical_map.items():
            for node in nodes:
                # Check 6 physical neighbors
                for dq, dr in AXIAL_DIRECTIONS:
                    neighbor_coord = (q + dq, r + dr)
                    if neighbor_coord in physical_map:
                        potential_neighbors = physical_map[neighbor_coord]
                        for pn in potential_neighbors:
                            # Check if topologically valid connection
                            # We assume connections are directed? Or undirected?
                            # Java graph is usually directed or bidirectional.
                            # Topology utils defines valid PAIRS.
                            
                            # Check forward connection (Node -> Neighbor)
                            topo = get_stream_topology(node.stream_application_type, pn.stream_application_type)
                            if topo != StreamTopology.Trail:
                                node.neighbors.append(pn.id)
                            
                            # Note: if the graph is undirected, we'd check reverse too, 
                            # but usually we iterate all nodes so the reverse check happens when we visit 'pn'.

        return graph

