package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.media.MediaRendererLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class InformationalStreamGraph implements MediaRendererLayer {

    private Boolean needRefresh = false;

    @Override
    public Boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    private List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> graph = new ArrayList<>();


    public void setGraph(List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> graph) {
        this.graph = graph;
    }

    public List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> getGraph() {
        return graph;
    }
}
