# coherent-information-space
Global Governance Model Intelligence Api

## Informational stream

### Eden Intelligence API Console
- Vaadin Flow console that exposes the informational stream visuals and experiments alongside the existing governance tooling. Use the graph selector to switch between the informational stream lattice and the Mandelbrot set renderer while keeping the display controls in the same workspace.
- The informational stream graph builds 6 steps of four nettings (upstream/downstream, edge/vertex), yielding 24 stacked levels rendered from complex-plane coordinates. The positions rely on 12th roots of unity and Gauss-based spacing so the discontinuous fractalic function described in the theory document is preserved in the visualization.
- Upstream vectors (anabolic) and downstream vectors (catabolic) keep their selector/detector/consumer polarity through the topology helpers, so the rendered layers highlight how energy/information flows across the stream application graph.

### Graph rendering layers
- A media manager draws multiple layers for the informational stream: labeled nodes, colored neuron-like points for upstream/downstream function and system tracks, animated/curved trajectories, and straight-line overlays. This makes it easier to recognize selector→consumer directionality and the upstream/downstream split at each fractal step.
- The Mandelbrot view stays available as a companion fractal reference; it runs the classic iteration loop and colors each pixel by iteration depth to benchmark the coherent-space renderer against a known complex system.

### Natural Language Driven Systems
- Dedicated NL routes expose CRUD-style views for signals, feature streams, and stream applications, enabling you to describe and relate upstream/downstream semantics in natural language. Signal management supports congruence/similarity links, localization, and stream application typing so the NL layer can feed the graph topology without hand-coding nodes.
- Use the accordion layouts under `/nl/signal` and `/nl/feature` to browse signals and feature streams side by side; `/nl/stream-application` reserves space for wiring the natural language descriptions into concrete stream applications.

### Theory reference
- The folder includes `IS-De la fractali algebrici la fluxuri informationale-261225-110317.pdf`, which outlines the algebraic fractal approach behind the informational stream. The code above anchors that theory in the console through complex-plane netting, upstream/downstream topology, and fractal graph traversal.
