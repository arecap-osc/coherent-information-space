package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media.layer;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.util.Graphics2dUtils;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.LinkBean;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.WordBean;
import org.springframework.cop.support.BeanUtil;
import org.springframework.data.geo.Point;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SemanticMapGraphics2DUtils {

    public static void drawPoints(Graphics2D graphics2D, List<WordBean> wbs, MediaRendererTransform mrt,
                                  ConfigurableGraphics2dStyle mrs, double radius) {
        graphics2D.setColor(mrs.getColor());
        graphics2D.setStroke(new BasicStroke(mrs.getPenStroke().floatValue()));
        wbs.stream()
                .map(wb -> BeanUtil.getBean(SemanticMapService.class).getRequestGraphicsCoordinates(wb, mrt).get())
                .forEach(requestGraphicsCoordinates -> Graphics2dUtils.drawPoint(graphics2D, requestGraphicsCoordinates.getX(),
                        requestGraphicsCoordinates.getY(), mrs.getPenStroke(), radius));
    }

    public static void drawPoint(Graphics2D graphics2D, WordBean wb, MediaRendererTransform mrt, ConfigurableGraphics2dStyle mrs,
                                 double radius) {
        drawPoints(graphics2D, Arrays.asList(new WordBean[] { wb }), mrt, mrs, radius);
    }

    public static void drawLine(Graphics2D graphics2D, LinkBean linkBean, MediaRendererTransform mediaRendererTransform,
                                ConfigurableGraphics2dStyle mediaRendererStyle) {
        Set<LinkBean> set = new HashSet<>();
        set.add(linkBean);
        drawLines(graphics2D, set, mediaRendererTransform, mediaRendererStyle);
    }

    public static void drawLines(Graphics2D graphics2D, Set<LinkBean> edgeBeans,
                                 MediaRendererTransform mediaRendererTransform, ConfigurableGraphics2dStyle mediaRendererStyle) {
        graphics2D.setColor(mediaRendererStyle.getColor());
        graphics2D.setStroke(new BasicStroke(mediaRendererStyle.getPenStroke().floatValue()));

        edgeBeans.stream().forEach(lb -> {
            Point fromRequestGraphicsCoordinates = BeanUtil.getBean(SemanticMapService.class)
                    .getRequestGraphicsCoordinates(lb.getFromWord(), mediaRendererTransform).get();
            Point toRequestGraphicsCoordinates = BeanUtil.getBean(SemanticMapService.class)
                    .getRequestGraphicsCoordinates(lb.getToWord(), mediaRendererTransform).get();
            Graphics2dUtils.drawLine(graphics2D, fromRequestGraphicsCoordinates.getX(), fromRequestGraphicsCoordinates.getY(),
                    toRequestGraphicsCoordinates.getX(), toRequestGraphicsCoordinates.getY());
        });
    }

    public static void drawString(Graphics2D graphics2D, String s, WordBean wb, MediaRendererTransform mrt) {
        Point requestGraphicsCoordinates = BeanUtil.getBean(SemanticMapService.class)
                .getRequestGraphicsCoordinates(wb, mrt)
                .get();
        Graphics2dUtils.drawStringInCenter(graphics2D, s, requestGraphicsCoordinates.getX(), requestGraphicsCoordinates.getY());
    }

}
