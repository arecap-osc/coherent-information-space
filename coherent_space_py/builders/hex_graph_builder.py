from __future__ import annotations

from abc import ABC, abstractmethod
from typing import Dict, Iterable, List, Tuple

import numpy as np

from coherent_space_py.geometry.roots_of_unity import roots_of_12
from coherent_space_py.model.node import Node
from coherent_space_py.model.multivalent_node import MultivalentNode
from coherent_space_py.utils.gauss import gauss_sum


# Note: the ordering of directions matters for ring traversal when placing nodes.
# Starting from (radius, 0) and stepping through these vectors keeps every point
# on the ring at the same axial distance from the origin while walking around
# the hexagon.
AXIAL_DIRECTIONS: List[Tuple[int, int]] = [
    (0, -1),
    (-1, 0),
    (-1, 1),
    (0, 1),
    (1, 0),
    (1, -1),
]


class HexGraphBuilder(ABC):
    """Base class encapsulating the geometry shared by all builders."""

    #: Selector/Detector/Consumer cycle for a vector pointing from the center.
    TYPE_CYCLE = ("selector", "detector", "consumer")

    def __init__(self, orientation: str, type_offset: int = 0):
        self.orientation = orientation
        self.type_offset = type_offset % len(self.TYPE_CYCLE)

    @staticmethod
    def axial_to_cartesian(q: int, r: int, step_distance: float) -> Tuple[float, float]:
        """Map axial hex coordinates to cartesian space.

        This uses the common pointy-top axial mapping and then rotates the
        resulting vector according to a 12th root of unity so that the new grid
        aligns with the Java builder layout.
        """
        x = step_distance * (np.sqrt(3) * (q + r / 2))
        y = step_distance * (1.5 * r)
        # Rotate 30 degrees to mirror the RootsOfUnity orientation used in Java.
        rot_x, rot_y = roots_of_12(2, 1.0)
        rotation = np.array([[rot_x, -rot_y], [rot_y, rot_x]])
        return tuple(np.matmul(rotation, np.array([x, y])).astype(float))

    @staticmethod
    def vector_direction(q: int, r: int) -> str:
        return "SelectorDetectorConsumer" if (q + r) % 2 == 0 else "ConsumerDetectorSelector"

    def node_type(self, radius: int, index_on_ring: int) -> str:
        idx = (radius + index_on_ring + self.type_offset) % len(self.TYPE_CYCLE)
        return self.TYPE_CYCLE[idx]

    @abstractmethod
    def build_hex_grid(self, step_count: int) -> Dict[int, Node]:
        ...


class _DefaultHexGraphBuilder(HexGraphBuilder):
    """Concrete implementation shared by all specific directional builders."""

    def build_hex_grid(self, step_count: int) -> Dict[int, Node]:
        if step_count < 0:
            raise ValueError("step_count must be non-negative")

        nodes: Dict[int, Node] = {}
        axial_index: Dict[Tuple[int, int], int] = {}

        def add_node(node_id: int, q: int, r: int, radius: int, index_on_ring: int):
            position = self.axial_to_cartesian(q, r, step_distance=1.0)
            node = Node(
                id=node_id,
                position=position,
                type=self.node_type(radius, index_on_ring),
                vector_direction=self.vector_direction(q, r),
                orientation=self.orientation,
            )
            nodes[node_id] = node
            axial_index[(q, r)] = node_id

        # Center node
        add_node(0, 0, 0, 0, 0)

        for radius in range(1, step_count + 1):
            start_q, start_r = radius, 0
            node_id = gauss_sum(6, radius - 1) + 1
            q, r = start_q, start_r
            index_on_ring = 0
            for direction_index, (dq, dr) in enumerate(AXIAL_DIRECTIONS):
                steps = radius
                for _ in range(steps):
                    add_node(node_id, q, r, radius, index_on_ring)
                    index_on_ring += 1
                    node_id += 1
                    q += dq
                    r += dr

        # Build neighbor lists using axial adjacency.
        for (q, r), node_id in axial_index.items():
            neighbor_ids = []
            for dq, dr in AXIAL_DIRECTIONS:
                neighbor = (q + dq, r + dr)
                if neighbor in axial_index:
                    neighbor_ids.append(axial_index[neighbor])
            nodes[node_id].neighbors = sorted(set(neighbor_ids))

        return nodes

    def build_multivalent_grid(
        self,
        step_count: int,
        logic_layers: Iterable[str] = ("function", "system"),
    ) -> Dict[Tuple[int, str], MultivalentNode]:
        """Generate a logical overlay for each physical node.

        Every physical node is replicated for each logic layer, keeping the
        geometry and neighbors identical but shifting the trivalent role based
        on the layer. The simplest policy used here is to rotate the
        selector/detector/consumer cycle by one position for every additional
        layer, mirroring the Java builders where function/system alternate
        parity-driven roles.
        """

        physical_nodes = self.build_hex_grid(step_count)
        layers = list(logic_layers)
        if not layers:
            raise ValueError("logic_layers must contain at least one entry")

        overlay: Dict[Tuple[int, str], MultivalentNode] = {}
        for idx, layer in enumerate(layers):
            for node_id, node in physical_nodes.items():
                logic_role = self.TYPE_CYCLE[(idx + self.TYPE_CYCLE.index(node.type)) % len(self.TYPE_CYCLE)]
                overlay[(node_id, layer)] = MultivalentNode(
                    physical_id=node_id,
                    logic_layer=layer,
                    logic_role=logic_role,
                    position=node.position,
                    orientation=node.orientation,
                    vector_direction=node.vector_direction,
                    neighbors=list(node.neighbors),
                )

        return overlay


class UpstreamEdgeHexGraphBuilder(_DefaultHexGraphBuilder):
    def __init__(self):
        super().__init__(orientation="upstream_edge", type_offset=0)


class DownstreamEdgeHexGraphBuilder(_DefaultHexGraphBuilder):
    def __init__(self):
        super().__init__(orientation="downstream_edge", type_offset=1)


class UpstreamVertexHexGraphBuilder(_DefaultHexGraphBuilder):
    def __init__(self):
        super().__init__(orientation="upstream_vertex", type_offset=2)


class DownstreamVertexHexGraphBuilder(_DefaultHexGraphBuilder):
    def __init__(self):
        super().__init__(orientation="downstream_vertex", type_offset=1)
