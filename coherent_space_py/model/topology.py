from typing import Set, Tuple, Dict
from coherent_space_py.model.enums import InformationalStreamProcessType, StreamTopology

# Abbreviation map for cleaner definition, mirroring Java's InformationalStreamUtils
_ABBR: Dict[str, str] = {
    InformationalStreamProcessType.UpstreamSelectorFunction: "u(S)",
    InformationalStreamProcessType.UpstreamDetectorFunction: "u(D)",
    InformationalStreamProcessType.UpstreamConsumerFunction: "u(C)",
    InformationalStreamProcessType.UpstreamSelectorSystem: "S(u)",
    InformationalStreamProcessType.UpstreamDetectorSystem: "D(u)",
    InformationalStreamProcessType.UpstreamConsumerSystem: "C(u)",
    InformationalStreamProcessType.DownstreamSelectorFunction: "d(S)",
    InformationalStreamProcessType.DownstreamDetectorFunction: "d(D)",
    InformationalStreamProcessType.DownstreamConsumerFunction: "d(C)",
    InformationalStreamProcessType.DownstreamSelectorSystem: "S(d)",
    InformationalStreamProcessType.DownstreamDetectorSystem: "D(d)",
    InformationalStreamProcessType.DownstreamConsumerSystem: "C(d)",
}

# Reverse map for lookups if needed
_TYPE_BY_ABBR = {v: k for k, v in _ABBR.items()}

def _get_abbr(app_type: InformationalStreamProcessType) -> str:
    return _ABBR[app_type]

# Topology Sets as sets of tuples (upstream_abbr, downstream_abbr)
UPSTREAM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(S)", "D(u)"), ("D(u)", "u(C)"), ("u(C)", "S(u)"), ("S(u)", "u(C)"), 
    ("u(D)", "u(S)"), ("C(u)", "u(D)"), ("C(u)", "u(S)"), ("u(S)", "C(u)")
}

DOWNSTREAM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("D(d)", "d(S)"), ("D(d)", "d(C)"), ("d(C)", "S(d)"), ("S(d)", "d(C)"), 
    ("S(d)", "d(D)"), ("C(d)", "d(D)"), ("C(d)", "d(S)"), ("d(S)", "C(d)")
}

UPSTREAM_FUNCTION_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(S)", "u(C)"), ("u(C)", "u(S)"), ("u(D)", "u(S)"), ("u(C)", "u(D)")
}

UPSTREAM_SYSTEM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("S(u)", "C(u)"), ("C(u)", "S(u)"), ("S(u)", "D(u)"), ("D(u)", "C(u)")
}

DOWNSTREAM_FUNCTION_TOPOLOGY: Set[Tuple[str, str]] = {
    ("d(C)", "d(S)"), ("d(S)", "d(C)"), ("d(D)", "d(S)"), ("d(D)", "d(C)")
}

DOWNSTREAM_SYSTEM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("C(d)", "S(d)"), ("S(d)", "C(d)"), ("S(d)", "d(D)"), ("C(d)", "D(d)")
}

UPSTREAMS_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(S)", "C(d)"), ("C(d)", "u(S)"), ("d(S)", "u(C)"), ("u(C)", "d(S)"),
    ("d(D)", "u(S)"), ("u(D)", "S(d)"),
    ("u(C)", "D(d)"), ("d(C)", "u(D)"),
    ("S(u)", "D(d)"), ("d(S)", "D(u)"),
    ("S(u)", "d(C)"), ("d(C)", "S(u)"), ("C(u)", "S(d)"), ("S(d)", "C(u)"),
    ("D(u)", "C(d)"), ("d(D)", "C(u)")
}

DOWNSTREAMS_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(C)", "d(S)"), ("D(u)", "d(S)"), ("d(S)", "u(C)"),
    ("u(C)", "D(d)"), ("S(u)", "D(d)"),
    ("d(C)", "S(u)"), ("u(D)", "d(C)"), ("S(u)", "d(C)"),
    ("S(d)", "u(D)"), ("C(u)", "S(d)"), ("S(d)", "C(u)"),
    ("d(D)", "u(S)"), ("d(D)", "C(u)"),
    ("C(d)", "u(S)"), ("C(d)", "D(u)"), ("u(S)", "C(d)")
}

UPSTREAM_DOWNSTREAM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(S)", "d(S)"), ("d(S)", "u(S)"), ("C(d)", "u(C)"), ("u(C)", "C(d)"),
    ("S(d)", "u(S)"), ("u(D)", "d(D)"),
    ("u(C)", "d(C)"), ("D(d)", "u(D)"),
    ("S(u)", "d(S)"), ("D(d)", "D(u)"),
    ("S(u)", "S(d)"), ("S(d)", "S(u)"), ("C(u)", "d(C)"), ("d(C)", "C(u)"),
    ("D(u)", "d(D)"), ("C(d)", "C(u)")
}

DOWNSTREAM_UPSTREAM_TOPOLOGY: Set[Tuple[str, str]] = {
    ("u(S)", "d(S)"), ("S(u)", "d(S)"), ("d(S)", "S(u)"),
    ("u(D)", "D(d)"), ("D(u)", "D(d)"),
    ("d(C)", "u(C)"), ("C(u)", "d(C)"), ("u(C)", "d(C)"),
    ("u(S)", "S(d)"), ("S(d)", "S(u)"), ("S(d)", "u(S)"),
    ("d(D)", "u(D)"), ("d(D)", "D(u)"),
    ("C(d)", "u(C)"), ("C(d)", "C(u)"), ("C(u)", "C(d)")
}

def get_stream_topology(upstream: InformationalStreamProcessType, downstream: InformationalStreamProcessType) -> StreamTopology:
    """
    Determines the topology type based on the upstream and downstream application types.
    Mirrors InformationalStreamUtils.getStreamTopology
    """
    pair = (_get_abbr(upstream), _get_abbr(downstream))

    if pair in UPSTREAM_TOPOLOGY:
        return StreamTopology.Upstream
    if pair in DOWNSTREAM_TOPOLOGY:
        return StreamTopology.Downstream
    if pair in UPSTREAM_FUNCTION_TOPOLOGY:
        return StreamTopology.UpstreamFunction
    if pair in DOWNSTREAM_FUNCTION_TOPOLOGY:
        return StreamTopology.DownstreamFunction
    if pair in UPSTREAM_SYSTEM_TOPOLOGY:
        return StreamTopology.UpstreamSystem
    if pair in DOWNSTREAM_SYSTEM_TOPOLOGY:
        return StreamTopology.DownstreamSystem
    if pair in UPSTREAMS_TOPOLOGY:
        return StreamTopology.Upstreams
    if pair in DOWNSTREAMS_TOPOLOGY:
        return StreamTopology.Downstreams
    if pair in UPSTREAM_DOWNSTREAM_TOPOLOGY:
        return StreamTopology.UpstreamDownstream
    if pair in DOWNSTREAM_UPSTREAM_TOPOLOGY:
        return StreamTopology.DownstreamUpstream
    
    return StreamTopology.Trail
