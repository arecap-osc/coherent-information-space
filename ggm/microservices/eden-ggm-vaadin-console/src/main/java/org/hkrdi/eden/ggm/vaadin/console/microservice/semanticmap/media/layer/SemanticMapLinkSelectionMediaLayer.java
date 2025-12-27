package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.LinkBean;

import java.awt.*;
import java.util.Optional;

public class SemanticMapLinkSelectionMediaLayer extends SemanticMapPngMediaLayer {

    private Optional<LinkBean> linkBean = Optional.empty();

    public SemanticMapLinkSelectionMediaLayer(String network) {
        super(network);
    }

    public void setLinkBean(LinkBean linkBean) {
        this.linkBean = Optional.ofNullable(linkBean);
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        if(linkBean.isPresent()) {
            Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(null, 4.4d);

            mediaRendererStyle.setColor(ColorFactory.web("#fca103"));
            SemanticMapGraphics2DUtils.drawLine(graphics2D, linkBean.get(), mediaRendererTransform, mediaRendererStyle);
        }
    }

}
