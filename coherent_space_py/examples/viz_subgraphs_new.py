
from __future__ import annotations
import matplotlib.pyplot as plt
from matplotlib.patches import Polygon

from coherent_space_py.geometry import get_positions, polygon_points_in_angle_order

from coherent_space_py.model.enums import StreamProcessType, InformationalStreamVectorDirection
from coherent_space_py.model.topology_rules import get_upstream_connections, get_downstream_connections

VD = InformationalStreamVectorDirection
SAT = StreamProcessType


def abbrev(t: StreamProcessType) -> str:
    name = t.name
    caps = "".join([c for c in name if c.isupper()])
    return caps.replace("U", "").replace("D", "")


def draw_layer(ax, *, mode: str, vd, draw_background: bool = True, draw_labels: bool = True):
    positions = get_positions(mode=mode)
    up_nodes = {t: p for t, p in positions.items() if "Upstream" in t.name}
    down_nodes = {t: p for t, p in positions.items() if "Downstream" in t.name}

    if draw_background and up_nodes:
        ft_types = [SAT.UpstreamSelectorFunction, SAT.UpstreamConsumerFunction, SAT.UpstreamDetectorFunction]
        st_types = [SAT.UpstreamSelectorSystem, SAT.UpstreamConsumerSystem, SAT.UpstreamDetectorSystem]
        ft_pts = [up_nodes[t] for t in ft_types if t in up_nodes]
        st_pts = [up_nodes[t] for t in st_types if t in up_nodes]
        if len(ft_pts) == 3:
            ax.add_patch(Polygon(ft_pts, closed=True, fill=True, facecolor='lightblue', alpha=0.12,
                                 edgecolor='blue', linestyle='--'))
        if len(st_pts) == 3:
            ax.add_patch(Polygon(st_pts, closed=True, fill=True, facecolor='lightgreen', alpha=0.12,
                                 edgecolor='green', linestyle='--'))

    if draw_background and down_nodes:
        poly_pts = polygon_points_in_angle_order(list(down_nodes.values()))
        if len(poly_pts) >= 3:
            ax.add_patch(Polygon(poly_pts, closed=True, fill=True, facecolor='orange', alpha=0.10,
                                 edgecolor='orange', linestyle=':'))

    for t, (x, y) in up_nodes.items():
        color = 'green' if "System" in t.name else 'blue'
        ax.add_patch(plt.Circle((x, y), 0.08, color=color, zorder=3))
        ax.text(x, y, abbrev(t), ha='center', va='center', fontweight='bold',
                color='white', fontsize=8, zorder=4)
        if draw_labels:
            ax.text(x*1.22, y*1.22, t.name.split("stream")[1], ha='center', fontsize=7)

    for t, (x, y) in down_nodes.items():
        color = 'brown' if "System" in t.name else 'orange'
        ax.add_patch(plt.Circle((x, y), 0.07, color=color, zorder=3))
        ax.text(x, y, abbrev(t), ha='center', va='center', fontweight='bold',
                color='white', fontsize=8, zorder=4)
        if draw_labels:
            ax.text(x*1.25, y*1.25, t.name.split("stream")[1], ha='center', fontsize=7)

    def draw_arrows(node_map, get_targets_fn):
        for s_type, s_pos in node_map.items():
            for t_type in get_targets_fn(s_type, vd):
                if t_type in node_map:
                    ax.annotate(
                        "",
                        xy=node_map[t_type], xycoords='data',
                        xytext=s_pos, textcoords='data',
                        arrowprops=dict(arrowstyle="-|>", color="black", lw=2, mutation_scale=14),
                        zorder=5
                    )

    if mode in ("UP", "BOTH") and up_nodes:
        draw_arrows(up_nodes, get_upstream_connections)
    if mode in ("DOWN", "BOTH") and down_nodes:
        draw_arrows(down_nodes, get_downstream_connections)


def visualize_schematics(out_path: str = "hexagon_schematics_viz.png"):
    fig, axes = plt.subplots(3, 2, figsize=(16, 24))
    fig.suptitle('Hexavalent Logic Schematics (Upstream vertical + Downstream horizontal inner hex)', fontsize=16)

    configs = [
        {"title": "Upstream (CornerParity)", "mode": "UP", "vd": VD.CornerParity, "ax": axes[0, 0]},
        {"title": "Upstream (SideParity)",   "mode": "UP", "vd": VD.SideParity,   "ax": axes[0, 1]},
        {"title": "Downstream (CornerParity)", "mode": "DOWN", "vd": VD.CornerParity, "ax": axes[1, 0]},
        {"title": "Downstream (SideParity)",   "mode": "DOWN", "vd": VD.SideParity,   "ax": axes[1, 1]},
        {"title": "Superposition (CornerParity)", "mode": "BOTH", "vd": VD.CornerParity, "ax": axes[2, 0]},
        {"title": "Superposition (SideParity)",   "mode": "BOTH", "vd": VD.SideParity,   "ax": axes[2, 1]},
    ]

    for cfg in configs:
        ax = cfg["ax"]
        draw_layer(ax, mode=cfg["mode"], vd=cfg["vd"], draw_background=True, draw_labels=True)
        ax.set_title(cfg["title"])
        ax.set_aspect('equal')
        ax.axis('off')
        ax.set_xlim(-1.6, 1.6)
        ax.set_ylim(-1.6, 1.6)

    plt.tight_layout()
    plt.savefig(out_path, dpi=300)
    print(f"Visualization saved to {out_path}")
    return out_path


if __name__ == "__main__":
    visualize_schematics()
