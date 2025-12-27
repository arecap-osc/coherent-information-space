package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.PNGMediaRendererDrawer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapServiceFactory;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;

import java.awt.*;
import java.util.Optional;

public class SemanticMapWordsLettersMediaLayer implements SemanticMapServiceFactory, MediaRendererLayer, PNGMediaRendererDrawer {

    private Boolean needRefresh = false;


    private Optional<Long> semanticMapId = Optional.empty();

    @Override
    public Boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public void setSemanticMapId(Long semanticMapId) {
        this.semanticMapId = Optional.ofNullable(semanticMapId);
    }

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        if(semanticMapId.isPresent()) {
            graphics2D.setColor(ColorFactory.BLACK);
            Font font = graphics2D.getFont();
            graphics2D.setFont(new Font(font.getName(), Font.BOLD, 24));
            getSemanticMapService().findMapWords(semanticMapId.get()).stream().forEach(mapWords -> {
                SemanticMapGraphics2DUtils.drawString(graphics2D, mapWords.getLetter(), new WordBean("", mapWords.getX(), mapWords.getY()), mediaRendererTransform);
            });
        }
    }

}
