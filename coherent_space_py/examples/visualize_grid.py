import matplotlib.pyplot as plt
from coherent_space_py.model.infinite_graph import InfiniteCoherentGraph
from coherent_space_py.model.enums import (
    InformationalStreamNetting,
    InformationalStreamVectorDirection,
    StreamApplicationType,
)

VD = InformationalStreamVectorDirection

def visualize_infinite():
    # 1. Init Infinite Graph
    graph = InfiniteCoherentGraph(stream_distance=100.0, scale=1.0)
    
    # 2. Get a patch (symmetric around origin)
    # Range -6 to +6 to cover all quadrants
    nodes = graph.get_patch(range(-6, 7), range(-6, 7))
    
    print(f"Generated {len(nodes)} nodes in patch.")
    
    # 3. Plot
    fig, ax = plt.subplots(figsize=(10, 8))
    
    netting_colors = {
        InformationalStreamNetting.UpstreamEdge: "#1f77b4",
        InformationalStreamNetting.UpstreamVertex: "#2ca02c",
        InformationalStreamNetting.DownstreamEdge: "#ff7f0e",
        InformationalStreamNetting.DownstreamVertex: "#d62728",
    }

    xs = []
    ys = []
    colors = []
    labels = []
    
    for node in nodes:
        xs.append(node.position.real)
        ys.append(node.position.imag)

        colors.append(netting_colors.get(node.netting, "#7f7f7f"))

        # Label upstream vs downstream for quick glance
        labels.append("U" if node.stream_application_type.value.startswith("Upstream") else "D")
        
        # Offset slightly if positions are identical?
        # Matplotlib will overdraw.
            
    ax.scatter(xs, ys, c=colors, s=100, alpha=0.6, edgecolors='black')
    
    # Annotate
    for x, y, label in zip(xs, ys, labels):
         ax.text(x, y, label, ha='center', va='center', color='white', fontsize=8)

    # Find closest node of target type
    connection_distance_cap = 1.2 * graph.stream_distance * graph.scale
    for node in nodes: # Iterate through nodes again to draw connections
        if node.connections:
            for target_type_name in node.connections.keys():
                 # Simple heuristic: find nearest node of that type in the patch
                 # In a real implementation, this would be computed by math
                 
                 target_enum = StreamApplicationType[target_type_name]
                 candidates = [n for n in nodes if n.stream_application_type == target_enum]
                 if not candidates: continue
                 
                 # Find nearest
                 nearest = min(candidates, key=lambda n: abs(n.position - node.position))
                 
                 # Distance check to avoid drawing across the map (local connections only)
                 if abs(nearest.position - node.position) < connection_distance_cap:
                     ax.plot(
                         [node.position.real, nearest.position.real],
                         [node.position.imag, nearest.position.imag],
                         color='gray',
                         alpha=0.3, # Faint lines
                         linestyle='-',
                         linewidth=0.5
                     )

    # Annotate a few nodes using the new logic
    count = 0
    for node in nodes:
        if -0.5 < node.position.real < 0.5 and -0.5 < node.position.imag < 0.5:
             # Determine drawing style based on vector direction (Vertex vs Edge)
            line_style = '-' if node.vector_direction == VD.CornerParity else '--'
            alpha = 0.6 if node.vector_direction == VD.CornerParity else 0.4 #text, fontsize=6)

    ax.set_aspect('equal')
    plt.title('Infinite Coherent Grid: Quad Lattice + Connection "Roads"')
    plt.grid(True, linestyle=':', alpha=0.3)
    plt.savefig('infinite_grid_viz.png', dpi=300)
    print("Visualization saved to infinite_grid_viz.png")

if __name__ == "__main__":
    visualize_infinite()
