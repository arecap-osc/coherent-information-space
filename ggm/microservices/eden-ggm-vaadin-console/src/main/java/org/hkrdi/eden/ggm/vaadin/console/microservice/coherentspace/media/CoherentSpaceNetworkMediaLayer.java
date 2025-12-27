package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CoherentSpaceNetworkMediaLayer implements MediaLayer, NetworkMediaLayerStyle {

    private String network;

    private List<MediaRendererLayer> mediaRendererLayers = new ArrayList<>();

    private Color color = ColorFactory.web("#6b89d8");
    private Double opacity = 1d;
    private Double penStroke = 4.4d;
    private Boolean visible = false;
    private Boolean addressIndexVisible = false;
    private Boolean clusterIndexVisible = true;
    private Boolean trivalentLogicVisible = false;
    private Boolean applicationDataVisible = false;

    public CoherentSpaceNetworkMediaLayer(String network, Color color) {
        this.network = network;
        this.color = color;
    }
    
    public CoherentSpaceNetworkMediaLayer(String network) {
        this.network = network;
    }

    public CoherentSpaceNetworkMediaLayer() {
    }

    @Override
    public String getName() {
        return network;
    }

    @Override
    public Boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        return mediaRendererLayers;
    }

    @Override
    public NetworkMediaLayerStyle getMediaRenderStyle() {
        return this;
    }

    @Override
    public Boolean isApplicationDataVisible() {
        return applicationDataVisible;
    }

    @Override
    public void setApplicationDataVisible(Boolean applicationDataVisible) {
        this.applicationDataVisible = applicationDataVisible;
    }

    @Override
    public Boolean isAddressIndexVisible() {
        return addressIndexVisible;
    }

    @Override
    public void setAddressIndexVisible(Boolean addressIndexVisible) {
        this.addressIndexVisible = addressIndexVisible;
    }

    @Override
    public Boolean isClusterIndexVisible() {
        return clusterIndexVisible;
    }

    @Override
    public void setClusterIndexVisible(Boolean clusterIndexVisible) {
        this.clusterIndexVisible = clusterIndexVisible;
    }

    @Override
    public Boolean isTrivalentLogicVisible() {
        return trivalentLogicVisible;
    }

    @Override
    public void setTrivalentLogicVisible(Boolean trivalentLogicVisible) {
        this.trivalentLogicVisible = trivalentLogicVisible;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Double getOpacity() {
        return opacity;
    }

    @Override
    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    @Override
    public Double getPenStroke() {
        return penStroke;
    }

    @Override
    public void setPenStroke(Double penStroke) {
        this.penStroke = penStroke;
    }

    @Override
    public Font getFont() {
        //TODO
        return new Font("ARIAL", Font.BOLD, 26);
    }

    @Override
    public void setFont(Font font) {
        //TODO
    }
}
