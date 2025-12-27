package org.hkrdi.eden.ggm.vaadin.console.media.util;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.cop.support.BeanUtil;
import org.springframework.data.geo.Point;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public final class CoherentSpaceGraphics2DUtils {

    public static void drawPoints(Graphics2D graphics2D, List<NodeBean> nbs, MediaRendererTransform mrt, ConfigurableGraphics2dStyle mrs, double radius) {
        graphics2D.setColor(mrs.getAlphaColor());
        graphics2D.setStroke(new BasicStroke(mrs.getPenStroke().floatValue()));
        nbs.parallelStream()
                .map(nb -> BeanUtil.getBean(CoherentSpaceService.class).getRequestGraphicsCoordinates(nb, mrt).get())
                .forEach(requestGraphicsCoordinates -> Graphics2dUtils.drawPoint(graphics2D, requestGraphicsCoordinates.getX(),
                        requestGraphicsCoordinates.getY(), mrs.getPenStroke(), radius));
    }

    public static void drawPoint(Graphics2D graphics2D, NodeBean nb, MediaRendererTransform mrt, ConfigurableGraphics2dStyle mrs,
                                 double radius) {
        drawPoints(graphics2D, Arrays.asList(new NodeBean[]{nb}), mrt, mrs, radius);
    }

    public static void drawLine(Graphics2D graphics2D, EdgeBean edgeBean, MediaRendererTransform mediaRendererTransform,
                                ConfigurableGraphics2dStyle mediaRendererStyle) {
        drawLines(graphics2D, Arrays.asList(new EdgeBean[]{edgeBean}), mediaRendererTransform, mediaRendererStyle);
    }

    public static void drawLines(Graphics2D graphics2D, List<EdgeBean> edgeBeans,
                                 MediaRendererTransform mediaRendererTransform, ConfigurableGraphics2dStyle mrs) {
        graphics2D.setColor(mrs.getAlphaColor());
        graphics2D.setStroke(new BasicStroke(mrs.getPenStroke().floatValue()));

        edgeBeans.stream().forEach(eb -> {
            Point fromRequestGraphicsCoordinates = BeanUtil.getBean(CoherentSpaceService.class)
                    .getRequestGraphicsCoordinates(eb.getFromNode(), mediaRendererTransform).get();
            Point toRequestGraphicsCoordinates = BeanUtil.getBean(CoherentSpaceService.class)
                    .getRequestGraphicsCoordinates(eb.getToNode(), mediaRendererTransform).get();
            Graphics2dUtils.drawLine(graphics2D, fromRequestGraphicsCoordinates.getX(), fromRequestGraphicsCoordinates.getY(),
                    toRequestGraphicsCoordinates.getX(), toRequestGraphicsCoordinates.getY());
        });
    }

    public static void drawLines(Graphics2D graphics2D, List<EdgeBean> edgeBeans,
                                 MediaRendererTransform mediaRendererTransform, ConfigurableGraphics2dStyle mrs, int[] frameParts) {
        graphics2D.setColor(mrs.getAlphaColor());
        graphics2D.setStroke(new BasicStroke(mrs.getPenStroke().floatValue()));

        edgeBeans.stream().forEach(eb -> {
            for (int i = 0; i < frameParts.length; i += 2) {
            	graphics2D.setColor(mrs.getAlphaColor());
                Point head = BeanUtil.getBean(CoherentSpaceService.class)
                        .getRequestGraphicsCoordinates(eb, frameParts[i], 12, mediaRendererTransform).get();
                Point tail = BeanUtil.getBean(CoherentSpaceService.class)
                        .getRequestGraphicsCoordinates(eb, frameParts[i + 1], 12, mediaRendererTransform).get();
                ;
                Graphics2dUtils.drawLine(graphics2D, head.getX(), head.getY(),
                        tail.getX(), tail.getY());
                
                graphics2D.setColor(mrs.getAlphaColor().darker());
                Graphics2dUtils.drawPoint(graphics2D, tail.getX(), tail.getY(),
                		mrs.getPenStroke(), 0.5);
                
            }
        });

    }

    public static void drawString(Graphics2D graphics2D, String s, ClusterBean cb, MediaRendererTransform mrt) {
        Point requestGraphicsCoordinates = BeanUtil.getBean(CoherentSpaceService.class).getRequestGraphicsCoordinates(cb, mrt)
                .get();
        Graphics2dUtils.drawStringInCenter(graphics2D, s, requestGraphicsCoordinates.getX(), requestGraphicsCoordinates.getY());
    }


    public static void drawString(Graphics2D graphics2D, String s, NodeBean nb, ClusterBean cb, int startFrom, int totalParts, MediaRendererTransform mrt) {
        Point requestGraphicsCoordinates = BeanUtil.getBean(CoherentSpaceService.class).getRequestGraphicsCoordinates(nb, cb, startFrom, totalParts, mrt)
                .get();

        Graphics2dUtils.drawStringInCenter(graphics2D, s, requestGraphicsCoordinates.getX(), requestGraphicsCoordinates.getY());
    }

}