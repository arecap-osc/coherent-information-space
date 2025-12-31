
"""Infinite Coherent Grid Model."""
from typing import List
from coherent_space_py.model.node import Node
from coherent_space_py.math.lattice import (
    LatticeConfig,
    LatticeMath,
    UpstreamEdgeLattice,
    DownstreamEdgeLattice,
    UpstreamVertexLattice,
    DownstreamVertexLattice
)

class InfiniteCoherentGraph:
    """
    Virtual Graph that computes nodes on-the-fly using Quad Lattice math.
    """
    
    def __init__(self, stream_distance: float = 1.0, scale: float = 1.0):
        self.config = LatticeConfig(
            stream_distance=stream_distance,
            step=0, # Base step
            scale=scale
        )
        
        # Initialize Lattices
        self.lattices = [
            UpstreamEdgeLattice(self.config),
            DownstreamEdgeLattice(self.config),
            UpstreamVertexLattice(self.config),
            DownstreamVertexLattice(self.config)
        ]
        
    def get_patch(self, q_range: range, r_range: range, origin: complex = 0j) -> List[Node]:
        """
        Generates nodes for a specific patch of the infinite grid.
        
        Args:
            q_range: Range of 'real' steps (e.g. range(0, 5))
            r_range: Range of 'imaginary' steps (e.g. range(0, 5))
            origin: The complex origin for this patch.
            
        Returns:
            List of superimposed nodes.
        """
        all_nodes = []
        
        for lat in self.lattices:
            # Apply specific scroll for each lattice?
            # In Java, the scroll determines the 'quota' for step calculation.
            # Here we assume q,r ARE the steps.
            # But we must apply the scroll offset to the final position relative to origin?
            # Or is the scroll intrinsic to the lattice zero-point?
            
            # Java: result.setReal(origin.getReal()); ... for ID 0.
            # So (0,0) is at Origin.
            # But the Lattice definition might imply (0,0) IS the scroll point?
            # Checking Java: "ComplexPlane scroll = getScroll(root);"
            # "Double realQuota = scroll.getReal();"
            # "Integer realSteps = (graphPosition - origin) / realQuota"
            
            # So the STEP SIZE is determined by the Scroll.
            # And the lattice 0,0 is at Origin.
            
            # So valid nodes are generated relative to 'origin' using q,r.
            
            for q in q_range:
                for r in r_range:
                    nodes = lat.get_nodes(q, r, origin)
                    all_nodes.extend(nodes)
                    
        return all_nodes
