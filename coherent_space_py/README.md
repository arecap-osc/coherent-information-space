# coherent_space_py

Reimplementation in Python of the semantic hexagonal "coherent space" used by the Java informational stream builders. The package provides direction-specific hex graph builders, geometry helpers based on the 12th roots of unity, Gauss-sum identity generation, and a simple example for generating an upstream edge grid.

## Requirements
- Python 3.10+
- `numpy`

Install dependencies locally:

```bash
python -m pip install -r requirements.txt
```

## Local usage

1. Ensure the repository root is on `PYTHONPATH` (or install the package in editable mode).
2. Run the example to generate a three-step upstream-edge grid and write it to JSON:

```bash
PYTHONPATH=. python coherent_space_py/examples/build_example_grid.py
```

This prints the node dictionary as JSON and writes it to `coherent_space_py/examples/example_grid.json`. Use `--output` to control the destination and `--step-count` to generate larger grids.

To build the full netting for all four link orientations in one shot and write it to JSON, use:

```bash
PYTHONPATH=. python coherent_space_py/examples/build_all_link_types_grid.py
```

The script prints a JSON object keyed by orientation (`upstream_edge`, `downstream_edge`, `upstream_vertex`, `downstream_vertex`) where each entry includes a `summary` of node/type counts and the complete node dictionaries. It also writes the same data to `coherent_space_py/examples/all_link_types_grid.json`. Use `--output` to control the destination and `--step-count` to generate larger grids.

### Suprastructură multivalentă (function/system)

Poți suprapune logică multivalentă peste aceleași noduri fizice. Constructorii hexagonali oferă un helper care clondează fiecare nod fizic în mai multe „straturi” logice (ex. `function`, `system`) și rotește ciclul selector/detector/consumer pentru fiecare strat, păstrând geometria și vecinii.

```python
from coherent_space_py import UpstreamEdgeHexGraphBuilder

builder = UpstreamEdgeHexGraphBuilder()
physical_nodes = builder.build_hex_grid(step_count=3)
logical_nodes = builder.build_multivalent_grid(step_count=3, logic_layers=("function", "system"))

print(physical_nodes[3].to_dict())  # nod fizic
print(logical_nodes[(3, "function")].to_dict())
print(logical_nodes[(3, "system")].to_dict())
```

`logic_role` din nodul multivalent arată rolul trivalent ajustat pentru stratul respectiv; poziția, orientarea și vecinii rămân identici cu nodul fizic. Ajustează `logic_layers` pentru a adăuga mai multe proiecții semantice peste aceeași rețea.

## Hugging Face Space setup

1. Create a new **Python** Space.
2. Add a `requirements.txt` containing:
   ```
   numpy
   ```
3. Add this repository to the Space (either by pushing the code or via a submodule).
4. In `app.py` (or your entry script), ensure the repo root is on `PYTHONPATH` before using the builders:
   ```python
   import os, sys
   sys.path.append(os.path.dirname(__file__))
   from coherent_space_py import UpstreamEdgeHexGraphBuilder

   def main():
       nodes = UpstreamEdgeHexGraphBuilder().build_hex_grid(step_count=3)
       print(nodes[0].to_dict())

   if __name__ == "__main__":
       main()
   ```
5. Set the Space **Startup command** to run your script, e.g. `python app.py`.

## Google Colab usage

In a Colab notebook cell:

```python
import sys, subprocess, os

# Clone or pull the repository
if not os.path.exists("coherent-information-space"):
    subprocess.run(["git", "clone", "https://github.com/<your-org>/coherent-information-space.git"], check=True)

%cd coherent-information-space

# Install dependencies
%pip install -r requirements.txt

# Make the package importable
import sys, os
sys.path.append(os.getcwd())

from coherent_space_py import UpstreamEdgeHexGraphBuilder

nodes = UpstreamEdgeHexGraphBuilder().build_hex_grid(step_count=3)
print(list(nodes.keys())[:5])
```

Replace the repository URL with the location of your fork if needed. Adjust `step_count` to explore larger graphs.
