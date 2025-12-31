# informationalstream_graph (Python)

Port in Python of the Java **InformationalStreamDoubleRangeIntegerIdentityGraphBuilder** + the 4 netting builders.

## What you get

- Deterministic generation of nodes (ID + complex-plane position) for:
  - UpstreamEdge
  - DownstreamEdge
  - UpstreamVertex
  - DownstreamVertex
- View-window sampling (so the infinite graph is generated only for what you want to render)
- A simple matplotlib visualizer (`render_demo.py`)

## Quickstart

```bash
python -m venv .venv
source .venv/bin/activate
pip install matplotlib

python render_demo.py
```

## Notes

- The original Java builder doesn’t explicitly compute *neighbors*; it places nodes deterministically.  
  The demo infers neighbors geometrically (`infer_neighbors`) just for visualization.
- If you want exact neighbor rules identical to your Java side, we can port the exact topology/linking logic too (it lives in the graph-properties factories and application-stream direction rules).
