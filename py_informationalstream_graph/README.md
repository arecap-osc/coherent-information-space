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

# Generates render_demo.png in the current folder
python render_demo.py
```

## Single-netting demo

If you want to inspect one netting at a time (pick upstream/downstream + edge/vertex, level/step, stream distance, scale, and window):

```bash
# Saves single_netting.png by default
python render_single_netting.py --hexagon upstream --connection edge --step 0 --stream-distance 3 --scale 1 --width 50 --height 30 --with-edges
```

Both scripts are headless-friendly: they always write a PNG and print the absolute
path. Use `--output` to change the filename and `--show` if you also want an
interactive window (when a display is available).

## Notes

- The original Java builder doesn’t explicitly compute *neighbors*; it places nodes deterministically.  
  The demo infers neighbors geometrically (`infer_neighbors`) just for visualization.
- If you want exact neighbor rules identical to your Java side, we can port the exact topology/linking logic too (it lives in the graph-properties factories and application-stream direction rules).
