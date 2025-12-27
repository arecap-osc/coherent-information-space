package org.arecap.eden.ia.console.microservice.graph.media;

import org.arecap.eden.ia.console.media.ConfigurableGraphics2dStyle;
import org.arecap.eden.ia.console.media.MediaLayer;
import org.arecap.eden.ia.console.media.MediaRenderStyle;
import org.arecap.eden.ia.console.media.MediaRendererLayer;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphMediaLayer implements MediaLayer, ConfigurableGraphics2dStyle {

    private String name;

    private List<MediaRendererLayer> mediaRendererLayers = new ArrayList<>();

    private Color color = ColorFactory.web("#6b89d8");
    private Double opacity = 1d;
    private Double penStroke = 4.4d;
    private Boolean visible = false;

    private Font font = new Font("ARIAL", Font.BOLD, 26);

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
        return mediaRendererLayers;
    }

    @Override
    public MediaRenderStyle getMediaRenderStyle() {
        return this;
    }

    @Override
    public Color getColor() {
        return this.color;
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
        return font;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

}
