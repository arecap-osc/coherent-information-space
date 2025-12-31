
"""Infinite Coherent Grid Model powered by the InformationalStreamDoubleRangeIntegerIdentityGraphBuilder port."""
from typing import Dict, List, Optional, Tuple

from py_informationalstream_graph.informationalstream_graph import (
    ComplexPlane,
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy,
    InformationalStreamNetting as BuilderNetting,
    InformationalStreamVectorDirection as BuilderVectorDirection,
    GraphRoot,
    StreamApplicationLayer as BuilderLayer,
    StreamApplicationType as BuilderAppType,
)

from coherent_space_py.model.enums import (
    InformationalStreamNetting,
    InformationalStreamVectorDirection,
    InformationalStreamNeuronType,
)
from coherent_space_py.model.node import Node
from coherent_space_py.model.topology_rules import (
    get_downstream_connections,
    get_upstream_connections,
)


class InfiniteCoherentGraph:
    """
    Virtual Graph that computes nodes using the Python port of
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilder and then
    post-processes them with the hexavalent connection rules.
    """

    def __init__(
        self,
        stream_distance: float = 1.0,
        scale: float = 1.0,
        namespace: str = "org.arecap.eden.ia.console.informationalstream.api",
    ):
        self.stream_distance = stream_distance
        self.scale = scale
        self.namespace = namespace
        self.builder = InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy()

    def _max_steps(self, q_range: range, r_range: range) -> Tuple[int, int]:
        max_q = max(abs(q_range.start), abs(q_range.stop - 1))
        max_r = max(abs(r_range.start), abs(r_range.stop - 1))
        return max_q, max_r

    def _netting_display_window(
        self, netting: BuilderNetting, q_range: range, r_range: range
    ) -> ComplexPlane:
        """
        Compute the display window for a specific netting so that the Java-port
        builder samples the requested q/r span. The builder increments the
        sampling grid using its scroll (width/height) per netting, so the window
        must be sized from that scroll, not from an arbitrary extent.
        """
        builder = next(b for b in self.builder.builders if b.accept(netting))
        graph_root = GraphRoot(
            stream_distance=self.stream_distance, step=0, scale=self.scale, netting=netting
        )
        scroll = builder.get_scroll(graph_root)

        max_q, max_r = self._max_steps(q_range, r_range)
        padding = 1
        extent_real = (max_q + padding) * abs(scroll.real)
        extent_imag = (max_r + padding) * abs(scroll.imaginary)
        # Java builder expects bottom-right imaginary negative.
        return ComplexPlane(extent_real, -extent_imag)

    def _map_app_type(
        self, netting: BuilderNetting, app_type: BuilderAppType, layer: BuilderLayer
    ) -> InformationalStreamNeuronType:
        upstream = netting in (BuilderNetting.UpstreamEdge, BuilderNetting.UpstreamVertex)
        if app_type == BuilderAppType.selector and layer == BuilderLayer.function:
            return (
                InformationalStreamNeuronType.UpstreamSelectorFunction
                if upstream
                else InformationalStreamNeuronType.DownstreamSelectorFunction
            )
        if app_type == BuilderAppType.selector and layer == BuilderLayer.system:
            return (
                InformationalStreamNeuronType.UpstreamSelectorSystem
                if upstream
                else InformationalStreamNeuronType.DownstreamSelectorSystem
            )
        if app_type == BuilderAppType.detector and layer == BuilderLayer.function:
            return (
                InformationalStreamNeuronType.UpstreamDetectorFunction
                if upstream
                else InformationalStreamNeuronType.DownstreamDetectorFunction
            )
        if app_type == BuilderAppType.detector and layer == BuilderLayer.system:
            return (
                InformationalStreamNeuronType.UpstreamDetectorSystem
                if upstream
                else InformationalStreamNeuronType.DownstreamDetectorSystem
            )
        if app_type == BuilderAppType.consumer and layer == BuilderLayer.function:
            return (
                InformationalStreamNeuronType.UpstreamConsumerFunction
                if upstream
                else InformationalStreamNeuronType.DownstreamConsumerFunction
            )
        if app_type == BuilderAppType.consumer and layer == BuilderLayer.system:
            return (
                InformationalStreamNeuronType.UpstreamConsumerSystem
                if upstream
                else InformationalStreamNeuronType.DownstreamConsumerSystem
            )
        raise ValueError(f"Unsupported application combination: {netting} {app_type} {layer}")

    def _map_vector_direction(
        self, vdir: BuilderVectorDirection
    ) -> InformationalStreamVectorDirection:
        return (
            InformationalStreamVectorDirection.CornerParity
            if vdir == BuilderVectorDirection.SelectorDetectorConsumer
            else InformationalStreamVectorDirection.SideParity
        )

    def _builder_steps(
        self, builder: object, root: object, origin_plane: ComplexPlane, position: ComplexPlane
    ) -> Tuple[int, int]:
        """
        Introspect the netting builder to recover its (real, imag) step counters
        for a given node position. The upstream-edge builder returns four values
        (steps + quotas); the others return two. We only need the counters to
        keep a deterministic axial-like coordinate per node and to clip the
        display patch to the requested q/r ranges.
        """
        raw_steps = builder._steps(root, origin_plane, position)  # type: ignore[attr-defined]
        if len(raw_steps) == 2:
            return raw_steps  # type: ignore[return-value]
        if len(raw_steps) == 4:
            real_steps, imag_steps, _, _ = raw_steps  # type: ignore[misc]
            return real_steps, imag_steps
        raise ValueError(f"Unexpected _steps output for {builder}: {raw_steps}")

    def _convert_nodes(
        self,
        graph_nodes: Dict[int, object],
        netting: BuilderNetting,
        offset: int,
        builder: object,
        root: GraphRoot,
        origin_plane: ComplexPlane,
        q_range: range,
        r_range: range,
    ) -> List[Node]:
        converted: List[Node] = []
        for original_id, gnode in graph_nodes.items():
            steps = self._builder_steps(builder, root, origin_plane, gnode.position)
            real_steps, imag_steps = steps
            if real_steps not in q_range or imag_steps not in r_range:
                continue

            stream_app_type = self._map_app_type(netting, gnode.app_type, gnode.layer)
            netting_enum = InformationalStreamNetting(netting.value)
            vector_direction = self._map_vector_direction(gnode.vector_direction)
            converted.append(
                Node(
                    id=offset + original_id,
                    position=complex(gnode.position.real, gnode.position.imaginary),
                    stream_application_type=stream_app_type,
                    netting=netting_enum,
                    vector_direction=vector_direction,
                    orientation=f"{netting.value}:{original_id}",
                    neighbors=[],
                    connections={},
                    namespace=self.namespace,
                    network_links={},
                    lattice_steps=(real_steps, imag_steps),
                )
            )
        return converted

    def get_patch(self, q_range: range, r_range: range, origin: complex = 0j) -> List[Node]:
        """
        Generates nodes for a specific patch of the infinite grid using the
        InformationalStreamDoubleRangeIntegerIdentityGraphBuilder port.

        Args:
            q_range: Range of 'real' steps (e.g. range(0, 5))
            r_range: Range of 'imaginary' steps (e.g. range(0, 5))
            origin: The complex origin for this patch.

        Returns:
            List of superimposed nodes.
        """
        origin_plane = ComplexPlane(origin.real, origin.imag)
        all_nodes: List[Node] = []
        nettings_in_order = [
            BuilderNetting.UpstreamEdge,
            BuilderNetting.DownstreamEdge,
            BuilderNetting.UpstreamVertex,
            BuilderNetting.DownstreamVertex,
        ]
        # Keep IDs unique across nettings by reserving a wide offset block for each graph.
        id_block = 1_000_000
        for idx, netting in enumerate(nettings_in_order):
            builder_impl = next(b for b in self.builder.builders if b.accept(netting))
            view = self._netting_display_window(netting, q_range, r_range)
            graph_root = GraphRoot(
                stream_distance=self.stream_distance, step=0, scale=self.scale, netting=netting
            )
            graph_nodes = self.builder.get_graph(
                netting=netting,
                stream_distance=self.stream_distance,
                step=0,
                scale=self.scale,
                origin=origin_plane,
                display_bottom_right=view,
            )
            all_nodes.extend(
                self._convert_nodes(
                    graph_nodes,
                    netting,
                    offset=idx * id_block,
                    builder=builder_impl,
                    root=graph_root,
                    origin_plane=origin_plane,
                    q_range=q_range,
                    r_range=r_range,
                )
            )

        self._attach_hexavalent_links(all_nodes)
        return all_nodes

    # --- Hexavalent / informational-stream aware helpers ---

    def _as_complex(self, position) -> complex:
        """Normalize a node position to complex arithmetic for distance checks."""
        if isinstance(position, complex):
            return position
        if isinstance(position, ComplexPlane):
            return complex(position.real, position.imaginary)
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

    def _group_by_type(self, nodes: List[Node]) -> Dict[InformationalStreamNeuronType, List[Node]]:
        grouped: Dict[InformationalStreamNeuronType, List[Node]] = {}
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
