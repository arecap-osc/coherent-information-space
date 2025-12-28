from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path
from typing import Dict, Tuple

from coherent_space_py import (
    DownstreamEdgeHexGraphBuilder,
    DownstreamVertexHexGraphBuilder,
    UpstreamEdgeHexGraphBuilder,
    UpstreamVertexHexGraphBuilder,
)


def build_all_orientations(step_count: int = 3) -> Dict[str, dict]:
    """Generate grids for all four link orientations in one pass.

    Returns a dictionary keyed by orientation name. Each entry contains a
    summary (node count and per-type counts) and the full node dictionary.
    """

    def as_serializable(nodes) -> Tuple[int, dict]:
        type_counts: Dict[str, int] = {}
        for node in nodes.values():
            type_counts[node.type] = type_counts.get(node.type, 0) + 1
        return len(nodes), type_counts

    builders = {
        "upstream_edge": UpstreamEdgeHexGraphBuilder(),
        "downstream_edge": DownstreamEdgeHexGraphBuilder(),
        "upstream_vertex": UpstreamVertexHexGraphBuilder(),
        "downstream_vertex": DownstreamVertexHexGraphBuilder(),
    }

    output: Dict[str, dict] = {}
    for orientation, builder in builders.items():
        nodes = builder.build_hex_grid(step_count=step_count)
        node_count, type_counts = as_serializable(nodes)
        output[orientation] = {
            "summary": {"node_count": node_count, "type_counts": type_counts},
            "nodes": {node_id: node.to_dict() for node_id, node in nodes.items()},
        }

    return output


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate grids for all link orientations and export as JSON."
    )
    parser.add_argument(
        "--step-count",
        type=int,
        default=3,
        help="Number of steps from the origin to include in each grid (default: 3).",
    )
    parser.add_argument(
        "--output",
        type=Path,
        default=Path(__file__).with_name("all_link_types_grid.json"),
        help="Path to write the JSON output (default: examples/all_link_types_grid.json).",
    )
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    grid = build_all_orientations(step_count=args.step_count)
    json_str = json.dumps(grid, indent=2, sort_keys=True)

    print(json_str)
    args.output.write_text(json_str + "\n")
    print(f"Wrote JSON to {args.output.resolve()}", file=sys.stderr)


if __name__ == "__main__":
    main()
