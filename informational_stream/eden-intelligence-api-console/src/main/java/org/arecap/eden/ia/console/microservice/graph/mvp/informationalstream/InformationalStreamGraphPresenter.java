package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream;


import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.support.graph.builder.InformationalStreamDoubleRangeIntegerIdentityGraphBuilder;
import org.arecap.eden.ia.console.media.MediaRenderLayerFactory;
import org.arecap.eden.ia.console.media.mvp.MediaRenderImagePresenter;
import org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media.InformationalStreamGraph;
import org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media.InformationalStreamGraphManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringComponent
@UIScope
public class InformationalStreamGraphPresenter extends MediaRenderImagePresenter<InformationalStreamGraphView> {


    @Autowired
    private InformationalStreamDoubleRangeIntegerIdentityGraphBuilder graphBuilder;

    private  List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> graph = new ArrayList<>();

    private InformationalStreamGraphManager informationalStreamGraphMedia = new InformationalStreamGraphManager();

    @Override
    public MediaRenderLayerFactory getMediaRendererLayerFactory() {
        return informationalStreamGraphMedia;
    }

    @Override
    public void composeOrRefreshImages(Boolean forceRefresh) {
        Double screenW = getScreenProperties().getWidth();
        Double screenH = getScreenProperties().getHeight();

        Double offsetX = (screenW - 800) / 2;
        Double offsetY = (screenH - 800) / 2;

        graph.clear();
        for(int step= 1; step <= 6; step ++) {
            graph.addAll(graphBuilder
                    .getNettingGraphs(600D, step, getScreenProperties().getScale(),
                            new ComplexPlane(0D, 0D), new ComplexPlane(400d, -400d)));
        }
        informationalStreamGraphMedia.getMediaRendererLayers().stream()
                .filter(mrl -> InformationalStreamGraph.class.isAssignableFrom(mrl.getClass()))
                .map(mrl -> (InformationalStreamGraph)mrl)
                .forEach(isg -> isg.setGraph(graph));
        super.composeOrRefreshImages(forceRefresh);
    }
}
