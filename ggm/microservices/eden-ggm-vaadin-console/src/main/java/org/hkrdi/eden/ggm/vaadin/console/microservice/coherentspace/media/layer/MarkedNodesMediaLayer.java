package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSelectionMediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.ResetableMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MarkedNodesMediaLayer extends GgmSelectionMediaRendererLayer implements ResetableMediaLayer {

    private List<NodeBean> nodes = new ArrayList<>();

    public MarkedNodesMediaLayer(String network, ConfigurableGraphics2dStyle mediaRenderStyle) {
        super(network, mediaRenderStyle);
    }

    public void processNodeBean(NodeBean nodeBean) {
        if(!nodes.remove(nodeBean) ) {
            nodes.add(nodeBean);
        }
    }

    @Override
    public void clear() {
        nodes = new ArrayList<>();
        setNeedRefresh(true);
    }

    @Override
    public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
        if (nodes.size() > 0) {
            Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(ColorFactory.web("#666666"),
                    getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
            CoherentSpaceGraphics2DUtils
                    .drawPoints(graphics2d, nodes,
                            mediaRendererTransform, mediaRendererStyle, 2.2);
            CoherentSpaceGraphics2DUtils
                    .drawPoints(graphics2d, nodes,
                            mediaRendererTransform, getMediaRenderStyle(), 1.8);

        }

    }

}
