"""Root-of-unity helpers for hexagonal geometry.

The original Java implementation enumerated the 12th roots of unity in a
clockwise order starting at the positive real axis. We mirror that behavior here
while relying on NumPy for the trigonometry so the rest of the code can operate
with plain float coordinates.
"""
from __future__ import annotations

import numpy as np
from typing import Tuple


_TAU = 2 * np.pi


def root_of_unity(index: int, quota: float, order: int = 12) -> Tuple[float, float]:
    """Return the cartesian coordinates for the given ``index`` root of unity.

    Parameters
    ----------
    index:
        1-based index of the root. The Java version numbers 1..12, so we keep
        that convention and wrap values that fall outside the range.
    quota:
        Radial distance (scale) from the origin.
    order:
        Number of roots for the unit circle. Defaults to 12.
    """
    normalized = ((index - 1) % order) + 1
    angle = _TAU * (normalized - 1) / order
    return float(quota * np.cos(angle)), float(quota * np.sin(angle))


def roots_of_12(index: int, quota: float) -> Tuple[float, float]:
    """Convenience wrapper for the 12th roots used by the coherent space."""
    return root_of_unity(index, quota, order=12)


__all__ = ["root_of_unity", "roots_of_12"]
