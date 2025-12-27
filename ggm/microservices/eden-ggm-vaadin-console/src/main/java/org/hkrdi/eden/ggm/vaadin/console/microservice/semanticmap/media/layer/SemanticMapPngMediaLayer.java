package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.PNGMediaRendererDrawer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapServiceFactory;

public  abstract class SemanticMapPngMediaLayer implements SemanticMapServiceFactory, MediaRendererLayer, NetworkMediaLayerName, NetworkMediaLayer, PNGMediaRendererDrawer {

    private String network;

    private Boolean needRefresh = false;

    public SemanticMapPngMediaLayer(String network) {
        this.network = network;
    }

    @Override
    public String getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public Boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
    }


}
