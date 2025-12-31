"""Render a single informational-stream netting graph.

Usage examples:
  # default: upstream + edge, step 0, stream_distance 3, scale 1, 50x30 window
  python render_single_netting.py

  # downstream + vertex on level 2, custom stream distance and zoom
  python render_single_netting.py --hexagon downstream --connection vertex --step 2 --stream-distance 4 --scale 0.8 --width 60 --height 36
"""

from __future__ import annotations

import argparse
import os

import matplotlib

matplotlib.use("Agg")  # headless-friendly
import matplotlib.pyplot as plt
from matplotlib.figure import Figure

from informationalstream_graph import (
    ComplexPlane,
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy,
    InformationalStreamNetting,
    infer_neighbors,
)


def build_netting(hexagon: str, connection: str) -> InformationalStreamNetting:
    hexagon_norm = hexagon.lower()
    connection_norm = connection.lower()

    if hexagon_norm not in ("upstream", "downstream"):
        raise ValueError("hexagon must be 'upstream' or 'downstream'")
    if connection_norm not in ("edge", "vertex"):
        raise ValueError("connection must be 'edge' or 'vertex'")

    if hexagon_norm == "upstream" and connection_norm == "edge":
        return InformationalStreamNetting.UpstreamEdge
    if hexagon_norm == "upstream":
        return InformationalStreamNetting.UpstreamVertex
    if connection_norm == "edge":
        return InformationalStreamNetting.DownstreamEdge
    return InformationalStreamNetting.DownstreamVertex


def plot_graph(nodes, title: str, with_edges: bool = True) -> Figure:
    xs = []
    ys = []
    for n in nodes.values():
        xs.append(n.position.real)
        ys.append(n.position.imaginary)

    fig, ax = plt.subplots(figsize=(8, 6))
    ax.scatter(xs, ys, s=12)

    if with_edges:
        for n in nodes.values():
            for nb in n.neighbors:
                if nb in nodes and nb > n.id:
                    a = n.position
                    b = nodes[nb].position
                    ax.plot([a.real, b.real], [a.imaginary, b.imaginary], linewidth=0.5, alpha=0.4, color="gray")

    ax.set_title(title)
    ax.set_aspect("equal", adjustable="box")
    ax.grid(True, linestyle="--", alpha=0.3)
    plt.tight_layout()
    return fig


def main():
    parser = argparse.ArgumentParser(description="Render one informational-stream netting graph.")
    parser.add_argument("--hexagon", default="upstream", help="Hexagon type: upstream | downstream (default: upstream)")
    parser.add_argument("--connection", default="edge", help="Connection type: edge | vertex (default: edge)")
    parser.add_argument("--stream-distance", type=float, default=3.0, help="Base stream distance (default: 3.0)")
    parser.add_argument("--step", type=int, default=0, help="Level/step (default: 0)")
    parser.add_argument("--scale", type=float, default=1.0, help="Zoom multiplier (default: 1.0)")
    parser.add_argument("--origin-x", type=float, default=0.0, help="Origin real coordinate (default: 0.0)")
    parser.add_argument("--origin-y", type=float, default=0.0, help="Origin imaginary coordinate (default: 0.0)")
    parser.add_argument("--width", type=float, default=50.0, help="Window width (default: 50.0)")
    parser.add_argument("--height", type=float, default=30.0, help="Window height (default: 30.0)")
    parser.add_argument("--with-edges", action="store_true", help="Draw inferred neighbor edges")
    parser.add_argument("--output", default="single_netting.png", help="Path to save the image (default: single_netting.png)")
    parser.add_argument("--show", action="store_true", help="Display the interactive window in addition to saving the image.")
    args = parser.parse_args()

    netting = build_netting(args.hexagon, args.connection)

    builder = InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy()

    origin = ComplexPlane(args.origin_x, args.origin_y)
    display_bottom_right = ComplexPlane(args.width / 2.0, -args.height / 2.0)

    nodes = builder.get_graph(
        netting=netting,
        stream_distance=args.stream_distance,
        step=args.step,
        scale=args.scale,
        origin=origin,
        display_bottom_right=display_bottom_right,
    )

    if args.with_edges:
        infer_neighbors(nodes)

    title = f"{netting.value} | step={args.step}, stream_distance={args.stream_distance}, scale={args.scale}"
    fig = plot_graph(nodes, title, with_edges=args.with_edges)
    fig.savefig(args.output, dpi=300)
    print(f"Saved netting visualization to {os.path.abspath(args.output)}")
    if args.show:
        plt.show()


if __name__ == "__main__":
    main()
