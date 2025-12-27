package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSelectionMediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.springframework.cop.support.BeanUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InformationEdgesMediaLayer extends GgmSelectionMediaRendererLayer {
    private List<EdgeBean> edges = new ArrayList<>();
    private Long applicationId;
    private Color color;

    public InformationEdgesMediaLayer(String network, NetworkMediaLayerStyle graphics2dStyle, Color color, Long applicationId) {
        super(network, graphics2dStyle);
        this.color = color;
        this.applicationId = applicationId;
        if (applicationId != null) {
            edges = BeanUtil.getBean(CoherentSpaceService.class).getInformationContainedNetworkEdgeBeans(network, applicationId);
        }
    }

    public void setApplicationId(Long applicationId) {
        if (applicationId == null) {
            return;
        }
        this.applicationId = applicationId;
        edges = BeanUtil.getBean(CoherentSpaceService.class).getInformationContainedNetworkEdgeBeans(getNetwork(), applicationId);
        setNeedRefresh(true);
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mrt) {
        if (getMediaRenderStyle().isApplicationDataVisible()) {
            Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(color, getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
            CoherentSpaceGraphics2DUtils.drawLines(graphics2D, edges, mrt, mediaRendererStyle);
        }
    }

    @Override
    public NetworkMediaLayerStyle getMediaRenderStyle() {
        return (NetworkMediaLayerStyle) super.getMediaRenderStyle();
    }
}
