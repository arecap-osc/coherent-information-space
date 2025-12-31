"""Python port of InformationalStreamDoubleRangeIntegerIdentityGraphBuilder.

- Generates the (theoretically infinite) informational-stream netting graphs by sampling a view window.
- Ported from the Java implementation found in informationalstream/support/graph/builder/*

This module focuses on:
  * deterministic ID + position generation (same math as Java)
  * producing nodes for all 6 stream-application points (selector/detector/consumer × function/system)
  * optional neighbor inference (geometric)

Usage example is in render_demo.py
"""

from __future__ import annotations

from dataclasses import dataclass, field
from enum import Enum
from math import cos, sin, pi, sqrt
from typing import Dict, Iterable, List, Optional, Tuple


class InformationalStreamNetting(str, Enum):
    UpstreamEdge = "UpstreamEdge"
    DownstreamEdge = "DownstreamEdge"
    UpstreamVertex = "UpstreamVertex"
    DownstreamVertex = "DownstreamVertex"


class InformationalStreamVectorDirection(str, Enum):
    SelectorDetectorConsumer = "SelectorDetectorConsumer"
    ConsumerDetectorSelector = "ConsumerDetectorSelector"


class StreamApplicationType(str, Enum):
    selector = "selector"
    detector = "detector"
    consumer = "consumer"


class StreamApplicationLayer(str, Enum):
    function = "function"
    system = "system"


@dataclass
class ComplexPlane:
    real: float
    imaginary: float


def _int_value_java(d: float) -> int:
    """Java Double.intValue() truncates toward 0."""
    return int(d)


class RootsOfUnity:
    @staticmethod
    def r1(q: float) -> ComplexPlane:
        return ComplexPlane(q, 0.0)

    @staticmethod
    def r2(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(pi / 6), q * sin(pi / 6))

    @staticmethod
    def r3(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(pi / 3), q * sin(pi / 3))

    @staticmethod
    def r4(q: float) -> ComplexPlane:
        return ComplexPlane(0.0, q)

    @staticmethod
    def r5(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(2 * pi / 3), q * sin(2 * pi / 3))

    @staticmethod
    def r6(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(5 * pi / 6), q * sin(5 * pi / 6))

    @staticmethod
    def r7(q: float) -> ComplexPlane:
        return ComplexPlane(-q, 0.0)

    @staticmethod
    def r8(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(-5 * pi / 6), q * sin(-5 * pi / 6))

    @staticmethod
    def r9(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(-2 * pi / 3), q * sin(-2 * pi / 3))

    @staticmethod
    def r10(q: float) -> ComplexPlane:
        return ComplexPlane(0.0, -q)

    @staticmethod
    def r11(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(-pi / 3), q * sin(-pi / 3))

    @staticmethod
    def r12(q: float) -> ComplexPlane:
        return ComplexPlane(q * cos(-pi / 6), q * sin(-pi / 6))


def get_n_gauss_sum(n: int, gauss_sum_n: int) -> int:
    return n * gauss_sum_n * (gauss_sum_n + 1) // 2


@dataclass
class GraphRoot:
    stream_distance: float
    step: int
    scale: float
    netting: InformationalStreamNetting

    origin_vector_direction: InformationalStreamVectorDirection = (
        InformationalStreamVectorDirection.SelectorDetectorConsumer
    )

    def step_distance(self) -> float:
        if self.netting in (InformationalStreamNetting.UpstreamEdge, InformationalStreamNetting.UpstreamVertex):
            return self.stream_distance if self.step == 0 else self.stream_distance / (3 ** self.step)
        # downstream
        return (
            self.stream_distance
            if self.step == 0
            else (1 / sqrt(3)) * self.stream_distance / (3 ** self.step)
        )

    def width(self) -> float:
        if self.netting in (InformationalStreamNetting.UpstreamEdge, InformationalStreamNetting.UpstreamVertex):
            return self.step_distance()
        # downstream
        return self.height() * 2.0 / sqrt(3)

    def height(self) -> float:
        if self.netting in (InformationalStreamNetting.UpstreamEdge, InformationalStreamNetting.UpstreamVertex):
            return self.width() * 2.0 / sqrt(3)
        # downstream
        return self.step_distance()


@dataclass
class GraphNode:
    id: int
    netting: InformationalStreamNetting
    position: ComplexPlane
    app_type: StreamApplicationType
    layer: StreamApplicationLayer
    vector_direction: InformationalStreamVectorDirection

    # inferred neighbors (IDs) – optional
    neighbors: List[int] = field(default_factory=list)


class NettingGraphBuilder:
    """Port of InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder."""

    def accept(self, netting: InformationalStreamNetting) -> bool:
        raise NotImplementedError

    def get_scroll(self, root: GraphRoot) -> ComplexPlane:
        raise NotImplementedError

    # --- per-app-node calculators (ported from Java)

    def selector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    def selector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    def detector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    def detector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    def consumer_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    def consumer_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Optional[Tuple[int, ComplexPlane, InformationalStreamVectorDirection]]:
        raise NotImplementedError

    # --- shared window sampling

    def build_graph(self, stream_distance: float, step: int, scale: float, origin: ComplexPlane, display_bottom_right: ComplexPlane) -> Dict[int, GraphNode]:
        root = GraphRoot(stream_distance=stream_distance, step=step, scale=scale, netting=self.netting)

        display_upper_left = ComplexPlane(-display_bottom_right.real, -display_bottom_right.imaginary)
        p = ComplexPlane(display_upper_left.real, display_bottom_right.imaginary)
        scroll = self.get_scroll(root)

        out: Dict[int, GraphNode] = {}
        while p.imaginary <= display_upper_left.imaginary:
            while p.real <= display_bottom_right.real:
                for fn in (
                    (self.selector_function, StreamApplicationType.selector, StreamApplicationLayer.function),
                    (self.selector_system, StreamApplicationType.selector, StreamApplicationLayer.system),
                    (self.detector_function, StreamApplicationType.detector, StreamApplicationLayer.function),
                    (self.detector_system, StreamApplicationType.detector, StreamApplicationLayer.system),
                    (self.consumer_function, StreamApplicationType.consumer, StreamApplicationLayer.function),
                    (self.consumer_system, StreamApplicationType.consumer, StreamApplicationLayer.system),
                ):
                    calc, app_type, layer = fn
                    res = calc(root, origin, p)
                    if res is not None:
                        node_id, pos, vdir = res
                        out[node_id] = GraphNode(
                            id=node_id,
                            netting=root.netting,
                            position=pos,
                            app_type=app_type,
                            layer=layer,
                            vector_direction=vdir,
                        )
                p.real += scroll.real

            p.real = display_upper_left.real
            p.imaginary += scroll.imaginary

        return out


# ------------------------------
# Netting-specific builders
# ------------------------------


class UpstreamEdgeBuilder(NettingGraphBuilder):
    netting = InformationalStreamNetting.UpstreamEdge

    def accept(self, netting: InformationalStreamNetting) -> bool:
        return netting == self.netting

    def get_scroll(self, root: GraphRoot) -> ComplexPlane:
        return ComplexPlane(0.5 * root.width() * root.scale, 0.5 * root.height() * root.scale)

    def _steps(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Tuple[int, int, float, float]:
        real_quota = root.width() * root.scale
        real_steps = _int_value_java((p.real - origin.real) / real_quota)
        if p.real < origin.real:
            real_steps -= 1
        imag_quota = (3.0 / 4.0) * root.height() * root.scale
        imag_steps = _int_value_java((p.imaginary - origin.imaginary) / imag_quota)
        if p.imaginary < origin.imaginary:
            imag_steps -= 1
        return real_steps, imag_steps, real_quota, imag_quota

    def selector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, real_quota, _ = self._steps(root, origin, p)
        real_gauss_sum = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
        root11_imag = RootsOfUnity.r11(root.width() * root.scale * imag_steps)
        root1_real = RootsOfUnity.r1(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = real_gauss_sum + real_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps - imag_steps + 2
            if imag_steps % 2 == 0:
                parity = (real_steps + imag_steps + 1) % 2
            else:
                parity = (real_steps + imag_steps) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root11_imag.real + root1_imag.real, root11_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, _, _ = self._steps(root, origin, p)
        imag_steps = imag_steps * -1
        real_gauss_sum = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
        root3_imag = RootsOfUnity.r3(root.width() * root.scale * imag_steps)
        root1_real = RootsOfUnity.r1(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = real_gauss_sum + real_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps + imag_steps + 2
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root3_imag.real + root1_imag.real, root3_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, _, _ = self._steps(root, origin, p)
        root5_imag = RootsOfUnity.r5(root.width() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps))
            node_id = (
                get_n_gauss_sum(6, abs(imag_steps))
                + 2 * imag_steps
                - (abs(imag_steps) - abs(real_steps))
                - (abs(imag_steps) - 1)
            )
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root1_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def selector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, _, _ = self._steps(root, origin, p)
        real_steps *= -1
        imag_steps *= -1
        real_gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        root5_imag = RootsOfUnity.r5(root.width() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = real_gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.width() * root.scale * max(0, real_steps - imag_steps))
            node_id = real_gauss_sum - 2 * (real_steps - 1) - imag_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, _, _ = self._steps(root, origin, p)
        imag_steps *= -1
        real_gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        root9_imag = RootsOfUnity.r9(root.width() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = real_gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            imag_gauss_sum = get_n_gauss_sum(6, real_steps + 1) - 1
            node_id = imag_gauss_sum - 2 * real_steps + imag_steps
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps, _, _ = self._steps(root, origin, p)
        imag_steps *= -1
        root9_imag = RootsOfUnity.r9(root.width() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps))
            imag_gauss_sum = get_n_gauss_sum(6, abs(imag_steps))
            node_id = imag_gauss_sum + real_steps + 1
            parity = (abs(real_steps) + abs(imag_steps)) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root1_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None


# NOTE:
# The remaining 3 builders are lengthy but mostly mechanical ports.
# They are included below.


class UpstreamVertexBuilder(NettingGraphBuilder):
    netting = InformationalStreamNetting.UpstreamVertex

    def accept(self, netting: InformationalStreamNetting) -> bool:
        return netting == self.netting

    def get_scroll(self, root: GraphRoot) -> ComplexPlane:
        # matches Java: (width*scale, 3/4*height*scale)
        return ComplexPlane(root.width() * root.scale, (3.0 / 4.0) * root.height() * root.scale)

    def _steps(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Tuple[int, int]:
        real_quota = root.width() * root.scale
        real_steps = _int_value_java((p.real - origin.real) / real_quota)
        if p.real < origin.real:
            real_steps -= 1
        imag_quota = (3.0 / 4.0) * root.height() * root.scale
        imag_steps = _int_value_java((p.imaginary - origin.imaginary) / imag_quota)
        if p.imaginary < origin.imaginary:
            imag_steps -= 1
        return real_steps, imag_steps

    def selector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root11_imag = RootsOfUnity.r11(root.width() * root.scale * imag_steps)
        root1_real = RootsOfUnity.r1(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, real_steps) + 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps - imag_steps + 2
            parity = (real_steps + imag_steps) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root11_imag.real + root1_imag.real, root11_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        imag_steps *= -1
        root3_imag = RootsOfUnity.r3(root.width() * root.scale * imag_steps)
        root1_real = RootsOfUnity.r1(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, real_steps) + 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps + imag_steps + 2
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root3_imag.real + root1_imag.real, root3_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root5_imag = RootsOfUnity.r5(root.width() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, abs(imag_steps)) + real_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root1_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def selector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        real_steps *= -1
        imag_steps *= -1
        root5_imag = RootsOfUnity.r5(root.width() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, real_steps) - 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.width() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, real_steps) - imag_steps - 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        imag_steps *= -1
        root9_imag = RootsOfUnity.r9(root.width() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.width() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and real_steps == imag_steps:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, real_steps) - 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.width() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) - 2 * real_steps + imag_steps - 1
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        imag_steps *= -1
        root9_imag = RootsOfUnity.r9(root.width() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.width() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, abs(imag_steps)) + real_steps + 1
            parity = (abs(real_steps) + abs(imag_steps)) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root1_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None


class DownstreamEdgeBuilder(NettingGraphBuilder):
    netting = InformationalStreamNetting.DownstreamEdge

    def accept(self, netting: InformationalStreamNetting) -> bool:
        return netting == self.netting

    def get_scroll(self, root: GraphRoot) -> ComplexPlane:
        return ComplexPlane(0.5 * root.width() * root.scale, 0.5 * root.height() * root.scale)

    def _steps(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Tuple[int, int]:
        real_quota = (3.0 / 4.0) * root.width() * root.scale
        real_steps = _int_value_java((p.real - origin.real) / real_quota)
        if p.real < origin.real:
            real_steps -= 1
        imag_quota = root.height() * root.scale
        imag_steps = _int_value_java((p.imaginary - origin.imaginary) / imag_quota)
        if p.imaginary < origin.imaginary:
            imag_steps -= 1
        return real_steps, imag_steps

    # Ported from DownstreamEdgeDoubleRangeIntegerIdentityGraphBuilder.java
    def selector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        real_steps *= -1
        gauss_sum = get_n_gauss_sum(6, max(0, abs(real_steps) - 1)) + 1
        root7_real = RootsOfUnity.r7(root.height() * root.scale * abs(real_steps))
        root9_imag = RootsOfUnity.r9(root.height() * root.scale * imag_steps)
        if real_steps > 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum + abs(real_steps)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if abs(real_steps) + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, abs(real_steps) - imag_steps + 1))
            node_id = get_n_gauss_sum(6, abs(real_steps)) + abs(real_steps) - imag_steps + 2
            parity = (abs(real_steps) + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root7_real.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def selector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        gauss_sum = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
        root1_real = RootsOfUnity.r1(root.height() * root.scale * real_steps)
        root3_imag = RootsOfUnity.r3(root.height() * root.scale * imag_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum + real_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.height() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps - imag_steps + 2
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root1_real.real + root1_imag.real, root3_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root5_imag = RootsOfUnity.r5(root.height() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, abs(imag_steps)) + real_steps + 1
            parity = (abs(real_steps) + abs(imag_steps)) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root11_imag = RootsOfUnity.r11(root.height() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = (
                get_n_gauss_sum(6, abs(imag_steps))
                + 2 * imag_steps
                - (abs(imag_steps) - abs(real_steps))
                - (abs(imag_steps) - 1)
            )
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root11_imag.real + root1_imag.real, root11_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        root5_imag = RootsOfUnity.r5(root.height() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.height() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = gauss_sum - 2 * (real_steps - 1) - imag_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        root9_imag = RootsOfUnity.r9(root.height() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.height() * root.scale * real_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps + 1))
            imag_gauss_sum = get_n_gauss_sum(6, real_steps + 1) - 1
            node_id = imag_gauss_sum - 2 * real_steps + imag_steps
            parity = (real_steps + imag_steps + 1) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None


class DownstreamVertexBuilder(NettingGraphBuilder):
    netting = InformationalStreamNetting.DownstreamVertex

    def accept(self, netting: InformationalStreamNetting) -> bool:
        return netting == self.netting

    def get_scroll(self, root: GraphRoot) -> ComplexPlane:
        return ComplexPlane((3.0 / 4.0) * root.width() * root.scale, root.height() * root.scale)

    def _steps(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane) -> Tuple[int, int]:
        real_quota = (3.0 / 4.0) * root.width() * root.scale
        real_steps = _int_value_java((p.real - origin.real) / real_quota)
        if p.real < origin.real:
            real_steps -= 1
        imag_quota = root.height() * root.scale
        imag_steps = _int_value_java((p.imaginary - origin.imaginary) / imag_quota)
        if p.imaginary < origin.imaginary:
            imag_steps -= 1
        return real_steps, imag_steps

    # Ported from DownstreamVertexDoubleRangeIntegerIdentityGraphBuilder.java
    def selector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        real_steps *= -1
        root7_real = RootsOfUnity.r7(root.height() * root.scale * abs(real_steps))
        root9_imag = RootsOfUnity.r9(root.height() * root.scale * imag_steps)
        if real_steps > 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, abs(real_steps)) + 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if abs(real_steps) + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, abs(real_steps) - imag_steps + 1))
            node_id = get_n_gauss_sum(6, abs(real_steps)) + abs(real_steps) - imag_steps + 2
            parity = (abs(real_steps) + imag_steps) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root7_real.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def selector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root1_real = RootsOfUnity.r1(root.height() * root.scale * real_steps)
        root3_imag = RootsOfUnity.r3(root.height() * root.scale * imag_steps)
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = get_n_gauss_sum(6, real_steps) + 1
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root1_real.real, root1_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root1_imag = RootsOfUnity.r1(root.height() * root.scale * max(0, real_steps - imag_steps + 1))
            node_id = get_n_gauss_sum(6, real_steps) + real_steps - imag_steps + 2
            parity = (real_steps + imag_steps) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root1_real.real + root1_imag.real, root3_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root5_imag = RootsOfUnity.r5(root.height() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, abs(imag_steps)) + real_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (abs(real_steps) % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def detector_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root11_imag = RootsOfUnity.r11(root.height() * root.scale * abs(imag_steps))
        if imag_steps < 0 and real_steps >= imag_steps and real_steps < imag_steps + abs(imag_steps):
            root1_imag = RootsOfUnity.r1(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = get_n_gauss_sum(6, abs(imag_steps)) + real_steps + 1
            parity = (abs(real_steps) + abs(imag_steps)) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root11_imag.real + root1_imag.real, root11_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_function(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root5_imag = RootsOfUnity.r5(root.height() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.height() * root.scale * real_steps)
        gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps))
            node_id = gauss_sum - 2 * (real_steps - 1) - imag_steps
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root5_imag.real + root7_imag.real, root5_imag.imaginary)
            return (node_id, pos, vdir)
        return None

    def consumer_system(self, root: GraphRoot, origin: ComplexPlane, p: ComplexPlane):
        real_steps, imag_steps = self._steps(root, origin, p)
        root9_imag = RootsOfUnity.r9(root.height() * root.scale * imag_steps)
        root7_real = RootsOfUnity.r7(root.height() * root.scale * real_steps)
        gauss_sum = get_n_gauss_sum(6, max(0, real_steps)) - 1
        if real_steps < 0 or imag_steps < 0:
            return None
        if real_steps == 0 and imag_steps == 0:
            return (0, ComplexPlane(origin.real, origin.imaginary), InformationalStreamVectorDirection.SelectorDetectorConsumer)
        if imag_steps == 0:
            node_id = gauss_sum - 2 * (real_steps - 1)
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if (real_steps % 2 == 0) else InformationalStreamVectorDirection.ConsumerDetectorSelector
            return (node_id, ComplexPlane(root7_real.real, root7_real.imaginary), vdir)
        if real_steps + 1 >= imag_steps:
            root7_imag = RootsOfUnity.r7(root.height() * root.scale * max(0, real_steps - imag_steps + 1))
            imag_gauss_sum = get_n_gauss_sum(6, real_steps + 1) - 1
            node_id = imag_gauss_sum - 2 * real_steps + imag_steps
            parity = (real_steps + imag_steps) % 2
            vdir = InformationalStreamVectorDirection.SelectorDetectorConsumer if parity == 0 else InformationalStreamVectorDirection.ConsumerDetectorSelector
            pos = ComplexPlane(root9_imag.real + root7_imag.real, root9_imag.imaginary)
            return (node_id, pos, vdir)
        return None


# ------------------------------
# Top-level builder (equivalent to InformationalStreamDoubleRangeIntegerIdentityGraphBuilder)
# ------------------------------


class InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy:
    def __init__(self):
        self.builders: List[NettingGraphBuilder] = [
            UpstreamEdgeBuilder(),
            DownstreamEdgeBuilder(),
            UpstreamVertexBuilder(),
            DownstreamVertexBuilder(),
        ]

    def get_graph(
        self,
        netting: InformationalStreamNetting,
        stream_distance: float,
        step: int,
        scale: float,
        origin: ComplexPlane,
        display_bottom_right: ComplexPlane,
    ) -> Dict[int, GraphNode]:
        for b in self.builders:
            if b.accept(netting):
                return b.build_graph(stream_distance, step, scale, origin, display_bottom_right)
        raise ValueError(f"Unknown netting: {netting}")

    def get_netting_graphs(
        self,
        stream_distance: float,
        step: int,
        scale: float,
        origin: ComplexPlane,
        display_bottom_right: ComplexPlane,
    ) -> List[Dict[int, GraphNode]]:
        return [
            self.get_graph(InformationalStreamNetting.UpstreamEdge, stream_distance, step, scale, origin, display_bottom_right),
            self.get_graph(InformationalStreamNetting.DownstreamEdge, stream_distance, step, scale, origin, display_bottom_right),
            self.get_graph(InformationalStreamNetting.UpstreamVertex, stream_distance, step, scale, origin, display_bottom_right),
            self.get_graph(InformationalStreamNetting.DownstreamVertex, stream_distance, step, scale, origin, display_bottom_right),
        ]


def infer_neighbors(nodes: Dict[int, GraphNode], max_degree: int = 6) -> None:
    """Infer neighbors by geometric proximity.

    This is optional; the original Java builder focuses on deterministic placement.
    """
    ids = list(nodes.keys())
    pts = [(nodes[i].position.real, nodes[i].position.imaginary) for i in ids]
    # estimate spacing from median nearest distance
    # O(n^2) – fine for a few thousand nodes.
    import math

    dists: List[float] = []
    for a in range(min(200, len(ids))):
        ax, ay = pts[a]
        best = None
        for b in range(len(ids)):
            if a == b:
                continue
            bx, by = pts[b]
            d = (ax - bx) ** 2 + (ay - by) ** 2
            if best is None or d < best:
                best = d
        if best is not None:
            dists.append(math.sqrt(best))
    if not dists:
        return
    dists.sort()
    spacing = dists[len(dists) // 2]
    thr = spacing * 1.15

    for i_idx, i in enumerate(ids):
        ix, iy = pts[i_idx]
        neigh: List[Tuple[float, int]] = []
        for j_idx, j in enumerate(ids):
            if i == j:
                continue
            jx, jy = pts[j_idx]
            d = math.hypot(ix - jx, iy - jy)
            if d <= thr:
                neigh.append((d, j))
        neigh.sort(key=lambda t: t[0])
        nodes[i].neighbors = [j for _, j in neigh[:max_degree]]
