package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.LinkBean;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SemanticMapWordLinksMediaLayer extends SemanticMapPngMediaLayer {

    private Set<LinkBean> wordLinks = new HashSet<>();

    public SemanticMapWordLinksMediaLayer(String network) {
        super(network);
    }

    public void setWordLinks(Set<LinkBean> wordLinks) {
        this.wordLinks.clear();
        if(wordLinks != null) {
            this.wordLinks.addAll(wordLinks);
        }
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(null, 1.5d);

        if(wordLinks.size() > 0) {

            mediaRendererStyle.setColor(ColorFactory.web("#aa0000"));
            SemanticMapGraphics2DUtils.drawLines(graphics2D, wordLinks, mediaRendererTransform, mediaRendererStyle);

            List<WordBean> words = wordLinks.stream().map(linkBean -> linkBean.getToWord()).collect(Collectors.toList());
            mediaRendererStyle = new Graphics2dStyle(null, 3.5d);
            mediaRendererStyle.setColor(ColorFactory.web("#666666"));
            SemanticMapGraphics2DUtils.drawPoints(graphics2D, words, mediaRendererTransform, mediaRendererStyle, 3.5);
//
            mediaRendererStyle.setColor(ColorFactory.web("#00aa00"));
            SemanticMapGraphics2DUtils.drawPoints(graphics2D, words, mediaRendererTransform, mediaRendererStyle, 2.5);
        }

    }

}
