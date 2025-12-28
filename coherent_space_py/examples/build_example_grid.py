from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

from coherent_space_py import UpstreamEdgeHexGraphBuilder


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate an upstream-edge hex grid and export it as JSON."
    )
    parser.add_argument(
        "--step-count",
        type=int,
        default=3,
        help="Number of steps from the origin to include in the grid (default: 3).",
    )
    parser.add_argument(
        "--output",
        type=Path,
        default=Path(__file__).with_name("example_grid.json"),
        help="Path to write the JSON output (default: examples/example_grid.json).",
    )
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    builder = UpstreamEdgeHexGraphBuilder()
    nodes = builder.build_hex_grid(step_count=args.step_count)
    graph_dict = {node_id: node.to_dict() for node_id, node in nodes.items()}
    json_str = json.dumps(graph_dict, indent=2, sort_keys=True)

    print(json_str)
    args.output.write_text(json_str + "\n")
    print(f"Wrote JSON to {args.output.resolve()}", file=sys.stderr)


if __name__ == "__main__":
    main()
