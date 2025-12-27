package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.LinkBean;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.springframework.cop.support.BeanUtil;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SemanticMapLinksMediaLayer extends SemanticMapPngMediaLayer {

    private Set<LinkBean> semanticMapNetworkLinks = new HashSet<>();

    public SemanticMapLinksMediaLayer(String network) {
        super(network);
        semanticMapNetworkLinks.addAll(BeanUtil.getBean(SemanticMapService.class).getDefaultSemanticMapLinks(network));
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(null, 2.0d);

        mediaRendererStyle.setColor(ColorFactory.web("#6b89d8"));
        SemanticMapGraphics2DUtils.drawLines(graphics2D, semanticMapNetworkLinks, mediaRendererTransform, mediaRendererStyle);

    }

}
