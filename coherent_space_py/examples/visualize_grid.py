import math
import matplotlib.pyplot as plt
from matplotlib.patches import RegularPolygon
from coherent_space_py.model.infinite_graph import InfiniteCoherentGraph
from coherent_space_py.model.enums import (
    InformationalStreamNetting,
    InformationalStreamVectorDirection,
    StreamApplicationType,
)

VD = InformationalStreamVectorDirection

def _short_label(app_type: StreamApplicationType) -> str:
    upstream = "U" if app_type.value.startswith("Upstream") else "D"
    role = (
        "S" if "Selector" in app_type.value else "D" if "Detector" in app_type.value else "C"
    )
    layer = "F" if "Function" in app_type.value else "S"
    return f"{upstream}{role}{layer}"


def visualize_infinite():
    # 1. Init Infinite Graph
    graph = InfiniteCoherentGraph(stream_distance=100.0, scale=1.0)
    
    # 2. Get a patch (symmetric around origin)
    # Range -6 to +6 to cover all quadrants
    nodes = graph.get_patch(range(-6, 7), range(-6, 7))
    
    print(f"Generated {len(nodes)} nodes in patch.")
    
    # 3. Plot
    fig, ax = plt.subplots(figsize=(12, 10))
    
    netting_colors = {
        InformationalStreamNetting.UpstreamEdge: "#1f77b4",
        InformationalStreamNetting.UpstreamVertex: "#2ca02c",
        InformationalStreamNetting.DownstreamEdge: "#ff7f0e",
        InformationalStreamNetting.DownstreamVertex: "#d62728",
    }

    # Infer a local hex radius from the nearest neighbor spacing to keep the
    # outlines consistent across nettings.
    min_spacing = None
    for i, a in enumerate(nodes):
        for b in nodes[i + 1 :]:
            dist = abs(a.position - b.position)
            if dist == 0:
                continue
            min_spacing = dist if min_spacing is None else min(min_spacing, dist)
    hex_radius = (min_spacing or graph.stream_distance) * 0.40

    # Prepare helpers
    nodes_by_id = {n.id: n for n in nodes}
    labels = []
    colors = []
    xs = []
    ys = []

    for node in nodes:
        xs.append(node.position.real)
        ys.append(node.position.imag)
        colors.append(netting_colors.get(node.netting, "#7f7f7f"))
        labels.append(_short_label(node.stream_application_type))

        # Draw hexagon patch per node
        face_alpha = 0.22 if node.vector_direction == VD.SideParity else 0.32
        edge_style = "-" if node.vector_direction == VD.CornerParity else "--"
        hexagon = RegularPolygon(
            (node.position.real, node.position.imag),
            numVertices=6,
            radius=hex_radius,
            orientation=0.0,
            facecolor=netting_colors.get(node.netting, "#7f7f7f"),
            edgecolor="black",
            alpha=face_alpha,
            linestyle=edge_style,
            linewidth=0.6,
        )
        ax.add_patch(hexagon)

    # Scatter centers after hexagons for crisp edges
    ax.scatter(xs, ys, c=colors, s=60, alpha=0.8, edgecolors="black")

    # Annotate U/D
    for x, y, label in zip(xs, ys, labels):
        ax.text(
            x,
            y,
            label,
            ha="center",
            va="center",
            color="white",
            fontsize=7,
            weight="bold",
        )

    # Draw directional links using the computed neighbors and the hex-logic wiring
    # (connections are computed in InfiniteCoherentGraph via topology_rules).
    for node in nodes:
        for _, target_ids in node.connections.items():
            for target_id in target_ids:
                target = nodes_by_id.get(target_id)
                if target is None:
                    continue
                ax.annotate(
                    "",
                    xy=(target.position.real, target.position.imag),
                    xytext=(node.position.real, node.position.imag),
                    arrowprops=dict(
                        arrowstyle="->",
                        color=netting_colors.get(node.netting, "#333333"),
                        alpha=0.35,
                        linewidth=0.9,
                    ),
                )

    ax.set_aspect('equal')
    plt.title('Infinite Coherent Grid: Hex cells + stream vectors')
    plt.grid(True, linestyle=':', alpha=0.3)
    plt.savefig('infinite_grid_viz.png', dpi=300)
    print("Visualization saved to infinite_grid_viz.png")

if __name__ == "__main__":
    visualize_infinite()
