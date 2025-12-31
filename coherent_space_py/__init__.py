"""Python reimplementation of the coherent hexagonal information space."""

from coherent_space_py.builders.hex_graph_builder import CoherentSpaceBuilder
from coherent_space_py.geometry.roots_of_unity import root_of_unity, roots_of_12
from coherent_space_py.model.node import Node
from coherent_space_py.model.graph import InformationalStreamGraph
from coherent_space_py.utils.gauss import gauss_sum
from coherent_space_py.model.enums import (
    InformationalStreamNeuronType,
    InformationalStreamNetting,
    InformationalStreamVectorDirection,
    StreamTopology,
    StreamApplicationType,  # compat alias
)

__all__ = [
    "CoherentSpaceBuilder",
    "root_of_unity",
    "roots_of_12",
    "Node",
    "InformationalStreamGraph",
    "gauss_sum",
    "InformationalStreamNeuronType",
    "StreamApplicationType",  # compat alias
    "InformationalStreamNetting",
    "InformationalStreamVectorDirection",
    "StreamTopology"
]
