package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSelectionMediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.ResetableMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MarkedEdgesMediaLayer extends GgmSelectionMediaRendererLayer implements ResetableMediaLayer {

    private List<EdgeBean> edges = new ArrayList<>();

    public MarkedEdgesMediaLayer(String network, ConfigurableGraphics2dStyle mediaRenderStyle) {
        super(network, mediaRenderStyle);
    }

    public void processEdgeBean(EdgeBean nodeBean) {
        if (!edges.remove(nodeBean)) {
            edges.add(nodeBean);
        }
    }

    @Override
    public void clear() {
        edges = new ArrayList<>();
        setNeedRefresh(true);
    }

    @Override
    public void drawContent(Graphics2D graphics2d, MediaRendererTransform mediaRendererTransform) {
        for (EdgeBean edge : edges) {
            CoherentSpaceGraphics2DUtils
                    .drawLine(graphics2d, edge, mediaRendererTransform, getMediaRenderStyle());
        }
    }
}
