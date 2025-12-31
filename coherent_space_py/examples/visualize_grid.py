import matplotlib.pyplot as plt
from coherent_space_py.model.infinite_graph import InfiniteCoherentGraph

def visualize_infinite():
    # 1. Init Infinite Graph
    graph = InfiniteCoherentGraph(stream_distance=100.0, scale=1.0)
    
    # 2. Get a patch (symmetric around origin)
    # Range -6 to +6 to cover all quadrants
    nodes = graph.get_patch(range(-6, 7), range(-6, 7))
    
    print(f"Generated {len(nodes)} nodes in patch.")
    
    # 3. Plot
    fig, ax = plt.subplots(figsize=(10, 8))
    
    xs = []
    ys = []
    colors = []
    labels = []
    
    for node in nodes:
        xs.append(node.position.real)
        ys.append(node.position.imag)
        
        # Color by Netting Type logic
        if "Upstream" in node.stream_application_type:
            colors.append("blue") # Upstream
            labels.append("U")
        else:
            colors.append("orange") # Downstream
            labels.append("D")
            
        # Offset slightly if positions are identical?
        # Matplotlib will overdraw.
            
    ax.scatter(xs, ys, c=colors, s=100, alpha=0.6, edgecolors='black')
    
    # Annotate
    for x, y, label in zip(xs, ys, labels):
         ax.text(x, y, label, ha='center', va='center', color='white', fontsize=8)

    # Find closest node of target type
    for node in nodes: # Iterate through nodes again to draw connections
        if node.connections:
            for target_type_name in node.connections.keys():
                 # Simple heuristic: find nearest node of that type in the patch
                 # In a real implementation, this would be computed by math
                 
                 candidates = [n for n in nodes if n.stream_application_type == target_type_name]
                 if not candidates: continue
                 
                 # Find nearest
                 nearest = min(candidates, key=lambda n: abs(n.position - node.position))
                 
                 # Distance check to avoid drawing across the map (local connections only)
                 if abs(nearest.position - node.position) < 0.6: # Typically distance is ~0.3 - 0.5
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
            line_style = '-' if node.vector_direction == VD.Vertex else '--'
            alpha = 0.6 if node.vector_direction == VD.Vertex else 0.4text, fontsize=6)

    ax.set_aspect('equal')
    plt.title('Infinite Coherent Grid: Quad Lattice + Connection "Roads"')
    plt.grid(True, linestyle=':', alpha=0.3)
    plt.savefig('infinite_grid_viz.png', dpi=300)
    print("Visualization saved to infinite_grid_viz.png")

if __name__ == "__main__":
    visualize_infinite()
