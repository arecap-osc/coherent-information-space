from __future__ import annotations

import json

from coherent_space_py import UpstreamEdgeHexGraphBuilder


def main() -> None:
    builder = UpstreamEdgeHexGraphBuilder()
    nodes = builder.build_hex_grid(step_count=3)
    graph_dict = {node_id: node.to_dict() for node_id, node in nodes.items()}
    print(json.dumps(graph_dict, indent=2, sort_keys=True))


if __name__ == "__main__":
    main()
