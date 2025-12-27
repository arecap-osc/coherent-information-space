package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;

public class GgmSecondNodeMediaLayer extends GgmSelectionMediaRendererLayer {

    private Long endNodeIndex;

    public GgmSecondNodeMediaLayer(String network, ConfigurableGraphics2dStyle mediaRenderStyle) {
        super(network, mediaRenderStyle);
    }

    public void setEndNodeIndex(Long endNodeIndex) {
        this.endNodeIndex = endNodeIndex;
    }

    public Long getEndNodeIndex() {
        return endNodeIndex;
    }

    @Override
    public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
        if (getEndNodeIndex() != null) {
            Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(ColorFactory.web("#666666"),
                    getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
            CoherentSpaceGraphics2DUtils
                    .drawPoint(graphics2d, new NodeBean(getNetwork(), endNodeIndex),
                            mediaRendererTransform, mediaRendererStyle, 2.2);
            CoherentSpaceGraphics2DUtils
                    .drawPoint(graphics2d, new NodeBean(getNetwork(), endNodeIndex),
                            mediaRendererTransform, getMediaRenderStyle(), 1.8);


            CoherentSpaceGraphics2DUtils
                    .drawPoint(graphics2d, new NodeBean(getNetwork(), endNodeIndex),
                            mediaRendererTransform, mediaRendererStyle, 0.5);

            //TODO: ne trebuie firstNodeIndex daca vrem si sageata
        }

    }

}
