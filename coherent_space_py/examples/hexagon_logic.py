from coherent_space_py.model.enums import (
    StreamApplicationType, 
    InformationalStreamVectorDirection
)
from coherent_space_py.model.topology_rules import (
    get_upstream_connections,
    get_downstream_connections
)

VD = InformationalStreamVectorDirection
SAT = StreamApplicationType

def print_hexagon(title: str, is_upstream: bool, source_vd: VD):
    print(f"\n=== {title} ===")
    print(f"Vector Direction: {source_vd.name}")
    
    types = [t for t in SAT]
    relevant_types = [t for t in types if ("Upstream" in t.name if is_upstream else "Downstream" in t.name)]
    
    for node_type in relevant_types:
        if is_upstream:
            conns = get_upstream_connections(node_type, source_vd)
        else:
            conns = get_downstream_connections(node_type, source_vd)
            
        print(f"  {node_type.name} -> {[t.name for t in conns]}")

def main():
    print("Generating Hexagon Logic (4 Types)...")
    # Define test cases for 4 variations: Upstream/Downstream x Vertex/Edge
    configs = [
        ("Upstream Hexagon (Type A - Vertex Mode)", True, VD.Vertex),
        ("Upstream Hexagon (Type B - Edge Mode)", True, VD.Edge),
        ("Downstream Hexagon (Type A - Vertex Mode)", False, VD.Vertex),
        ("Downstream Hexagon (Type B - Edge Mode)", False, VD.Edge)
    ]
    
    for title, is_upstream, source_vd in configs:
        print_hexagon(title, is_upstream, source_vd)

if __name__ == "__main__":
    main()

