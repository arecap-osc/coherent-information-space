
"""Geometry positions for subgraph schematics (Deliverable A).

This module is intentionally small and deterministic:
- Upstream positions: regular hexagon, vertical (pointy-top)
- Downstream positions: regular hexagon, horizontal (flat-top), inner-hex scale = 1/sqrt(3)
- Parity does NOT affect geometry (only topology rules / vectors).

It complements roots_of_unity.py; for schematic drawings we use explicit angles
that match the Java-validated mapping you've confirmed visually.
"""

from __future__ import annotations
import math
from typing import Dict, Tuple, Mapping

from coherent_space_py.model.enums import StreamProcessType

# Confirmed angle mappings
UPSTREAM_ANGLES: Mapping[StreamProcessType, float] = {
    StreamProcessType.UpstreamSelectorFunction: 90,   # Top
    StreamProcessType.UpstreamConsumerSystem: 30,     # Top-Right
    StreamProcessType.UpstreamDetectorFunction: 330,  # Bottom-Right
    StreamProcessType.UpstreamSelectorSystem: 270,    # Bottom
    StreamProcessType.UpstreamConsumerFunction: 210,  # Bottom-Left
    StreamProcessType.UpstreamDetectorSystem: 150     # Top-Left
}

# Downstream: horizontal (flat-top) inner hexagon
DOWNSTREAM_ANGLES: Mapping[StreamProcessType, float] = {
    StreamProcessType.DownstreamSelectorSystem: 0,      # Right
    StreamProcessType.DownstreamConsumerFunction: 60,   # Top-Right
    StreamProcessType.DownstreamDetectorSystem: 120,    # Top-Left
    StreamProcessType.DownstreamSelectorFunction: 180,  # Left
    StreamProcessType.DownstreamConsumerSystem: 240,    # Bottom-Left
    StreamProcessType.DownstreamDetectorFunction: 300   # Bottom-Right
}

DEFAULT_DOWNSTREAM_SCALE = 1.0 / math.sqrt(3)


def polar_xy(radius: float, angle_deg: float) -> Tuple[float, float]:
    a = math.radians(angle_deg)
    return (radius * math.cos(a), radius * math.sin(a))


def get_positions(
    *,
    mode: str,
    upstream_radius: float = 1.0,
    downstream_radius: float = DEFAULT_DOWNSTREAM_SCALE,
    upstream_angles: Mapping[StreamProcessType, float] = UPSTREAM_ANGLES,
    downstream_angles: Mapping[StreamProcessType, float] = DOWNSTREAM_ANGLES,
) -> Dict[StreamProcessType, Tuple[float, float]]:
    """Return positions for a single schematic.

    mode in {"UP","DOWN","BOTH"}.
    """
    m = mode.upper().strip()
    out: Dict[StreamProcessType, Tuple[float, float]] = {}

    if m in ("UP", "BOTH"):
        for t, deg in upstream_angles.items():
            out[t] = polar_xy(upstream_radius, deg)

    if m in ("DOWN", "BOTH"):
        for t, deg in downstream_angles.items():
            out[t] = polar_xy(downstream_radius, deg)

    return out


def polygon_points_in_angle_order(points):
    """Sort list[(x,y)] by angle for polygon drawing."""
    return sorted(points, key=lambda p: math.atan2(p[1], p[0]))
