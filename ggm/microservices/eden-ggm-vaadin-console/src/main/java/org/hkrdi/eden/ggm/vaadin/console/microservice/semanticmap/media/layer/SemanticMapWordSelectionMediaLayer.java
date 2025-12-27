package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;

import java.awt.*;
import java.util.Optional;

public class SemanticMapWordSelectionMediaLayer extends SemanticMapPngMediaLayer {

    private Optional<WordBean> fromWord = Optional.empty();

    private Optional<WordBean> toWord = Optional.empty();

    public SemanticMapWordSelectionMediaLayer(String network) {
        super(network);
    }

    public void setFromWord(WordBean word) {
        fromWord = Optional.ofNullable(word);
    }

    public void setToWord(WordBean word) {
        toWord = Optional.ofNullable(word);
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        Graphics2dStyle mediaRendererStyle = new Graphics2dStyle(null, 3.5d);
        if(fromWord.isPresent()) {

            mediaRendererStyle.setColor(ColorFactory.web("#666666"));
            SemanticMapGraphics2DUtils.drawPoint(graphics2D, fromWord.get(), mediaRendererTransform, mediaRendererStyle, 6.5);
            mediaRendererStyle.setColor(ColorFactory.web("#fca103"));
            SemanticMapGraphics2DUtils.drawPoint(graphics2D, fromWord.get(), mediaRendererTransform, mediaRendererStyle, 5.5);
        }
        if(toWord.isPresent()) {

            mediaRendererStyle.setColor(ColorFactory.web("#666666"));
            SemanticMapGraphics2DUtils.drawPoint(graphics2D, toWord.get(), mediaRendererTransform, mediaRendererStyle, 6.5);
            mediaRendererStyle.setColor(ColorFactory.web("#fca103"));
            SemanticMapGraphics2DUtils.drawPoint(graphics2D, toWord.get(), mediaRendererTransform, mediaRendererStyle, 5.5);
            mediaRendererStyle.setColor(ColorFactory.web("#666666"));
            SemanticMapGraphics2DUtils.drawPoint(graphics2D, toWord.get(), mediaRendererTransform, mediaRendererStyle, 2.5);
        }
    }

}
