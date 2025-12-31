from informational_stream import ComplexPlane, build_all_nettings, plot_graph

if __name__ == "__main__":
    # mirrors your Java call:
    # getNettingGraphs(600D, step, scale, origin=(0,0), displayBottomRight=(w/2, -h/2))
    stream_distance = 600.0
    scale = 1.0
    origin = ComplexPlane(0.0, 0.0)

    # pick something similar to your screen rectangle. Example:
    display_bottom_right = ComplexPlane(600.0, -400.0)

    graphs = {}
    for step in range(1, 2):
        graphs.update(build_all_nettings(stream_distance, step, scale, origin, display_bottom_right))

    plot_graph(graphs, show_internal_vectors=True, show_labels=False)
