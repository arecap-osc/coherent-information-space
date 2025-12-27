package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;
import java.util.stream.Collectors;

public class RoadEndNodeMediaLayer extends RoadSelectedNodesMediaLayer {
    public RoadEndNodeMediaLayer(String network, Graphics2dStyle graphics2dStyle) {
        super(network, graphics2dStyle);
    }

    @Override
    public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
        super.drawContent(graphics2d, mediaRendererTransform);
        //draw center

        Graphics2dStyle fillGraphics2dStyle = new Graphics2dStyle(ColorFactory.web("#666666"),
                getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());

        CoherentSpaceGraphics2DUtils.drawPoints(graphics2d,
                getSelectedIndex().stream().map(index -> new NodeBean(getNetwork(), index)).collect(Collectors.toList()),
                mediaRendererTransform, fillGraphics2dStyle, 0.5);
    }
}