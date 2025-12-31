"""
Minimal Python port (generation + visualization) of the "Informational Stream" nettings from the Java archive.

What it ports:
- 4 nettings: UpstreamVertex, UpstreamEdge, DownstreamVertex, DownstreamEdge
- step dimension logic (stepDistance / width / height) exactly as in Java interfaces
- point-of-view (POV1..POV6) placement + ID generation exactly as in the 4 Java *GraphBuilder classes
- node positions inside each hex (12 app-nodes) exactly as in the 12 *DoubleGraph Java classes
- vector connectivity inside each hex (upstream/downstream lists) exactly as in Upstream/Downstream*GraphBean Java interfaces

What it intentionally does NOT port:
- the full UI stack / layers / rendering engine from the Java app
- any inter-hex linking beyond the netting placement (you can add it later once you decide the semantics)

Dependencies:
- matplotlib (for drawing)
"""

from __future__ import annotations
from dataclasses import dataclass, field
from enum import Enum
from typing import Dict, List, Optional, Tuple, Iterable
import math


# ---------------------------
# Core primitives (ComplexPlane, RootsOfUnity, gauss sum)
# ---------------------------

@dataclass
class ComplexPlane:
    real: float = 0.0
    imaginary: float = 0.0

    def __add__(self, other: "ComplexPlane") -> "ComplexPlane":
        return ComplexPlane(self.real + other.real, self.imaginary + other.imaginary)


class InformationalStreamVectorDirection(Enum):
    CornerParity = 0
    EdgeParity = 1


class InformationalStreamNetting(Enum):
    UpstreamVertex = "UpstreamVertex"
    UpstreamEdge = "UpstreamEdge"
    DownstreamVertex = "DownstreamVertex"
    DownstreamEdge = "DownstreamEdge"


def get_n_gauss_sum(six: int, n: int) -> int:
    """
    Port of InformationalStreamUtils.getNGaussSum(6, n).
    Java:
        public static Integer getNGaussSum(Integer p, Integer n) {
            if(n <= 0) return 0;
            Integer sum = 0;
            for (int i = 1; i <= n; i++) sum += (p * i);
            return sum;
        }
    """
    if n <= 0:
        return 0
    return sum(six * i for i in range(1, n + 1))


def root_of_12(k: int, quota: float) -> ComplexPlane:
    """
    RootsOfUnity.get{k}RootOf12(quota) from Java.
    Java angle: 2*pi * (k-1)/12, but note their helper methods map 1..12.
    """
    theta = 2.0 * math.pi * (k - 1) / 12.0
    return ComplexPlane(quota * math.cos(theta), quota * math.sin(theta))


# ---------------------------
# Graph bean (hex) + app nodes inside
# ---------------------------

# Node keys (stable identifiers)
U_SF = "u(Sf)"  # Upstream Selector Function
U_SS = "u(Ss)"  # Upstream Selector System
U_DF = "u(Df)"  # Upstream Detector Function
U_DS = "u(Ds)"  # Upstream Detector System
U_CF = "u(Cf)"  # Upstream Consumer Function
U_CS = "u(Cs)"  # Upstream Consumer System

D_SF = "d(Sf)"  # Downstream Selector Function
D_SS = "d(Ss)"  # Downstream Selector System
D_DF = "d(Df)"  # Downstream Detector Function
D_DS = "d(Ds)"  # Downstream Detector System
D_CF = "d(Cf)"  # Downstream Consumer Function
D_CS = "d(Cs)"  # Downstream Consumer System

UPSTREAM_NODES = [U_SF, U_CS, U_DF, U_SS, U_CF, U_DS]
DOWNSTREAM_NODES = [D_SF, D_CS, D_DF, D_SS, D_CF, D_DS]


@dataclass
class ISGraph:
    """Equivalent of InformationalStreamDoubleRangeIntegerIdentityGraphBean"""
    netting: InformationalStreamNetting
    stream_distance: float
    step: int
    scale: float

    id: int = 0
    vector_direction: InformationalStreamVectorDirection = InformationalStreamVectorDirection.CornerParity
    origin_vector_direction: InformationalStreamVectorDirection = InformationalStreamVectorDirection.CornerParity
    pos: ComplexPlane = field(default_factory=ComplexPlane)

    def is_upstream_netting(self) -> bool:
        return "Upstream" in self.netting.value

    # --- step distance / width / height as in UpstreamDoubleRangeIntegerIdentityGraphBean / Downstream... ---
    def step_distance(self) -> float:
        if self.is_upstream_netting():
            # Upstream: step==0 ? streamDistance : streamDistance / 3^step
            return self.stream_distance if self.step == 0 else self.stream_distance / (3 ** self.step)
        # Downstream: step==0 ? streamDistance : 1/sqrt(3) * streamDistance / 3^step
        return self.stream_distance if self.step == 0 else (1 / math.sqrt(3)) * self.stream_distance / (3 ** self.step)

    def width(self) -> float:
        if self.is_upstream_netting():
            return self.step_distance()
        # Downstream: width = height * 2 / sqrt(3)
        return self.height() * 2.0 / math.sqrt(3)

    def height(self) -> float:
        if self.is_upstream_netting():
            # Upstream: height = width * 2 / sqrt(3)
            return self.width() * 2.0 / math.sqrt(3)
        # Downstream: height = stepDistance
        return self.step_distance()

    # --- app node positions inside a hex (ports the 12 *DoubleGraph classes) ---
    def node_position(self, node_key: str) -> ComplexPlane:
        # The Java "isUpstream(root)" checks netting name contains "Upstream".
        is_up = self.is_upstream_netting()

        # Each app node uses a specific root index and a specific quota:
        # - if isUpstream(root): quota = (1/sqrt(3)) * stepDistance * scale (for upstream nodes)
        # - else: quota = (1/3) * stepDistance * scale  (for upstream nodes)
        # For downstream nodes the quotas are flipped in the code with TODO comments; we keep exactly their implementation:
        # - if isUpstream(root): quota = (1/3) * stepDistance * scale
        # - else: quota = (1/sqrt(3)) * stepDistance * scale
        sd = self.step_distance() * self.scale

        if node_key == U_SF:
            k = 10
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd
        elif node_key == U_SS:
            k = 4
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd
        elif node_key == U_DF:
            k = 2
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd
        elif node_key == U_DS:
            k = 8
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd
        elif node_key == U_CF:
            k = 6
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd
        elif node_key == U_CS:
            k = 12
            quota = (1 / math.sqrt(3)) * sd if is_up else (1 / 3) * sd

        elif node_key == D_SF:
            k = 7
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        elif node_key == D_SS:
            k = 1
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        elif node_key == D_DF:
            k = 11
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        elif node_key == D_DS:
            k = 5
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        elif node_key == D_CF:
            k = 3
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        elif node_key == D_CS:
            k = 9
            quota = (1 / 3) * sd if is_up else (1 / math.sqrt(3)) * sd
        else:
            raise KeyError(node_key)

        off = root_of_12(k, quota)
        return ComplexPlane(self.pos.real + off.real, self.pos.imaginary + off.imaginary)

    # --- vector connectivity inside a hex (ports UpstreamDoubleRangeIntegerIdentityGraphBean / Downstream...) ---
    def vectors(self) -> List[Tuple[str, str]]:
        """
        Returns directed edges between app nodes inside this hex.
        For Upstream nettings: edges follow the "*Upstream()" lists.
        For Downstream nettings: edges follow the "*Downstream()" lists.
        """
        edges: List[Tuple[str, str]] = []
        vd = self.vector_direction
        ovd = self.origin_vector_direction

        if self.is_upstream_netting():
            # Ported from UpstreamDoubleRangeIntegerIdentityGraphBean
            def sel_func_targets() -> List[str]:
                if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                    return [U_DS, U_CF]
                return [U_DS, U_CS]

            def sel_sys_targets() -> List[str]:
                if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                    return [U_DS, U_CS]
                return [U_DS, U_CF]

            def det_func_targets() -> List[str]:
                return [U_SF, U_SS]

            def det_sys_targets() -> List[str]:
                return [U_CF, U_CS]

            def con_func_targets() -> List[str]:
                if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                    return [U_SS, U_DF]
                return [U_SF, U_DF]

            def con_sys_targets() -> List[str]:
                if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                    return [U_SF, U_DF]
                return [U_SS, U_DF]

            sources = {
                U_SF: sel_func_targets(),
                U_SS: sel_sys_targets(),
                U_DF: det_func_targets(),
                U_DS: det_sys_targets(),
                U_CF: con_func_targets(),
                U_CS: con_sys_targets(),
            }
            for s, ts in sources.items():
                for t in ts:
                    edges.append((s, t))
            return edges

        # Downstream nettings: ported from DownstreamDoubleRangeIntegerIdentityGraphBean
        def ds_sel_func_targets() -> List[str]:
            if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                return []
            return [D_CF, D_CS]

        def ds_sel_sys_targets() -> List[str]:
            if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                return [D_DF, D_DS]
            return [D_DF, D_DS, D_CF, D_CS]

        def ds_det_func_targets() -> List[str]:
            return [D_SF, D_CF]

        def ds_det_sys_targets() -> List[str]:
            return [D_SF, D_CF]

        def ds_con_func_targets() -> List[str]:
            if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                return [D_SF, D_SS]
            return []

        def ds_con_sys_targets() -> List[str]:
            if ovd == InformationalStreamVectorDirection.CornerParity and vd == InformationalStreamVectorDirection.CornerParity:
                return [D_SF, D_SS, D_DF, D_DS]
            return [D_DF, D_DS]

        sources = {
            D_SF: ds_sel_func_targets(),
            D_SS: ds_sel_sys_targets(),
            D_DF: ds_det_func_targets(),
            D_DS: ds_det_sys_targets(),
            D_CF: ds_con_func_targets(),
            D_CS: ds_con_sys_targets(),
        }
        for s, ts in sources.items():
            for t in ts:
                edges.append((s, t))
        return edges


# ---------------------------
# Netting graph builders (ports the 4 Java *GraphBuilder classes)
# ---------------------------

@dataclass
class Viewport:
    """Equivalent of origin + displayBottomRight and the builder's implicit displayUpperLeft."""
    origin: ComplexPlane
    display_bottom_right: ComplexPlane

    @property
    def display_upper_left(self) -> ComplexPlane:
        # Java: new ComplexPlane(-displayBottomRight.real, -displayBottomRight.imaginary)
        return ComplexPlane(-self.display_bottom_right.real, -self.display_bottom_right.imaginary)


class BaseBuilder:
    netting: InformationalStreamNetting

    def get_step_dimension_graph(self, stream_distance: float, step: int, scale: float) -> ISGraph:
        return ISGraph(netting=self.netting, stream_distance=stream_distance, step=step, scale=scale)

    def get_scroll(self, root: ISGraph) -> ComplexPlane:
        raise NotImplementedError

    # POV methods must return Optional[ISGraph]
    def pov1(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...
    def pov2(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...
    def pov3(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...
    def pov4(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...
    def pov5(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...
    def pov6(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]: ...

    def get_graph(self, stream_distance: float, step: int, scale: float, origin: ComplexPlane, display_bottom_right: ComplexPlane) -> Dict[int, ISGraph]:
        root = self.get_step_dimension_graph(stream_distance, step, scale)
        vp = Viewport(origin=origin, display_bottom_right=display_bottom_right)

        graph: Dict[int, ISGraph] = {}
        display_ul = vp.display_upper_left
        graph_pos = ComplexPlane(display_ul.real, display_bottom_right.imaginary)

        scroll = self.get_scroll(root)

        while graph_pos.imaginary <= display_ul.imaginary:
            while graph_pos.real <= display_bottom_right.real:
                for fn in (self.pov1, self.pov2, self.pov3, self.pov4, self.pov5, self.pov6):
                    g = fn(root, vp, graph_pos)
                    if g is not None:
                        graph[g.id] = g
                graph_pos.real += scroll.real
            graph_pos.real = display_ul.real
            graph_pos.imaginary += scroll.imaginary

        return graph


# ---- UpstreamVertex builder ----
class UpstreamVertexBuilder(BaseBuilder):
    netting = InformationalStreamNetting.UpstreamVertex

    def get_scroll(self, root: ISGraph) -> ComplexPlane:
        return ComplexPlane(root.width() * root.scale, 0.5 * root.height() * root.scale)

    def _base_steps(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Tuple[int, int]:
        scroll = self.get_scroll(root)
        rq = scroll.real
        rs = int((graph_pos.real - vp.origin.real) / rq)
        if graph_pos.real < vp.origin.real:
            rs -= 1
        iq = scroll.imaginary
        ims = int((graph_pos.imaginary - vp.origin.imaginary) / iq)
        if graph_pos.imaginary < vp.origin.imaginary:
            ims -= 1
        return rs, ims

    def pov1(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root10i = root_of_12(10, r.height() * r.scale * ims)
        root12r = root_of_12(12, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss + rs
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root12r.real, root12r.imaginary)
            return r
        if rs + 1 >= ims:
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + rs - ims + 2
            idx = (rs + ims + 1) % 2 if (ims % 2 == 0) else (rs + ims) % 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if idx == 0 else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root10i.real + root12i.real, root10i.imaginary + root12i.imaginary)
            return r
        return None

    def pov6(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root2i = root_of_12(2, r.height() * r.scale * ims)
        root12r = root_of_12(12, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss + rs
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root12r.real, root12r.imaginary)
            return r
        if rs + 1 >= ims:
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + rs + ims + 2
            idx = (rs + ims + 1) % 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if idx == 0 else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root2i.real + root12i.real, root2i.imaginary + root12i.imaginary)
            return r
        return None

    def pov3(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        root4 = root_of_12(4, r.height() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims))
            r.id = get_n_gauss_sum(6, abs(ims)) + 2 * ims - (abs(ims) - abs(rs)) - (abs(ims) - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root4.real + root12i.real, root4.imaginary + root12i.imaginary)
            return r
        return None

    def pov2(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        rs *= -1
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs)) - 1
        root4i = root_of_12(4, r.height() * r.scale * ims)
        root6r = root_of_12(6, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 2 * (rs - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root6r.real, root6r.imaginary)
            return r
        if rs >= ims:
            root6i = root_of_12(6, r.height() * r.scale * max(0, rs - ims))
            r.id = real_gauss - 2 * (rs - 1) - ims
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root4i.real + root6i.real, root4i.imaginary + root6i.imaginary)
            return r
        return None

    def pov5(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs)) - 1
        root8i = root_of_12(8, r.height() * r.scale * ims)
        root6r = root_of_12(6, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 2 * (rs - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root6r.real, root6r.imaginary)
            return r
        if rs + 1 >= ims:
            root6i = root_of_12(6, r.height() * r.scale * max(0, rs - ims + 1))
            imag_gauss = get_n_gauss_sum(6, rs + 1) - 1
            r.id = imag_gauss - 2 * rs + ims
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((rs + ims + 1) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root8i.real + root6i.real, root8i.imaginary + root6i.imaginary)
            return r
        return None

    def pov4(self, root: ISGraph, vp: Viewport, graph_pos: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._base_steps(r, vp, graph_pos)
        ims *= -1
        root8 = root_of_12(8, r.height() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims))
            r.id = imag_gauss + rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((abs(rs) + abs(ims)) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root8.real + root12i.real, root8.imaginary + root12i.imaginary)
            return r
        return None


# ---- UpstreamEdge builder ----
class UpstreamEdgeBuilder(BaseBuilder):
    netting = InformationalStreamNetting.UpstreamEdge

    def get_scroll(self, root: ISGraph) -> ComplexPlane:
        return ComplexPlane(0.5 * root.width() * root.scale, 0.5 * root.height() * root.scale)

    def _steps(self, r: ISGraph, vp: Viewport, gp: ComplexPlane) -> Tuple[int, int]:
        real_quota = r.width() * r.scale
        rs = int((gp.real - vp.origin.real) / real_quota)
        if gp.real < vp.origin.real:
            rs -= 1
        imag_quota = 0.75 * r.height() * r.scale
        ims = int((gp.imaginary - vp.origin.imaginary) / imag_quota)
        if gp.imaginary < vp.origin.imaginary:
            ims -= 1
        return rs, ims

    def pov1(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root11 = root_of_12(11, r.width() * r.scale * ims)
        root1 = root_of_12(1, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss + rs
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root1.real, root1.imaginary)
            return r
        if rs + 1 >= ims:
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + rs - ims + 2
            idx = (rs + ims + 1) % 2 if (ims % 2 == 0) else (rs + ims) % 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if idx == 0 else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root11.real + root1i.real, root11.imaginary)
            return r
        return None

    def pov6(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root3 = root_of_12(3, r.width() * r.scale * ims)
        root1 = root_of_12(1, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss + rs
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root1.real, root1.imaginary)
            return r
        if rs + 1 >= ims:
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + rs + ims + 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((rs + ims + 1) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root3.real + root1i.real, root3.imaginary)
            return r
        return None

    def pov3(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        root5 = root_of_12(5, r.width() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims))
            r.id = get_n_gauss_sum(6, abs(ims)) + 2 * ims - (abs(ims) - abs(rs)) - (abs(ims) - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root5.real + root1i.real, root5.imaginary)
            return r
        return None

    def pov2(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        rs *= -1
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs)) - 1
        root5i = root_of_12(5, r.width() * r.scale * ims)
        root7r = root_of_12(7, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 2 * (rs - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root7r.real, root7r.imaginary)
            return r
        if rs >= ims:
            root7i = root_of_12(7, r.width() * r.scale * max(0, rs - ims))
            r.id = real_gauss - 2 * (rs - 1) - ims
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root5i.real + root7i.real, root5i.imaginary)
            return r
        return None

    def pov5(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs)) - 1
        root9i = root_of_12(9, r.width() * r.scale * ims)
        root7r = root_of_12(7, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 2 * (rs - 1)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root7r.real, root7r.imaginary)
            return r
        if rs + 1 >= ims:
            root7i = root_of_12(7, r.width() * r.scale * max(0, rs - ims + 1))
            imag_gauss = get_n_gauss_sum(6, rs + 1) - 1
            r.id = imag_gauss - 2 * rs + ims
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((rs + ims + 1) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root9i.real + root7i.real, root9i.imaginary)
            return r
        return None

    def pov4(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        root9 = root_of_12(9, r.width() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims))
            r.id = imag_gauss + rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((abs(rs) + abs(ims)) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root9.real + root1i.real, root9.imaginary)
            return r
        return None


# ---- DownstreamVertex builder ----
class DownstreamVertexBuilder(BaseBuilder):
    netting = InformationalStreamNetting.DownstreamVertex

    def get_scroll(self, root: ISGraph) -> ComplexPlane:
        return ComplexPlane(root.width() * root.scale * 0.75, root.height() * root.scale * 0.75)

    def _steps(self, r: ISGraph, vp: Viewport, gp: ComplexPlane) -> Tuple[int, int]:
        scroll = self.get_scroll(r)
        rq = scroll.real
        rs = int((gp.real - vp.origin.real) / rq)
        if gp.real < vp.origin.real:
            rs -= 1
        iq = scroll.imaginary
        ims = int((gp.imaginary - vp.origin.imaginary) / iq)
        if gp.imaginary < vp.origin.imaginary:
            ims -= 1
        return rs, ims

    def pov1(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root9i = root_of_12(9, r.width() * r.scale * ims)
        root7r = root_of_12(7, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root7r.real, root7r.imaginary)
            return r
        if rs + 1 >= ims:
            root7i = root_of_12(7, r.width() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + ims + 1
            idx = (rs + ims + 1) % 2 if (ims % 2 == 0) else (rs + ims) % 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if idx == 0 else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root9i.real + root7i.real, root9i.imaginary)
            return r
        return None

    def pov6(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        root9 = root_of_12(9, r.width() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root1 = root_of_12(1, r.width() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims) - 1)
            r.id = imag_gauss - 2 * ims + rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root9.real + root1.real, root9.imaginary)
            return r
        return None

    def pov3(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root11 = root_of_12(11, r.width() * r.scale * ims)
        root1 = root_of_12(1, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 3 * rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root1.real, root1.imaginary)
            return r
        if rs + 1 >= ims:
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, max(0, rs + 1)) - 3 * (rs + 1) - ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((rs + ims + 1) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root11.real + root1i.real, root11.imaginary)
            return r
        return None

    def pov2(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root3 = root_of_12(3, r.width() * r.scale * ims)
        root1 = root_of_12(1, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 3 * rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root1.real, root1.imaginary)
            return r
        if rs + 1 >= ims:
            root1i = root_of_12(1, r.width() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, max(0, rs + 1)) - 3 * (rs + 1) + ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (((rs + 1) % 2) == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root3.real + root1i.real, root3.imaginary)
            return r
        return None

    def pov5(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        root5 = root_of_12(5, r.width() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root1 = root_of_12(1, r.width() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims))
            r.id = imag_gauss + 2 * ims - rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs + ims) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root5.real + root1.real, root5.imaginary)
            return r
        return None

    def pov4(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        rs *= -1
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root5 = root_of_12(5, r.width() * r.scale * ims)
        root7 = root_of_12(7, r.width() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = get_n_gauss_sum(6, max(0, rs - 1)) + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root7.real, root7.imaginary)
            return r
        if rs >= ims:
            root7i = root_of_12(7, r.width() * r.scale * max(0, rs - ims))
            r.id = real_gauss - ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (((rs + ims) % 2) == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root5.real + root7i.real, root5.imaginary)
            return r
        return None


# ---- DownstreamEdge builder ----
class DownstreamEdgeBuilder(BaseBuilder):
    netting = InformationalStreamNetting.DownstreamEdge

    def get_scroll(self, root: ISGraph) -> ComplexPlane:
        return ComplexPlane(0.75 * root.width() * root.scale, 0.5 * root.height() * root.scale)

    def _steps(self, r: ISGraph, vp: Viewport, gp: ComplexPlane) -> Tuple[int, int]:
        scroll = self.get_scroll(r)
        rq = scroll.real
        rs = int((gp.real - vp.origin.real) / rq)
        if gp.real < vp.origin.real:
            rs -= 1
        iq = scroll.imaginary
        ims = int((gp.imaginary - vp.origin.imaginary) / iq)
        if gp.imaginary < vp.origin.imaginary:
            ims -= 1
        return rs, ims

    def pov1(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs - 1)) + 1
        root8 = root_of_12(8, r.height() * r.scale * ims)
        root6 = root_of_12(6, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root6.real, root6.imaginary)
            return r
        if rs + 1 >= ims:
            root6i = root_of_12(6, r.height() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, rs) + ims + 1
            idx = (rs + ims + 1) % 2 if (ims % 2 == 0) else (rs + ims) % 2
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if idx == 0 else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root8.real + root6i.real, root8.imaginary + root6i.imaginary)
            return r
        return None

    def pov6(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        root8 = root_of_12(8, r.height() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root12 = root_of_12(12, r.height() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims) - 1)
            r.id = imag_gauss - 2 * ims + rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root8.real + root12.real, root8.imaginary + root12.imaginary)
            return r
        return None

    def pov3(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root10 = root_of_12(10, r.height() * r.scale * ims)
        root12 = root_of_12(12, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 3 * rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root12.real, root12.imaginary)
            return r
        if rs + 1 >= ims:
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, max(0, rs + 1)) - 3 * (rs + 1) - ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if ((rs + ims + 1) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root10.real + root12i.real, root10.imaginary + root12i.imaginary)
            return r
        return None

    def pov2(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root2 = root_of_12(2, r.height() * r.scale * ims)
        root12 = root_of_12(12, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = real_gauss - 3 * rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root12.real, root12.imaginary)
            return r
        if rs + 1 >= ims:
            root12i = root_of_12(12, r.height() * r.scale * max(0, rs - ims + 1))
            r.id = get_n_gauss_sum(6, max(0, rs + 1)) - 3 * (rs + 1) + ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (((rs + 1) % 2) == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root2.real + root12i.real, root2.imaginary + root12i.imaginary)
            return r
        return None

    def pov5(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        root4 = root_of_12(4, r.height() * r.scale * abs(ims))
        if ims < 0 and rs >= ims and rs < ims + abs(ims):
            root12 = root_of_12(12, r.height() * r.scale * max(0, rs - ims))
            imag_gauss = get_n_gauss_sum(6, abs(ims))
            r.id = imag_gauss + 2 * ims - rs + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (abs(rs + ims) % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root4.real + root12.real, root4.imaginary + root12.imaginary)
            return r
        return None

    def pov4(self, root: ISGraph, vp: Viewport, gp: ComplexPlane) -> Optional[ISGraph]:
        r = ISGraph(self.netting, root.stream_distance, root.step, root.scale)
        rs, ims = self._steps(r, vp, gp)
        rs *= -1
        ims *= -1
        real_gauss = get_n_gauss_sum(6, max(0, rs))
        root4 = root_of_12(4, r.height() * r.scale * ims)
        root6 = root_of_12(6, r.height() * r.scale * rs)
        if rs < 0 or ims < 0:
            return None
        if rs == 0 and rs == ims:
            r.id = 0
            r.pos = ComplexPlane(vp.origin.real, vp.origin.imaginary)
            r.vector_direction = InformationalStreamVectorDirection.CornerParity
            return r
        if ims == 0:
            r.id = get_n_gauss_sum(6, max(0, rs - 1)) + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (rs % 2 == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root6.real, root6.imaginary)
            return r
        if rs >= ims:
            root6i = root_of_12(6, r.height() * r.scale * max(0, rs - ims))
            r.id = real_gauss - ims + 1
            r.vector_direction = InformationalStreamVectorDirection.CornerParity if (((rs + ims) % 2) == 0) else InformationalStreamVectorDirection.EdgeParity
            r.pos = ComplexPlane(root4.real + root6i.real, root4.imaginary + root6i.imaginary)
            return r
        return None


def build_all_nettings(stream_distance: float, step: int, scale: float, origin: ComplexPlane, display_bottom_right: ComplexPlane) -> Dict[int, ISGraph]:
    graphs: Dict[int, ISGraph] = {}
    for builder in (UpstreamVertexBuilder(), UpstreamEdgeBuilder(), DownstreamVertexBuilder(), DownstreamEdgeBuilder()):
        g = builder.get_graph(stream_distance, step, scale, origin, display_bottom_right)
        graphs.update(g)
    return graphs


# ---------------------------
# Visualization (matplotlib)
# ---------------------------

def plot_graph(graphs: Dict[int, ISGraph], show_internal_vectors: bool = True, show_labels: bool = False):
    import matplotlib.pyplot as plt

    # Plot centers
    xs = [g.pos.real for g in graphs.values()]
    ys = [g.pos.imaginary for g in graphs.values()]

    plt.figure()
    plt.scatter(xs, ys, s=10)

    if show_labels:
        for g in graphs.values():
            plt.text(g.pos.real, g.pos.imaginary, str(g.id), fontsize=6)

    if show_internal_vectors:
        for g in graphs.values():
            # choose the relevant node set based on netting type
            node_keys = UPSTREAM_NODES if g.is_upstream_netting() else DOWNSTREAM_NODES
            node_pos = {k: g.node_position(k) for k in node_keys}

            for a, b in g.vectors():
                if a not in node_pos or b not in node_pos:
                    continue
                pa, pb = node_pos[a], node_pos[b]
                plt.arrow(pa.real, pa.imaginary, pb.real - pa.real, pb.imaginary - pa.imaginary,
                          length_includes_head=True, head_width=0.5, head_length=1.0, linewidth=0.3)

            # optionally draw node dots
            plt.scatter([p.real for p in node_pos.values()], [p.imaginary for p in node_pos.values()], s=4)

    plt.gca().set_aspect("equal", adjustable="box")
    plt.title(f"Informational Stream nettings ({len(graphs)} hex graphs)")
    plt.show()
