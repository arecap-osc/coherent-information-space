"""Render the composed informational-stream graph across multiple steps.

Example (mirrors the Java presenter loop with steps 1..4):

  python -m informationalstream_py_app.render_presenter \
    --width 1200 --height 800 --scale 1.0 --stream-distance 600 \
    --max-step 4 --output composed_steps.png

"""

from __future__ import annotations

import argparse
import os
from typing import Dict, Iterable, Tuple

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt

from py_informationalstream_graph.informationalstream_graph import InformationalStreamNetting

from informationalstream_py_app.presenter import (
    GraphComposition,
    InformationalStreamGraphPresenterPy,
    PresenterNodeKey,
    ScreenProperties,
)


def _build_steps(args: argparse.Namespace) -> Iterable[int]:
    if args.steps:
        return args.steps
    return range(args.min_step, args.max_step + 1)


def _color_by_netting(netting: InformationalStreamNetting) -> str:
    return {
        InformationalStreamNetting.UpstreamEdge: "#1f77b4",
        InformationalStreamNetting.DownstreamEdge: "#d62728",
        InformationalStreamNetting.UpstreamVertex: "#2ca02c",
        InformationalStreamNetting.DownstreamVertex: "#ff7f0e",
    }[netting]


def _marker_by_step(step: int) -> str:
    markers = ["o", "s", "^", "D", "P", "X"]
    return markers[(step - 1) % len(markers)]


def _draw_edges(ax, composition: GraphComposition, alpha: float = 0.35) -> None:
    seen: set[Tuple[PresenterNodeKey, PresenterNodeKey]] = set()
    for node in composition.nodes.values():
        for nb_key in node.neighbors:
            pair = tuple(sorted((node.key, nb_key)))
            if pair in seen:
                continue
            seen.add(pair)
            nb = composition.nodes.get(nb_key)
            if nb is None:
                continue
            ax.plot(
                [node.position.real, nb.position.real],
                [node.position.imaginary, nb.position.imaginary],
                color="gray",
                linewidth=0.4,
                alpha=alpha,
            )


def _draw_nodes(ax, composition: GraphComposition) -> None:
    buckets: Dict[Tuple[InformationalStreamNetting, int], Tuple[list[float], list[float]]] = {}
    for node in composition.nodes.values():
        key = (node.key.netting, node.key.step)
        buckets.setdefault(key, ([], []))
        buckets[key][0].append(node.position.real)
        buckets[key][1].append(node.position.imaginary)

    for (netting, step), (xs, ys) in sorted(buckets.items(), key=lambda item: item[0][1]):
        ax.scatter(
            xs,
            ys,
            s=12,
            marker=_marker_by_step(step),
            c=_color_by_netting(netting),
            alpha=0.9,
            label=f"{netting.value} · step {step}",
        )


def main() -> None:
    parser = argparse.ArgumentParser(description="Compose and render informational-stream graphs across steps.")
    parser.add_argument("--width", type=float, default=1200.0, help="Screen width (same as Java presenter).")
    parser.add_argument("--height", type=float, default=800.0, help="Screen height (same as Java presenter).")
    parser.add_argument("--scale", type=float, default=1.0, help="Scale factor used by the builder.")
    parser.add_argument("--stream-distance", type=float, default=600.0, help="Stream distance passed to the builder.")
    parser.add_argument("--min-step", type=int, default=1, help="First step to render (inclusive).")
    parser.add_argument("--max-step", type=int, default=4, help="Last step to render (inclusive).")
    parser.add_argument(
        "--steps",
        type=int,
        nargs="+",
        help="Explicit list of steps. Overrides min/max if provided.",
    )
    parser.add_argument(
        "--no-edges",
        dest="with_edges",
        action="store_false",
        help="Skip neighbor inference/edge drawing for faster renders.",
    )
    parser.add_argument(
        "--output",
        default="informational_stream_composed.png",
        help="Where to save the PNG (default: informational_stream_composed.png)",
    )
    parser.add_argument(
        "--show",
        action="store_true",
        help="Also open an interactive matplotlib window if a display is available.",
    )
    parser.add_argument(
        "--invert-y",
        action="store_true",
        help="Flip the Y axis to mirror the Java screen coordinates (origin at top-left).",
    )
    args = parser.parse_args()

    screen = ScreenProperties(width=args.width, height=args.height, scale=args.scale)
    presenter = InformationalStreamGraphPresenterPy(screen=screen, stream_distance=args.stream_distance)

    composition = presenter.compose(steps=_build_steps(args), infer_topology=args.with_edges)

    fig, ax = plt.subplots(figsize=(12, 8))
    _draw_nodes(ax, composition)
    if args.with_edges:
        _draw_edges(ax, composition)

    ax.set_aspect("equal", adjustable="box")
    ax.set_xlim(-args.width / 2.0, args.width / 2.0)
    ax.set_ylim(-args.height / 2.0, args.height / 2.0)
    if args.invert_y:
        ax.invert_yaxis()
    ax.set_title("InformationalStreamGraphPresenterPy · composed nettings across steps")
    ax.legend(loc="upper right", ncol=2, fontsize=8)
    ax.grid(False)

    plt.tight_layout()
    plt.savefig(args.output, dpi=300)
    print(f"Saved composition to {os.path.abspath(args.output)}")
    if args.show:
        plt.show()

    counts_netting = composition.counts_by_netting()
    counts_step = composition.counts_by_step()
    print("Nodes by netting:")
    for netting, count in counts_netting.items():
        print(f"  {netting.value}: {count}")
    print("Nodes by step:")
    for step, count in sorted(counts_step.items()):
        print(f"  step {step}: {count}")


if __name__ == "__main__":
    main()
