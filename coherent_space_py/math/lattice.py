"""Lattice mathematics for Coherent Space."""
from abc import ABC, abstractmethod
from typing import Optional, List, Tuple
from dataclasses import dataclass
import math

from coherent_space_py.model.enums import (
    InformationalStreamVectorDirection, 
    InformationalStreamNetting,
    StreamApplicationType
)
from coherent_space_py.geometry.roots_of_unity import (
    get_1_root_of_12, get_2_root_of_12, get_3_root_of_12, 
    get_4_root_of_12, get_5_root_of_12, get_6_root_of_12,
    get_7_root_of_12, get_8_root_of_12, get_9_root_of_12,
    get_10_root_of_12, get_11_root_of_12, get_12_root_of_12
)
from coherent_space_py.math.gauss import get_n_gauss_sum
from coherent_space_py.model.node import Node

from coherent_space_py.model.topology_rules import get_upstream_connections, get_downstream_connections

@dataclass
class LatticeConfig:
    stream_distance: float
    step: int
    scale: float

class LatticeMath(ABC):
    """Abstract base class for Lattice mathematics."""
    
    def __init__(self, config: LatticeConfig):
        self.config = config
    
    @property
    def step_distance(self) -> float:
        if self.config.step == 0:
            return self.config.stream_distance
        return self.config.stream_distance / (3 ** self.config.step)
        
    @property
    def width(self) -> float:
        return self.step_distance
        
    @property
    def height(self) -> float:
        return self.width * 2 / math.sqrt(3)

    @abstractmethod
    def get_scroll(self) -> complex:
        """Returns the origin offset for this lattice."""
        pass

    @abstractmethod
    def get_nodes(self, q: int, r: int, origin: complex) -> List[Node]:
        """Returns valid nodes for the given lattice steps (q, r)."""
        pass

    def _create_node(self, nid: int, pos: complex, type_enum: StreamApplicationType, vd_idx: int) -> Node:
        vd = InformationalStreamVectorDirection.CornerParity if vd_idx == 0 else InformationalStreamVectorDirection.SideParity
        
        # Determine netting dynamically based on type name
        netting = None
        name = type_enum.name
        is_upstream = "Upstream" in name
        
        if "UpstreamEdge" in self.__class__.__name__: netting = InformationalStreamNetting.UpstreamEdge
        elif "DownstreamEdge" in self.__class__.__name__: netting = InformationalStreamNetting.DownstreamEdge
        elif "UpstreamVertex" in self.__class__.__name__: netting = InformationalStreamNetting.UpstreamVertex
        elif "DownstreamVertex" in self.__class__.__name__: netting = InformationalStreamNetting.DownstreamVertex
        else:
             # Fallback
             netting = InformationalStreamNetting.UpstreamEdge if is_upstream else InformationalStreamNetting.DownstreamEdge

        # --- Populate Connections based on Topology Rules ---
        conns_map = {}
        if is_upstream:
             target_types = get_upstream_connections(type_enum, vd)
        else:
             target_types = get_downstream_connections(type_enum, vd)
             
        # Map target type name (e.g. "UpstreamDetectorSystem") to empty list of IDs (to be filled later? or just indicate 'Road exists')
        # Since we don't know the Neighbor IDs yet without complex search, we just store the INTENT (The "Road")
        # The Viz will use this intent to find the nearest neighbor of that type.
        for t in target_types:
            conns_map[t.name] = [] 

        return Node(
            id=nid,
            position=pos,
            stream_application_type=type_enum,
            netting=netting,
            vector_direction=vd,
            orientation=f"id:{nid}",
            neighbors=[],
            connections=conns_map,
        )

class UpstreamEdgeLattice(LatticeMath):
    """Mathematics for the Upstream Edge Lattice."""
    
    def get_scroll(self) -> complex:
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        return complex(0.5 * w, 0.5 * h)

    def get_nodes(self, q: int, r: int, origin: complex) -> List[Node]:
        nodes = []
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        
        real_steps = q
        imaginary_steps = r
        
        # --- 1. Upstream Selector Function ---
        node_sf = None
        if real_steps == 0 and real_steps == imaginary_steps:
             node_sf = self._create_node(0, origin, StreamApplicationType.UpstreamSelectorFunction, 0)
        elif imaginary_steps == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root1_real = get_1_root_of_12(w * real_steps)
             pos = origin + root1_real
             node_sf = self._create_node(real_gauss + real_steps, pos, StreamApplicationType.UpstreamSelectorFunction, real_steps % 2)
        elif real_steps + 1 >= imaginary_steps:
             real_gauss = get_n_gauss_sum(6, real_steps)
             root11_imag = get_11_root_of_12(w * imaginary_steps)
             root1_imag = get_1_root_of_12(w * max(0, real_steps - imaginary_steps + 1))
             nid = real_gauss + real_steps - imaginary_steps + 2
             vd_idx = (real_steps + imaginary_steps + 1) % 2 if imaginary_steps % 2 == 0 else (real_steps + imaginary_steps) % 2
             pos = origin + complex(root11_imag.real + root1_imag.real, root11_imag.imag + root1_imag.imag)
             node_sf = self._create_node(nid, pos, StreamApplicationType.UpstreamSelectorFunction, vd_idx)
        
        if node_sf: nodes.append(node_sf)
        
        # --- 2. Upstream Consumer System ---
        node_cs = None
        r_cs = r * -1
        if real_steps == 0 and real_steps == r_cs:
             node_cs = self._create_node(0, origin, StreamApplicationType.UpstreamConsumerSystem, 0)
        elif r_cs == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root1_real = get_1_root_of_12(w * real_steps)
             pos = origin + root1_real
             node_cs = self._create_node(real_gauss + real_steps, pos, StreamApplicationType.UpstreamConsumerSystem, real_steps % 2)
        elif real_steps + 1 >= r_cs and r_cs >= 0:
             root3_imag = get_3_root_of_12(w * r_cs)
             root1_imag = get_1_root_of_12(w * max(0, real_steps - r_cs + 1))
             real_gauss = get_n_gauss_sum(6, real_steps)
             nid = real_gauss + real_steps + r_cs + 2
             vd_idx = (real_steps + r_cs + 1) % 2
             pos = origin + complex(root3_imag.real + root1_imag.real, root3_imag.imag + root1_imag.imag)
             node_cs = self._create_node(nid, pos, StreamApplicationType.UpstreamConsumerSystem, vd_idx)

        if node_cs: nodes.append(node_cs)

        # --- 3. Upstream Detector Function ---
        node_df = None
        if real_steps == 0 and real_steps == imaginary_steps:
             node_df = self._create_node(0, origin, StreamApplicationType.UpstreamDetectorFunction, 0)
        elif imaginary_steps == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root1_real = get_1_root_of_12(w * real_steps)
             pos = origin + root1_real
             node_df = self._create_node(real_gauss + real_steps, pos, StreamApplicationType.UpstreamDetectorFunction, real_steps % 2)
        elif real_steps + 1 >= imaginary_steps:
             if imaginary_steps < 0 and real_steps >= imaginary_steps and real_steps < imaginary_steps + abs(imaginary_steps):
                 root5_imag = get_5_root_of_12(w * abs(imaginary_steps))
                 root1_imag = get_1_root_of_12(w * max(0, real_steps - imaginary_steps))
                 
                 nid = get_n_gauss_sum(6, abs(imaginary_steps)) + 2 * imaginary_steps - (abs(imaginary_steps) - abs(real_steps)) - (abs(imaginary_steps) - 1)
                 vd_idx = (abs(real_steps)) % 2
                 
                 pos = origin + complex(root5_imag.real + root1_imag.real, root5_imag.imag + root1_imag.imag)
                 node_df = self._create_node(nid, pos, StreamApplicationType.UpstreamDetectorFunction, vd_idx)
        
        if node_df: nodes.append(node_df)

        # --- 4. Upstream Selector System ---
        node_ss = None
        r_ss = r * -1
        q_ss = q * -1
        
        if q_ss == 0 and q_ss == r_ss:
             node_ss = self._create_node(0, origin, StreamApplicationType.UpstreamSelectorSystem, 0)
        elif r_ss == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q_ss - 1)) + 1 
             root7_real = get_7_root_of_12(w * q_ss)
             pos = origin + root7_real
             node_ss = self._create_node(real_gauss + q_ss, pos, StreamApplicationType.UpstreamSelectorSystem, q_ss % 2) 
        elif q_ss + 1 >= r_ss and r_ss >= 0:
             if q_ss >= r_ss: 
                 root7_imag = get_7_root_of_12(w * max(0, q_ss - r_ss))
                 root5_imag = get_5_root_of_12(w * r_ss)
                 
                 real_gauss = get_n_gauss_sum(6, max(0, q_ss)) - 1 
                 nid = real_gauss - 2*(q_ss - 1) - r_ss
                 
                 vd_idx = q_ss % 2
                 pos = origin + complex(root5_imag.real + root7_imag.real, root5_imag.imag + root7_imag.imag)
                 node_ss = self._create_node(nid, pos, StreamApplicationType.UpstreamSelectorSystem, vd_idx)
                 
        if node_ss: nodes.append(node_ss)

        # --- 5. Upstream Consumer Function ---
        node_cf = None
        r_cf = r * -1
        
        if real_steps == 0 and real_steps == r_cf:
             node_cf = self._create_node(0, origin, StreamApplicationType.UpstreamConsumerFunction, 0)
        elif r_cf == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps)) - 1
             root7_real = get_7_root_of_12(w * real_steps)
             pos = origin + root7_real
             node_cf = self._create_node(real_gauss - 2*(real_steps - 1), pos, StreamApplicationType.UpstreamConsumerFunction, real_steps % 2)
        elif real_steps + 1 >= r_cf and r_cf >= 0:
             root7_imag = get_7_root_of_12(w * max(0, real_steps - r_cf + 1))
             root9_imag = get_9_root_of_12(w * r_cf)
             
             imaginary_gauss = get_n_gauss_sum(6, real_steps + 1) - 1
             nid = imaginary_gauss - 2*real_steps + r_cf
             vd_idx = (real_steps + r_cf + 1) % 2
             
             pos = origin + complex(root9_imag.real + root7_imag.real, root9_imag.imag + root7_imag.imag)
             node_cf = self._create_node(nid, pos, StreamApplicationType.UpstreamConsumerFunction, vd_idx)

        if node_cf: nodes.append(node_cf)

        # --- 6. Upstream Detector System ---
        node_ds = None
        if r < 0 and real_steps >= r and real_steps < r + abs(r):
             root9_imag = get_9_root_of_12(w * abs(r))
             root1_imag = get_1_root_of_12(w * max(0, real_steps - r))
             
             nid = get_n_gauss_sum(6, abs(r)) + real_steps + 1 
             vd_idx = (abs(real_steps) + abs(r)) % 2
             
             pos = origin + complex(root9_imag.real + root1_imag.real, root9_imag.imag + root1_imag.imag)
             node_ds = self._create_node(nid, pos, StreamApplicationType.UpstreamDetectorSystem, vd_idx)
             
        if node_ds: nodes.append(node_ds)
        
        return nodes


class DownstreamEdgeLattice(LatticeMath):
    """
    Implements the Downstream Edge Lattice (Mijlocul laturilor inversate).
    """
    @property
    def step_distance(self) -> float:
        # Java: getStep() == 0 ? getStreamDistance() : 1/ Math.sqrt(3) * getStreamDistance() / Math.pow(3, getStep());
        base = self.config.stream_distance
        if self.config.step == 0:
            return base
        return (1.0 / math.sqrt(3)) * base / (3 ** self.config.step)

    def get_scroll(self) -> complex:
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        return complex(0.75 * w, 0.5 * h)

    def get_nodes(self, q: int, r: int, origin: complex) -> List[Node]:
        nodes = []
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        
        real_steps = q
        imaginary_steps = r
        
        # --- 1. Downstream Selector Function ---
        node_sf = None
        if real_steps == 0 and real_steps == imaginary_steps:
             node_sf = self._create_node(0, origin, StreamApplicationType.DownstreamSelectorFunction, 0)
        elif imaginary_steps == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root6_real = get_6_root_of_12(h * real_steps)
             pos = origin + root6_real
             node_sf = self._create_node(real_gauss, pos, StreamApplicationType.DownstreamSelectorFunction, real_steps % 2)
        elif real_steps + 1 >= imaginary_steps:
             root6_imag = get_6_root_of_12(h * max(0, real_steps - imaginary_steps + 1))
             root8_imag = get_8_root_of_12(h * imaginary_steps)
             nid = get_n_gauss_sum(6, real_steps) + imaginary_steps + 1
             vd_idx = (real_steps + imaginary_steps + 1) % 2 if imaginary_steps % 2 == 0 else (real_steps + imaginary_steps) % 2
             pos = origin + complex(root8_imag.real + root6_imag.real, root8_imag.imag + root6_imag.imag)
             node_sf = self._create_node(nid, pos, StreamApplicationType.DownstreamSelectorFunction, vd_idx)
             
        if node_sf: nodes.append(node_sf)
        
        # --- 2. Downstream Consumer System ---
        node_cs = None
        if r < 0 and q >= r and q < r + abs(r):
             root8_imag = get_8_root_of_12(h * abs(r))
             root12_imag = get_12_root_of_12(h * max(0, q - r))
             
             imaginary_gauss = get_n_gauss_sum(6, abs(r) - 1)
             nid = imaginary_gauss - 2*r + q + 1
             vd_idx = abs(q) % 2
             
             pos = origin + complex(root8_imag.real + root12_imag.real, root8_imag.imag + root12_imag.imag)
             node_cs = self._create_node(nid, pos, StreamApplicationType.DownstreamConsumerSystem, vd_idx)
             
        if node_cs: nodes.append(node_cs)

        # --- 3. Downstream Detector Function ---
        node_df = None
        if q == 0 and q == r:
             node_df = self._create_node(0, origin, StreamApplicationType.DownstreamDetectorFunction, 0)
        elif r == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q))
             root12_real = get_12_root_of_12(h * q)
             pos = origin + root12_real
             node_df = self._create_node(real_gauss - 3*q + 1, pos, StreamApplicationType.DownstreamDetectorFunction, q % 2)
        elif q + 1 >= r:
             root10_imag = get_10_root_of_12(h * r)
             root12_imag = get_12_root_of_12(h * max(0, q - r + 1))
             nid = get_n_gauss_sum(6, max(0, q + 1)) - 3*(q+1) - r + 1
             vd_idx = (q + r + 1) % 2
             pos = origin + complex(root10_imag.real + root12_imag.real, root10_imag.imag + root12_imag.imag)
             node_df = self._create_node(nid, pos, StreamApplicationType.DownstreamDetectorFunction, vd_idx)

        if node_df: nodes.append(node_df)

        # --- 4. Downstream Selector System ---
        node_ss = None
        r_ss = r * -1
        
        if q == 0 and q == r_ss:
             node_ss = self._create_node(0, origin, StreamApplicationType.DownstreamSelectorSystem, 0)
        elif r_ss == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q))
             root12_real = get_12_root_of_12(h * q)
             node_ss = self._create_node(real_gauss - 3*q + 1, origin + root12_real, StreamApplicationType.DownstreamSelectorSystem, q % 2)
        elif q + 1 >= r_ss:
             root2_imag = get_2_root_of_12(h * r_ss)
             root12_imag = get_12_root_of_12(h * max(0, q - r_ss + 1))
             nid = get_n_gauss_sum(6, max(0, q + 1)) - 3*(q+1) + r_ss + 1
             vd_idx = (q + 1) % 2
             pos = origin + complex(root2_imag.real + root12_imag.real, root2_imag.imag + root12_imag.imag)
             node_ss = self._create_node(nid, pos, StreamApplicationType.DownstreamSelectorSystem, vd_idx)
             
        if node_ss: nodes.append(node_ss)

        # --- 5. Downstream Consumer Function ---
        node_cf = None
        if r < 0 and q >= r and q < r + abs(r):
             root4_imag = get_4_root_of_12(h * abs(r))
             root12_imag = get_12_root_of_12(h * max(0, q - r))
             
             imaginary_gauss = get_n_gauss_sum(6, abs(r))
             nid = imaginary_gauss + 2*r - q + 1
             vd_idx = abs(q + r) % 2
             
             pos = origin + complex(root4_imag.real + root12_imag.real, root4_imag.imag + root12_imag.imag)
             node_cf = self._create_node(nid, pos, StreamApplicationType.DownstreamConsumerFunction, vd_idx)
             
        if node_cf: nodes.append(node_cf)

        # --- 6. Downstream Detector System ---
        node_ds = None
        q_ds = q * -1
        r_ds = r * -1
        
        if q_ds == 0 and q_ds == r_ds:
             node_ds = self._create_node(0, origin, StreamApplicationType.DownstreamDetectorSystem, 0)
        elif r_ds == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q_ds - 1)) + 1
             root6_real = get_6_root_of_12(h * q_ds)
             node_ds = self._create_node(real_gauss, origin + root6_real, StreamApplicationType.DownstreamDetectorSystem, q_ds % 2)
        elif q_ds >= r_ds:
             root4_imag = get_4_root_of_12(h * r_ds)
             root6_imag = get_6_root_of_12(h * max(0, q_ds - r_ds))
             
             real_gauss = get_n_gauss_sum(6, abs(q_ds))
             nid = real_gauss - r_ds + 1
             vd_idx = (q_ds + r_ds) % 2
             
             pos = origin + complex(root4_imag.real + root6_imag.real, root4_imag.imag + root6_imag.imag)
             node_ds = self._create_node(nid, pos, StreamApplicationType.DownstreamDetectorSystem, vd_idx)
             
        if node_ds: nodes.append(node_ds)
        
        return nodes

class UpstreamVertexLattice(LatticeMath):
    """Mathematics for the Upstream Vertex Lattice."""
    
    def get_scroll(self) -> complex:
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        return complex(1.0 * w, 0.5 * h) 

    def get_nodes(self, q: int, r: int, origin: complex) -> List[Node]:
        nodes = []
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        
        real_steps = q
        imaginary_steps = r
        
        # --- 1. Upstream Selector Function ---
        node_sf = None
        if real_steps == 0 and real_steps == imaginary_steps:
             node_sf = self._create_node(0, origin, StreamApplicationType.UpstreamSelectorFunction, 0)
        elif imaginary_steps == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root12_real = get_12_root_of_12(h * real_steps)
             pos = origin + root12_real
             node_sf = self._create_node(real_gauss + real_steps, pos, StreamApplicationType.UpstreamSelectorFunction, real_steps % 2)
        elif real_steps + 1 >= imaginary_steps:
             root10_imag = get_10_root_of_12(h * imaginary_steps)
             root12_imag = get_12_root_of_12(h * max(0, real_steps - imaginary_steps + 1))
             nid = get_n_gauss_sum(6, real_steps) + real_steps - imaginary_steps + 2
             vd_idx = (real_steps + imaginary_steps + 1) % 2 if imaginary_steps % 2 == 0 else (real_steps + imaginary_steps) % 2
             pos = origin + complex(root10_imag.real + root12_imag.real, root10_imag.imag + root12_imag.imag)
             node_sf = self._create_node(nid, pos, StreamApplicationType.UpstreamSelectorFunction, vd_idx)
             
        if node_sf: nodes.append(node_sf)
        
        # --- 2. Upstream Consumer System ---
        node_cs = None
        r_cs = r * -1
        if real_steps == 0 and real_steps == r_cs:
             node_cs = self._create_node(0, origin, StreamApplicationType.UpstreamConsumerSystem, 0)
        elif r_cs == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root12_real = get_12_root_of_12(h * real_steps)
             pos = origin + root12_real
             node_cs = self._create_node(real_gauss + real_steps, pos, StreamApplicationType.UpstreamConsumerSystem, real_steps % 2)
        elif real_steps + 1 >= r_cs and r_cs >= 0:
             root2_imag = get_2_root_of_12(h * r_cs)
             root12_imag = get_12_root_of_12(h * max(0, real_steps - r_cs + 1))
             nid = get_n_gauss_sum(6, real_steps) + real_steps + r_cs + 2
             vd_idx = (real_steps + r_cs + 1) % 2
             pos = origin + complex(root2_imag.real + root12_imag.real, root2_imag.imag + root12_imag.imag)
             node_cs = self._create_node(nid, pos, StreamApplicationType.UpstreamConsumerSystem, vd_idx)

        if node_cs: nodes.append(node_cs)

        # --- 3. Upstream Detector Function ---
        node_df = None
        if r < 0 and real_steps >= r and real_steps < r + abs(r):
             root4_imag = get_4_root_of_12(h * abs(r))
             root12_imag = get_12_root_of_12(h * max(0, real_steps - r))
             
             nid = get_n_gauss_sum(6, abs(r)) + 2 * r - (abs(r) - abs(real_steps)) - (abs(r) - 1)
             vd_idx = abs(real_steps) % 2
             
             pos = origin + complex(root4_imag.real + root12_imag.real, root4_imag.imag + root12_imag.imag)
             node_df = self._create_node(nid, pos, StreamApplicationType.UpstreamDetectorFunction, vd_idx)
             
        if node_df: nodes.append(node_df)

        # --- 4. Upstream Selector System ---
        node_ss = None
        q_ss = q * -1
        r_ss = r * -1
        
        if q_ss == 0 and q_ss == r_ss:
             node_ss = self._create_node(0, origin, StreamApplicationType.UpstreamSelectorSystem, 0)
        elif r_ss == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q_ss)) - 1
             root6_real = get_6_root_of_12(h * q_ss)
             pos = origin + root6_real
             node_ss = self._create_node(real_gauss - 2*(q_ss - 1), pos, StreamApplicationType.UpstreamSelectorSystem, q_ss % 2)
        elif q_ss >= r_ss and r_ss >= 0:
             root4_imag = get_4_root_of_12(h * r_ss)
             root6_imag = get_6_root_of_12(h * max(0, q_ss - r_ss))
             
             real_gauss = get_n_gauss_sum(6, max(0, q_ss)) - 1
             nid = real_gauss - 2*(q_ss - 1) - r_ss
             vd_idx = q_ss % 2
             
             pos = origin + complex(root4_imag.real + root6_imag.real, root4_imag.imag + root6_imag.imag)
             node_ss = self._create_node(nid, pos, StreamApplicationType.UpstreamSelectorSystem, vd_idx)

        if node_ss: nodes.append(node_ss)

        # --- 5. Upstream Consumer Function ---
        node_cf = None
        r_cf = r * -1
        
        if real_steps == 0 and real_steps == r_cf:
             node_cf = self._create_node(0, origin, StreamApplicationType.UpstreamConsumerFunction, 0)
        elif r_cf == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps)) - 1
             root6_real = get_6_root_of_12(h * real_steps)
             pos = origin + root6_real
             node_cf = self._create_node(real_gauss - 2*(real_steps - 1), pos, StreamApplicationType.UpstreamConsumerFunction, real_steps % 2)
        elif real_steps + 1 >= r_cf and r_cf >= 0:
             root8_imag = get_8_root_of_12(h * r_cf)
             root6_imag = get_6_root_of_12(h * max(0, real_steps - r_cf + 1))
             imaginary_gauss = get_n_gauss_sum(6, real_steps + 1) - 1
             nid = imaginary_gauss - 2*real_steps + r_cf
             vd_idx = (real_steps + r_cf + 1) % 2
             pos = origin + complex(root8_imag.real + root6_imag.real, root8_imag.imag + root6_imag.imag)
             node_cf = self._create_node(nid, pos, StreamApplicationType.UpstreamConsumerFunction, vd_idx)

        if node_cf: nodes.append(node_cf)

        # --- 6. Upstream Detector System ---
        node_ds = None
        r_ds = r * -1
        if r < 0 and real_steps >= r and real_steps < r + abs(r):
             root8_imag = get_8_root_of_12(h * abs(r))
             root12_imag = get_12_root_of_12(h * max(0, real_steps - r))
             
             nid = get_n_gauss_sum(6, abs(r)) + real_steps + 1 
             vd_idx = (abs(real_steps) + abs(r)) % 2
             
             pos = origin + complex(root8_imag.real + root12_imag.real, root8_imag.imag + root12_imag.imag)
             node_ds = self._create_node(nid, pos, StreamApplicationType.UpstreamDetectorSystem, vd_idx)
             
        if node_ds: nodes.append(node_ds)
        
        return nodes

class DownstreamVertexLattice(LatticeMath):
    """
    Implements the Downstream Vertex Lattice.
    """
    @property
    def step_distance(self) -> float:
        # Java mirror
        base = self.config.stream_distance
        if self.config.step == 0:
            return base
        return (1.0 / math.sqrt(3)) * base / (3 ** self.config.step)

    def get_scroll(self) -> complex:
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        return complex(0.75 * w, 0.75 * h)

    def get_nodes(self, q: int, r: int, origin: complex) -> List[Node]:
        nodes = []
        w = self.width * self.config.scale
        h = self.height * self.config.scale
        
        real_steps = q
        imaginary_steps = r
        
        # --- 1. Downstream Selector Function ---
        node_sf = None
        if real_steps == 0 and real_steps == imaginary_steps:
             node_sf = self._create_node(0, origin, StreamApplicationType.DownstreamSelectorFunction, 0)
        elif imaginary_steps == 0:
             real_gauss = get_n_gauss_sum(6, max(0, real_steps - 1)) + 1
             root7_real = get_7_root_of_12(w * real_steps)
             pos = origin + root7_real
             node_sf = self._create_node(real_gauss, pos, StreamApplicationType.DownstreamSelectorFunction, real_steps % 2)
        elif real_steps + 1 >= imaginary_steps:
             root9_imag = get_9_root_of_12(w * imaginary_steps)
             root7_imag = get_7_root_of_12(w * max(0, real_steps - imaginary_steps + 1))
             nid = get_n_gauss_sum(6, real_steps) + imaginary_steps + 1
             vd_idx = (real_steps + imaginary_steps + 1) % 2 if imaginary_steps % 2 == 0 else (real_steps + imaginary_steps) % 2
             pos = origin + complex(root9_imag.real + root7_imag.real, root9_imag.imag + root7_imag.imag)
             node_sf = self._create_node(nid, pos, StreamApplicationType.DownstreamSelectorFunction, vd_idx)
             
        if node_sf: nodes.append(node_sf)
        
        # --- 2. Downstream Consumer System ---
        node_cs = None
        r_cs = r * -1
        if r < 0 and q >= r and q < r + abs(r):
             root9_imag = get_9_root_of_12(w * abs(r))
             root1_imag = get_1_root_of_12(w * max(0, q - r))
             imaginary_gauss = get_n_gauss_sum(6, abs(r) - 1)
             nid = imaginary_gauss - 2*r + q + 1
             vd_idx = abs(q) % 2
             pos = origin + complex(root9_imag.real + root1_imag.real, root9_imag.imag + root1_imag.imag)
             node_cs = self._create_node(nid, pos, StreamApplicationType.DownstreamConsumerSystem, vd_idx)
             
        if node_cs: nodes.append(node_cs)
        
        # --- 3. Downstream Detector Function ---
        node_df = None
        if q == 0 and q == r:
             node_df = self._create_node(0, origin, StreamApplicationType.DownstreamDetectorFunction, 0)
        elif r == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q))
             root1_real = get_1_root_of_12(w * q)
             pos = origin + root1_real
             node_df = self._create_node(real_gauss - 3*q + 1, pos, StreamApplicationType.DownstreamDetectorFunction, q % 2)
        elif q + 1 >= r:
             root11_imag = get_11_root_of_12(w * r)
             root1_imag = get_1_root_of_12(w * max(0, q - r + 1))
             nid = get_n_gauss_sum(6, max(0, q + 1)) - 3*(q+1) - r + 1
             vd_idx = (q + r + 1) % 2
             pos = origin + complex(root11_imag.real + root1_imag.real, root11_imag.imag + root1_imag.imag)
             node_df = self._create_node(nid, pos, StreamApplicationType.DownstreamDetectorFunction, vd_idx)
             
        if node_df: nodes.append(node_df)
        
        # --- 4. Downstream Selector System ---
        node_ss = None
        q_ss = q * -1
        r_ss = r * -1
        
        if q_ss == 0 and q_ss == r_ss:
             node_ss = self._create_node(0, origin, StreamApplicationType.DownstreamSelectorSystem, 0)
        elif r_ss == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q_ss)) - 1
             root1_real = get_1_root_of_12(w * q_ss)
             node_ss = self._create_node(real_gauss - 3*q_ss + 1, origin + root1_real, StreamApplicationType.DownstreamSelectorSystem, q_ss % 2)
        elif q_ss + 1 >= r_ss:
             root3_imag = get_3_root_of_12(w * r_ss)
             root1_imag = get_1_root_of_12(w * max(0, q_ss - r_ss + 1))
             nid = get_n_gauss_sum(6, max(0, q_ss + 1)) - 3*(q_ss+1) + r_ss + 1
             vd_idx = (q_ss + 1) % 2
             pos = origin + complex(root3_imag.real + root1_imag.real, root3_imag.imag + root1_imag.imag)
             node_ss = self._create_node(nid, pos, StreamApplicationType.DownstreamSelectorSystem, vd_idx)
             
        if node_ss: nodes.append(node_ss)
        
        # --- 5. Downstream Consumer Function ---
        node_cf = None
        r_cf = r * -1
        if r < 0 and q >= r and q < r + abs(r):
             root5_imag = get_5_root_of_12(w * abs(r))
             root1_imag = get_1_root_of_12(w * max(0, q - r))
             
             imaginary_gauss = get_n_gauss_sum(6, abs(r))
             nid = imaginary_gauss + 2*r - q + 1
             vd_idx = abs(q + r) % 2
             
             pos = origin + complex(root5_imag.real + root1_imag.real, root5_imag.imag + root1_imag.imag)
             node_cf = self._create_node(nid, pos, StreamApplicationType.DownstreamConsumerFunction, vd_idx)
             
        if node_cf: nodes.append(node_cf)
        
        # --- 6. Downstream Detector System ---
        node_ds = None
        q_ds = q * -1
        r_ds = r * -1
        
        if q_ds == 0 and q_ds == r_ds:
             node_ds = self._create_node(0, origin, StreamApplicationType.DownstreamDetectorSystem, 0)
        elif r_ds == 0:
             real_gauss = get_n_gauss_sum(6, max(0, q_ds - 1)) + 1
             root7_real = get_7_root_of_12(w * q_ds)
             node_ds = self._create_node(real_gauss - r_ds + 1, origin + root7_real, StreamApplicationType.DownstreamDetectorSystem, int(q_ds % 2))
        elif q_ds >= r_ds:
             root5_imag = get_5_root_of_12(w * r_ds)
             root7_imag = get_7_root_of_12(w * max(0, q_ds - r_ds))
             
             real_gauss = get_n_gauss_sum(6, abs(q_ds))
             nid = real_gauss - r_ds + 1
             vd_idx = (q_ds + r_ds) % 2
             
             pos = origin + complex(root5_imag.real + root7_imag.real, root5_imag.imag + root7_imag.imag)
             node_ds = self._create_node(nid, pos, StreamApplicationType.DownstreamDetectorSystem, vd_idx)
             
        if node_ds: nodes.append(node_ds)
        
        return nodes
