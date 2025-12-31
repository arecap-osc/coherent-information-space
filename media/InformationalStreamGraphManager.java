package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.media.MediaRenderLayerFactory;
import org.arecap.eden.ia.console.media.MediaRendererLayer;

import java.util.Arrays;
import java.util.List;

public class InformationalStreamGraphManager implements MediaRenderLayerFactory {

    private final static InformationalStreamInformationGraph informationGraph = new InformationalStreamInformationGraph();

    private final static InformationalStreamCurveGraph curveGraph = new InformationalStreamCurveGraph();

    private final static InformationalStreamPointGraph pointsGraph = new InformationalStreamPointGraph();

    private final static AnimatedCurveInformationalStreamGraph animatedGraph = new AnimatedCurveInformationalStreamGraph();

    private final static List<MediaRendererLayer> informationalStreamGraphMediaRenders = Arrays.asList(informationGraph, pointsGraph, animatedGraph, curveGraph);

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        return  informationalStreamGraphMediaRenders;
    }
}
