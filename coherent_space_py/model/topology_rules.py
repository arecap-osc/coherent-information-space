from typing import List, Tuple
from coherent_space_py.model.enums import (
    StreamApplicationType, 
    InformationalStreamVectorDirection,
    InformationalStreamNetting
)

# Alias for brevity
VD = InformationalStreamVectorDirection
SAT = StreamApplicationType
ISN = InformationalStreamNetting

def get_upstream_connections(
    source_type: SAT, 
    source_vd: VD,
    origin_vd: VD = VD.CornerParity
) -> List[SAT]:
    """
    Returns the list of target StreamApplicationTypes that an UPSTREAM node connects to.
    """
    # Vertex Mode (was SDC)
    is_vertex = (origin_vd == VD.CornerParity and source_vd == VD.CornerParity)
    
    if source_type == SAT.UpstreamSelectorFunction:
        return [SAT.UpstreamDetectorSystem, SAT.UpstreamConsumerFunction] if is_vertex else [SAT.UpstreamDetectorSystem, SAT.UpstreamConsumerSystem]
        
    elif source_type == SAT.UpstreamSelectorSystem:
        return [SAT.UpstreamDetectorSystem, SAT.UpstreamConsumerSystem] if is_vertex else [SAT.UpstreamDetectorSystem, SAT.UpstreamConsumerFunction]
        
    elif source_type == SAT.UpstreamDetectorFunction:
        return [SAT.UpstreamSelectorFunction, SAT.UpstreamSelectorSystem]
        
    elif source_type == SAT.UpstreamDetectorSystem:
        return [SAT.UpstreamConsumerFunction, SAT.UpstreamConsumerSystem]
        
    elif source_type == SAT.UpstreamConsumerFunction:
         return [SAT.UpstreamSelectorSystem, SAT.UpstreamDetectorFunction] if is_vertex else [SAT.UpstreamSelectorFunction, SAT.UpstreamDetectorFunction]
         
    elif source_type == SAT.UpstreamConsumerSystem:
         return [SAT.UpstreamSelectorFunction, SAT.UpstreamDetectorFunction] if is_vertex else [SAT.UpstreamSelectorSystem, SAT.UpstreamDetectorFunction]
         
    return []

def get_downstream_connections(
    source_type: SAT, 
    source_vd: VD,
    origin_vd: VD = VD.CornerParity
) -> List[SAT]:
    """
    Returns the list of target StreamApplicationTypes that a DOWNSTREAM node connects to.
    Logic Strictly based on User's Mathematical Definition:
    
    Modes:
      Vertex (was SDC/Type A): D -> S
      Edge (was CDS/Type B): S -> D
    """
    is_vertex = (origin_vd == VD.CornerParity and source_vd == VD.CornerParity)
    
    targets = []
    
    # === COMMUTATIVE RULES (Invariant) ===
    
    if source_type == SAT.DownstreamSelectorSystem: # S2
        targets.extend([SAT.DownstreamConsumerFunction, SAT.DownstreamConsumerSystem]) # S2->&1, S2->&2
        
    elif source_type == SAT.DownstreamDetectorSystem: # D2
        targets.extend([SAT.DownstreamConsumerFunction, SAT.DownstreamConsumerSystem]) # D2->&1, D2->&2
        
    elif source_type == SAT.DownstreamConsumerFunction: # &1
        targets.extend([SAT.DownstreamDetectorFunction, SAT.DownstreamSelectorFunction]) # &1->D1, &1->S1
        
    elif source_type == SAT.DownstreamConsumerSystem: # &2
        # &2->S1, &2->D1 (User added)
        targets.extend([SAT.DownstreamSelectorFunction, SAT.DownstreamDetectorFunction])

    # === NASH RULES (Variable based on Vertex/Edge) ===
    
    if not is_vertex: # Edge Mode (CDS)
        # S1 -> D1, S1 -> D2
        if source_type == SAT.DownstreamSelectorFunction:
            targets.extend([SAT.DownstreamDetectorFunction, SAT.DownstreamDetectorSystem])
            
        # S2 -> D1, S2 -> D2
        elif source_type == SAT.DownstreamSelectorSystem:
            targets.extend([SAT.DownstreamDetectorFunction, SAT.DownstreamDetectorSystem])
            
    else: # Vertex Mode (SDC)
        # D1 -> S1, D1 -> S2
        if source_type == SAT.DownstreamDetectorFunction:
             targets.extend([SAT.DownstreamSelectorFunction, SAT.DownstreamSelectorSystem])
             
        # D2 -> S1, D2 -> S2
        elif source_type == SAT.DownstreamDetectorSystem:
             targets.extend([SAT.DownstreamSelectorFunction, SAT.DownstreamSelectorSystem])
    
    # Return unique list
    return list(set(targets))
