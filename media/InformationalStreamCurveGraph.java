package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.PNGMediaRendererDrawer;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Map;

public class InformationalStreamCurveGraph extends InformationalStreamGraph implements PNGMediaRendererDrawer {


    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        graphics2D.setStroke(new BasicStroke(3F));
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().stream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
//                if(sgraph.getId() == 0)
                if (sgraph.getNetting().name().contains(StreamTopology.Upstream.name())) {
                    drawUpstreamPoly(graphics2D, mediaRendererTransform, sgraph);
                    graphics2D.setColor(ColorFactory.web("#1FDE31"));
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph());


                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamDetectorFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph());

                    graphics2D.setColor(ColorFactory.web("#DE1FCC"));

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamConsumerSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getUpstreamDetectorSystemGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamDetectorSystemGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph());
                } else {
                    drawDownstreamPoly(graphics2D, mediaRendererTransform, sgraph);
                    graphics2D.setColor(ColorFactory.web("#1F8CDE"));

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph());

//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph());
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph());
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph());
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph());

//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph());

                    graphics2D.setColor(ColorFactory.web("#DE711F"));

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph());

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph());
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorSystemGraph());

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamDetectorSystemGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph());
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph());

//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorSystemGraph());
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerSystemGraph());
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph());
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph());
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph());

//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph());
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorSystemGraph());
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamDetectorSystemGraph());
                }
            });
        }
    }


    private void drawInformationalStreamReverse(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to) {
        drawPathReverse(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
    }

    private void drawInformationalStream(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to) {
        drawPath(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
    }

    private void drawInformationalStream2(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to) {
        drawPath2(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2);
    }

    private void drawPathReverse(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY) {
        Path2D arc = new Path2D.Double();
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 - distance/6 * Math.sin(theta), (fromY + toY)/2  + distance/6 * Math.cos(theta));
        arc.moveTo(fromX, fromY);
        arc.quadTo(controlPoint.getReal(), controlPoint.getImaginary(), toX, toY);
        graphics2D.draw(arc);
    }

    private void drawPath(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY) {
        Path2D arc = new Path2D.Double();
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 + distance/6 * Math.sin(theta), (fromY + toY)/2  - distance/6 * Math.cos(theta));
        arc.moveTo(fromX, fromY);
        arc.quadTo(controlPoint.getReal(), controlPoint.getImaginary(), toX, toY);
        graphics2D.draw(arc);
    }

    private void drawPath2(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY) {
        Path2D arc = new Path2D.Double();
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 + distance/2 * Math.sin(theta), (fromY + toY)/2  - distance/2 * Math.cos(theta));
        arc.moveTo(fromX, fromY);
        arc.quadTo(controlPoint.getReal(), controlPoint.getImaginary(), toX, toY);
        graphics2D.draw(arc);
    }

    private void drawUpstreamPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getUpstreamGraph().get(0);
        boolean skip = true;
        int i = 1;
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getUpstreamGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#DE711F") : ColorFactory.web("#1F8CDE"));
            drawInformationalStream2(graphics2D, mediaRendererTransform, start, toPoint);
            start = toPoint;
            i++;
        }
        graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#DE711F") : ColorFactory.web("#1F8CDE"));
        drawInformationalStream2(graphics2D, mediaRendererTransform, start, sgraph.getUpstreamGraph().get(0));
    }

    private void drawDownstreamPoly(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        StreamApplicationGraphPropertiesFactory<Double> start = sgraph.getDownstreamGraph().get(0);
        boolean skip = true;
        int i = 1;
        for (StreamApplicationGraphPropertiesFactory<Double> toPoint : sgraph.getDownstreamGraph()) {
            if (skip) {
                skip = false;
                continue;
            }
            graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#1FDE31") : ColorFactory.web("#DE1FCC"));
            drawInformationalStream2(graphics2D, mediaRendererTransform, start, toPoint);
            start = toPoint;
            i++;
        }
        graphics2D.setColor(i % 2 == 0 ? ColorFactory.web("#1FDE31") : ColorFactory.web("#DE1FCC"));
        drawInformationalStream2(graphics2D, mediaRendererTransform, start, sgraph.getDownstreamGraph().get(0));
    }

}
