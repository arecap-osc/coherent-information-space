from __future__ import annotations

import json
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


def main() -> None:
    grid = build_all_orientations(step_count=3)
    print(json.dumps(grid, indent=2, sort_keys=True))


if __name__ == "__main__":
    main()
