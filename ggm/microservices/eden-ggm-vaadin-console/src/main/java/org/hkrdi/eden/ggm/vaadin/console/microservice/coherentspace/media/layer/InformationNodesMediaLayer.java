package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSelectionMediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.cop.support.BeanUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InformationNodesMediaLayer extends GgmSelectionMediaRendererLayer {
    private List<NodeBean> nodes = new ArrayList<>();
    private Long applicationId;
    private Color color;

    public InformationNodesMediaLayer(String network, NetworkMediaLayerStyle graphics2dStyle, Color color, Long applicationId) {
        super(network, graphics2dStyle);
        this.color = color;
        this.applicationId = applicationId;
        if (applicationId != null) {
            nodes = BeanUtil.getBean(CoherentSpaceService.class).getInformationContainedNetworkNodeBeans(network, applicationId);
        }
    }

    public void setApplicationId(Long applicationId) {
        if (applicationId == null) {
            return;
        }
        this.applicationId = applicationId;
        nodes = BeanUtil.getBean(CoherentSpaceService.class).getInformationContainedNetworkNodeBeans(getNetwork(), applicationId);
        setNeedRefresh(true);
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mrt) {
        if (getMediaRenderStyle().isApplicationDataVisible()){
            nodes.stream().forEach(node -> {
                Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(ColorFactory.web("#666666"),
                        getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
                CoherentSpaceGraphics2DUtils
                        .drawPoint(graphics2D, new NodeBean(getNetwork(), node.getAddressIndex()),
                                mrt, mediaRendererStyle, 2.2);
                mediaRendererStyle = new Graphics2dStyle(color, getMediaRenderStyle().getOpacity(), getMediaRenderStyle().getPenStroke());
                CoherentSpaceGraphics2DUtils
                        .drawPoint(graphics2D, new NodeBean(getNetwork(), node.getAddressIndex()),
                                mrt, mediaRendererStyle, 1.8);
            });
        }
    }

    @Override
    public NetworkMediaLayerStyle getMediaRenderStyle() {
        return (NetworkMediaLayerStyle) super.getMediaRenderStyle();
    }
}
