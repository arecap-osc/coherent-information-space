package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SemanticMapWordsMediaLayer extends SemanticMapPngMediaLayer {

    private List<WordBean> semanticMapWords = new ArrayList<>();

    public SemanticMapWordsMediaLayer(String network) {
        super(network);
        semanticMapWords.addAll(getSemanticMapService().getDefaultSemanticMapWords(network));
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(null, 3.6d);

        mediaRendererStyle.setColor(ColorFactory.web("#6b89d8"));
        SemanticMapGraphics2DUtils.drawPoints(graphics2D, semanticMapWords, mediaRendererTransform, mediaRendererStyle, 2.5);

        mediaRendererStyle.setColor(ColorFactory.DARKGRAY);
        SemanticMapGraphics2DUtils.drawPoints(graphics2D, semanticMapWords, mediaRendererTransform, mediaRendererStyle, 1.5);

    }

}
