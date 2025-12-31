"""Demo: generate and visualize the four netting graphs.

Run:
  python render_demo.py

You can tweak the window and parameters below.
"""

from __future__ import annotations

import argparse
import os

import matplotlib

matplotlib.use("Agg")  # headless-friendly
import matplotlib.pyplot as plt

from informationalstream_graph import (
    ComplexPlane,
    InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy,
    infer_neighbors,
    StreamApplicationType,
)


def plot_graph(ax, nodes, title: str, with_edges: bool = True):
    # group points by app_type
    xs = {t: [] for t in StreamApplicationType}
    ys = {t: [] for t in StreamApplicationType}
    for n in nodes.values():
        xs[n.app_type].append(n.position.real)
        ys[n.app_type].append(n.position.imaginary)

    for t in StreamApplicationType:
        ax.scatter(xs[t], ys[t], s=12, label=t.value)

    if with_edges:
        for n in nodes.values():
            for nb in n.neighbors:
                if nb in nodes and nb > n.id:
                    a = n.position
                    b = nodes[nb].position
                    ax.plot([a.real, b.real], [a.imaginary, b.imaginary], linewidth=0.5, alpha=0.4)

    ax.set_title(title)
    ax.set_aspect("equal", adjustable="box")
    ax.legend(loc="upper right")


def main():
    parser = argparse.ArgumentParser(description="Render all four informational-stream nettings.")
    parser.add_argument(
        "--output",
        default="render_demo.png",
        help="Path to save the generated visualization (default: render_demo.png)",
    )
    parser.add_argument(
        "--show",
        action="store_true",
        help="Display the interactive window in addition to saving the image.",
    )
    args = parser.parse_args()

    builder = InformationalStreamDoubleRangeIntegerIdentityGraphBuilderPy()

    # Parameters similar to Java call
    stream_distance = 3.0
    step = 0
    scale = 1.0

    origin = ComplexPlane(0.0, 0.0)
    # IMPORTANT: keep imaginary negative (same convention as your Java code where
    # bottom-right is ( +W/2, -H/2 ) ).
    display_bottom_right = ComplexPlane(25.0, -15.0)

    graphs = builder.get_netting_graphs(stream_distance, step, scale, origin, display_bottom_right)

    fig, axes = plt.subplots(2, 2, figsize=(12, 10))
    axes = axes.ravel()

    titles = ["UpstreamEdge", "DownstreamEdge", "UpstreamVertex", "DownstreamVertex"]

    for ax, g, title in zip(axes, graphs, titles):
        infer_neighbors(g)
        plot_graph(ax, g, title)

    plt.tight_layout()
    plt.savefig(args.output, dpi=300)
    print(f"Saved demo visualization to {os.path.abspath(args.output)}")
    if args.show:
        plt.show()


if __name__ == "__main__":
    main()
