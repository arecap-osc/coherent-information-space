import sys
from coherent_space_py.builders.hex_graph_builder import CoherentSpaceBuilder
from coherent_space_py.model.enums import InformationalStreamNetting

def main():
    print("Building Coherent Space with UpstreamEdge netting...")
    builder = CoherentSpaceBuilder(InformationalStreamNetting.DownstreamEdge)
    
    # Build a small grid (Step 2)
    graph = builder.build(step_count=2)
    
    print(f"Total nodes generated: {len(graph.nodes)}")
    
    # Verify we have nodes of specific types
    up_sel_func = graph.get_nodes_by_type("UpstreamSelectorFunction")
    print(f"UpstreamSelectorFunction nodes: {len(up_sel_func)}")
    
    if not up_sel_func:
        print("Error: No UpstreamSelectorFunction nodes found!")
        sys.exit(1)
        
    # Pick one and check neighbors
    node = up_sel_func[0]
    print(f"\nInspecting Node {node.id}:")
    print(f"  Type: {node.stream_application_type}")
    print(f"  Position: {node.position}")
    print(f"  Neighbors (IDs): {node.neighbors}")
    
    # Check neighbor types
    print("  Neighbor Details:")
    for nid in node.neighbors:
        neighbor = graph.nodes[nid]
        print(f"    -> Node {nid}: {neighbor.stream_application_type} (Dir: {neighbor.vector_direction})")

if __name__ == "__main__":
    main()
