package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.PNGMediaRendererDrawer;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.Map;

public class InformationalStreamLineGraph extends InformationalStreamGraph implements PNGMediaRendererDrawer {


    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        graphics2D.setStroke(new BasicStroke(3F));
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().stream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
                if(sgraph.getNetting().name().contains(StreamTopology.Upstream.name())) {
                    drawUpstreamPoly(graphics2D, mediaRendererTransform, sgraph);
                    drawUpstreamFunctionPoly(graphics2D, mediaRendererTransform, sgraph);
                    drawUpstreamSystemPoly(graphics2D, mediaRendererTransform, sgraph);
                } else {
                    drawDownstreamPoly(graphics2D, mediaRendererTransform, sgraph);
                    drawDownstreamFunctionPoly(graphics2D, mediaRendererTransform, sgraph);
                    drawDownstreamSystemPoly(graphics2D, mediaRendererTransform, sgraph);
                }
            });
        }
    }

    private void drawUpstreamSystemPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#DE1FCC"));
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getUpstreamSystemGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getUpstreamSystemGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

    private void drawDownstreamSystemPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#1F8CDE"));
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getDownstreamSystemGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getDownstreamSystemGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

    private void drawUpstreamFunctionPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#1FDE31"));
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getUpstreamFunctionGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getUpstreamFunctionGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

    private void drawDownstreamFunctionPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#DE711F"));
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getDownstreamFunctionGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getDownstreamFunctionGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

    private void drawUpstreamPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getUpstreamGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        int i = 1;
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getUpstreamGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#DE711F") : ColorFactory.web("#1F8CDE"));
            i++;
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#DE711F") : ColorFactory.web("#1F8CDE"));
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

    private void drawDownstreamPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getDownstreamGraph().get(0);
        boolean skip = true;
        ComplexPlane temp = new ComplexPlane(start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        int i = 1;
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getDownstreamGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#1FDE31") : ColorFactory.web("#DE1FCC"));
            i++;
            Graphics2dUtils.drawLine(graphics2D, temp.getReal(), temp.getImaginary(), toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
            temp = new ComplexPlane(toPoint.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, toPoint.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
        }
        graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#1FDE31") : ColorFactory.web("#DE1FCC"));
        Graphics2dUtils.drawLine(graphics2D, start.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, start.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, temp.getReal(), temp.getImaginary());
    }

}
