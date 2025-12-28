"""Small arithmetic helpers used by the graph builders."""
from __future__ import annotations


def gauss_sum(multiplier: int, n: int) -> int:
    """Return ``multiplier * n * (n + 1) / 2`` as an int.

    The Java builders use ``getNGaussSum(6, k)`` repeatedly to create stable
    ranges of identifiers per ring of the hex grid. Keeping the helper separate
    makes that intent explicit and reusable.
    """
    return int(multiplier * n * (n + 1) // 2)


__all__ = ["gauss_sum"]
