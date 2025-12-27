package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;

import java.util.ArrayList;
import java.util.List;

public class SemanticMapMediaLayer implements MediaLayer {

    private final String name;

    private final Boolean selectable;

    private Boolean visible = true;

    private final List<MediaRendererLayer> layers = new ArrayList<>();


    public SemanticMapMediaLayer(String name, Boolean selectable) {
        this.name = name;
        this.selectable = selectable;
    }

    @Override
    public String getName() {
        return name;
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
        return layers;
    }

    @Override
    public MediaRenderStyle getMediaRenderStyle() {
        //N/A
        return null;
    }

    public Boolean hasSelectable() {
        return selectable;
    }
}
