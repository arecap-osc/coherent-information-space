"""Mathematical utilities for Coherent Space."""

def get_n_gauss_sum(n: int, gauss_sum_n: int) -> int:
    """
    Calculates the N-th Gauss Sum (Arithmetic Progression Sum).
    Formula: n * k * (k + 1) / 2
    
    Args:
        n: The multiplier (usually 6 for hexagonal steps).
        gauss_sum_n: The number of steps (k).
        
    Returns:
        The calculated sum.
    """
    return int(n * gauss_sum_n * (gauss_sum_n + 1) / 2)
