package org.hkrdi.eden.ggm.vaadin.console.microservice.common;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.MarkedNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

public interface MarkedSelectionProcessor extends StateSelectionProcessor {

    MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network);

    MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network);

    default void processNodeMarkerSelection(NodeBean nodeBean) {
        MarkedNodesMediaLayer mediaLayer = getMarkedNodesMediaLayer(nodeBean.getNetwork());
        mediaLayer.processNodeBean(nodeBean);
        mediaLayer.setNeedRefresh(true);
    }

    default void processEdgeMarkerSelection(EdgeBean edge) {
        MarkedEdgesMediaLayer mediaLayer = getMarkedEdgesMediaLayer(edge.getFromNode().getNetwork());
        mediaLayer.processEdgeBean(edge);
        mediaLayer.setNeedRefresh(true);
    }
}
