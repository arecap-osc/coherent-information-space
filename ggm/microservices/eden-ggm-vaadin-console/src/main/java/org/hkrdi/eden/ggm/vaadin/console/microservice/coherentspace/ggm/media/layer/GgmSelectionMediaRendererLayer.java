package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;

public abstract class GgmSelectionMediaRendererLayer implements PNGMediaRendererDrawer, NetworkMediaLayerName, MediaRenderStyleAware, HasMediaRenderStyle {

    private String network;

    private Boolean needRefresh = false;
    private ConfigurableGraphics2dStyle graphics2dStyle;

    public GgmSelectionMediaRendererLayer(String network, ConfigurableGraphics2dStyle graphics2dStyle) {
        this.network = network;
        this.graphics2dStyle = graphics2dStyle;
    }

    @Override
    public void setMediaRenderStyle(MediaRenderStyle mediaRenderStyle) {
        this.graphics2dStyle.setPenStroke(((ConfigurableGraphics2dStyle) mediaRenderStyle).getPenStroke());
        this.graphics2dStyle.setOpacity(((ConfigurableGraphics2dStyle) mediaRenderStyle).getOpacity());
    }

    @Override
    public Boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
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
    public ConfigurableGraphics2dStyle getMediaRenderStyle() {
        return graphics2dStyle;
    }
}
