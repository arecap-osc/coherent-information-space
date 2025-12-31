from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, Iterable, List, Tuple

from py_informationalstream_graph.informationalstream_graph import (
    ComplexPlane,
    GraphNode,
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy,
    InformationalStreamNetting,
    InformationalStreamVectorDirection,
    StreamApplicationLayer,
    StreamApplicationType,
    infer_neighbors,
)


@dataclass(frozen=True)
class ScreenProperties:
    """UI-like window that mirrors the Java presenter."""

    width: float
    height: float
    scale: float = 1.0

    def origin(self) -> ComplexPlane:
        return ComplexPlane(0.0, 0.0)

    def bottom_right(self) -> ComplexPlane:
        return ComplexPlane(self.width / 2.0, -self.height / 2.0)


@dataclass(frozen=True, order=True)
class PresenterNodeKey:
    step: int
    netting: InformationalStreamNetting
    node_id: int


@dataclass
class PresenterNode:
    key: PresenterNodeKey
    position: ComplexPlane
    app_type: StreamApplicationType
    layer: StreamApplicationLayer
    vector_direction: InformationalStreamVectorDirection
    neighbors: List[PresenterNodeKey]


@dataclass
class GraphComposition:
    nodes: Dict[PresenterNodeKey, PresenterNode]
    steps: List[int]
    screen: ScreenProperties

    def counts_by_netting(self) -> Dict[InformationalStreamNetting, int]:
        counts: Dict[InformationalStreamNetting, int] = {n: 0 for n in InformationalStreamNetting}
        for node in self.nodes.values():
            counts[node.key.netting] += 1
        return counts

    def counts_by_step(self) -> Dict[int, int]:
        counts: Dict[int, int] = {}
        for node in self.nodes.values():
            counts[node.key.step] = counts.get(node.key.step, 0) + 1
        return counts


class InformationalStreamGraphPresenterPy:
    """Compose the four netting graphs across multiple steps.

    Mirrors the Java InformationalStreamGraphPresenter: it loops steps, invokes
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilder.getNettingGraphs,
    and merges the results into a single composition ready for rendering.
    """

    def __init__(self, screen: ScreenProperties, stream_distance: float = 600.0):
        self.screen = screen
        self.stream_distance = stream_distance
        self.builder = InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy()

    def _collect_neighbors(
        self, graph: Dict[int, GraphNode], step: int
    ) -> Dict[int, List[PresenterNodeKey]]:
        out: Dict[int, List[PresenterNodeKey]] = {}
        for node_id, node in graph.items():
            neigh_keys: List[PresenterNodeKey] = []
            for nb in node.neighbors:
                if nb in graph:
                    neigh_keys.append(PresenterNodeKey(step=step, netting=node.netting, node_id=nb))
            out[node_id] = neigh_keys
        return out

    def compose(
        self,
        steps: Iterable[int] = (1, 2, 3, 4),
        infer_topology: bool = True,
    ) -> GraphComposition:
        origin = self.screen.origin()
        bottom_right = self.screen.bottom_right()

        nodes: Dict[PresenterNodeKey, PresenterNode] = {}
        steps_materialized: List[int] = []

        for step in steps:
            steps_materialized.append(step)
            netting_graphs = self.builder.get_netting_graphs(
                self.stream_distance,
                step,
                self.screen.scale,
                origin,
                bottom_right,
            )

            for graph in netting_graphs:
                if infer_topology:
                    infer_neighbors(graph)
                neighbor_map = self._collect_neighbors(graph, step)
                for node_id, node in graph.items():
                    key = PresenterNodeKey(step=step, netting=node.netting, node_id=node_id)
                    nodes[key] = PresenterNode(
                        key=key,
                        position=node.position,
                        app_type=node.app_type,
                        layer=node.layer,
                        vector_direction=node.vector_direction,
                        neighbors=neighbor_map.get(node_id, []),
                    )

        return GraphComposition(nodes=nodes, steps=steps_materialized, screen=self.screen)
