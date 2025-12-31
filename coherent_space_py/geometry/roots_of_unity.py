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


def roots_of_12(r: float) -> list[complex]:
    """Returns a list of all 12th roots of unity scaled by r."""
    # Note: The original `roots_of_12` function was a convenience wrapper for a single root.
    # This new function returns all 12 roots.
    # The `root_of_unity` function returns a Tuple[float, float], not complex.
    # We'll convert it to complex here for consistency with the new functions.
    return [complex(*root_of_unity(k + 1, r, order=12)) for k in range(12)]

def get_root_of_12_k(k: int, r: float) -> complex:
    """Returns the k-th root of 12 scaled by r."""
    # Java indices are 1-based in naming? No, looking at Java:
    # get1RootOf12 corresponds to index?
    # Java RootsOfUnity.java:
    # public static ComplexPlane get1RootOf12(Double quota) { return getRootOfUnity(12, 1, quota); }
    # So it uses k=1 directly.
    return complex(*root_of_unity(k, r, order=12))

def get_1_root_of_12(r: float) -> complex: return get_root_of_12_k(1, r)
def get_2_root_of_12(r: float) -> complex: return get_root_of_12_k(2, r)
def get_3_root_of_12(r: float) -> complex: return get_root_of_12_k(3, r)
def get_4_root_of_12(r: float) -> complex: return get_root_of_12_k(4, r)
def get_5_root_of_12(r: float) -> complex: return get_root_of_12_k(5, r)
def get_6_root_of_12(r: float) -> complex: return get_root_of_12_k(6, r)
def get_7_root_of_12(r: float) -> complex: return get_root_of_12_k(7, r)
def get_8_root_of_12(r: float) -> complex: return get_root_of_12_k(8, r)
def get_9_root_of_12(r: float) -> complex: return get_root_of_12_k(9, r)
def get_10_root_of_12(r: float) -> complex: return get_root_of_12_k(10, r)
def get_11_root_of_12(r: float) -> complex: return get_root_of_12_k(11, r)
def get_12_root_of_12(r: float) -> complex: return get_root_of_12_k(12, r)


__all__ = [
    "root_of_unity",
    "roots_of_12",
    "get_root_of_12_k",
    "get_1_root_of_12",
    "get_2_root_of_12",
    "get_3_root_of_12",
    "get_4_root_of_12",
    "get_5_root_of_12",
    "get_6_root_of_12",
    "get_7_root_of_12",
    "get_8_root_of_12",
    "get_9_root_of_12",
    "get_10_root_of_12",
    "get_11_root_of_12",
    "get_12_root_of_12",
]
