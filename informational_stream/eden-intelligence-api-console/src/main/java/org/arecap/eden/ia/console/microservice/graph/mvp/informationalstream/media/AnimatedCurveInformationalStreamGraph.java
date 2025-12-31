package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;
import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;
import org.arecap.eden.ia.console.media.GIFMediaRendererDrawer;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.Map;

public class AnimatedCurveInformationalStreamGraph extends InformationalStreamGraph implements GIFMediaRendererDrawer {

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, Integer frameNo) {
        graphics2D.setStroke(new BasicStroke(3F));
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().stream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
//                if(sgraph.getId() == 0)
                if (sgraph.getNetting().name().contains(StreamTopology.Upstream.name())) {
                    if (sgraph.getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity)
                            && sgraph.getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity)) {
                        drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
                    } else {
                        drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
                    }
                    drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);
                    drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
                    drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
                    drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);


                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
                } else {
                    if (sgraph.getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity)
                            && sgraph.getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity)) {
                        drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);

                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);

//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
//
//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
//
//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
//
//                        drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamSelectorSystemGraph(), frameNo);
                    } else {
                        drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);

                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);
                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);

//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
//
//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);
//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
//
//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
//
//                        drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamConsumerSystemGraph(), frameNo);
                    }
                    drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
                    drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
                    drawInformationalStream2(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse2(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamDetectorFunctionGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamConsumerSystemGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);

                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);

                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);

//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamDetectorSystemGraph(), frameNo);
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getUpstreamDetectorFunctionGraph(), frameNo);
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorSystemGraph(), frameNo);
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerFunctionGraph(), frameNo);
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionGraph(), frameNo);
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getDownstreamConsumerFunctionGraph(), frameNo);
//
//                    drawInformationalStreamReverse(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
//
//                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamDetectorSystemGraph(), frameNo);
                }

            });
        }
    }

    private void drawInformationalStream(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to, Integer frameNo) {
        drawFramePath(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, frameNo);
    }

    private void drawInformationalStream2(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to, Integer frameNo) {
        drawFramePath2(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, frameNo);
    }

    private void drawInformationalStreamReverse(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to, Integer frameNo) {
        drawFramePathReverse(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, frameNo);
    }

    private void drawInformationalStreamReverse2(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to, Integer frameNo) {
        drawFramePathReverse2(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, frameNo);
    }

    private void drawFramePath(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY, Integer framNo) {
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        Double t2 = 0.2D * framNo;
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 - distance/6 * Math.sin(theta), (fromY + toY)/2  + distance/6 * Math.cos(theta));
        ComplexPlane framePoint2 = new ComplexPlane((1 - t2) * (1 - t2) * fromX + 2 * (1 - t2) * t2 * controlPoint.getReal() + t2 * t2 * toX,
                (1 - t2) * (1 - t2) * fromY + 2 * (1 - t2) * t2 * controlPoint.getImaginary() + t2 * t2 * toY);
        graphics2D.setColor(ColorFactory.YELLOW);
        Graphics2dUtils.drawPoint(graphics2D, framePoint2.getReal(), framePoint2.getImaginary(),
                1.6D, 1.8D);
    }

    private void drawFramePath2(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY, Integer framNo) {
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        Double t2 = 0.2D * framNo;
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 - distance/2 * Math.sin(theta), (fromY + toY)/2  + distance/2 * Math.cos(theta));
        ComplexPlane framePoint2 = new ComplexPlane((1 - t2) * (1 - t2) * fromX + 2 * (1 - t2) * t2 * controlPoint.getReal() + t2 * t2 * toX,
                (1 - t2) * (1 - t2) * fromY + 2 * (1 - t2) * t2 * controlPoint.getImaginary() + t2 * t2 * toY);
        graphics2D.setColor(ColorFactory.RED);
        Graphics2dUtils.drawPoint(graphics2D, framePoint2.getReal(), framePoint2.getImaginary(),
                2D, 2.4D);
    }

    private void drawFramePathReverse(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY, Integer framNo) {
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        Double t2 = 0.2D * framNo;
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 + distance/6 * Math.sin(theta), (fromY + toY)/2  - distance/6 * Math.cos(theta));
        ComplexPlane framePoint2 = new ComplexPlane((1 - t2) * (1 - t2) * fromX + 2 * (1 - t2) * t2 * controlPoint.getReal() + t2 * t2 * toX,
                (1 - t2) * (1 - t2) * fromY + 2 * (1 - t2) * t2 * controlPoint.getImaginary() + t2 * t2 * toY);
        graphics2D.setColor(ColorFactory.YELLOW);
        Graphics2dUtils.drawPoint(graphics2D, framePoint2.getReal(), framePoint2.getImaginary(),
                1.6D, 1.8D);
    }

    private void drawFramePathReverse2(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY, Integer framNo) {
        Double distance = Math.sqrt(Math.pow(toX-fromX, 2) + Math.pow(toY-fromY, 2));
        Double theta = Math.atan2(toY - fromY, toX - fromX);
        Double t2 = 0.2D * framNo;
        ComplexPlane controlPoint = new ComplexPlane((fromX + toX)/2 + distance/2 * Math.sin(theta), (fromY + toY)/2  - distance/2 * Math.cos(theta));
        ComplexPlane framePoint2 = new ComplexPlane((1 - t2) * (1 - t2) * fromX + 2 * (1 - t2) * t2 * controlPoint.getReal() + t2 * t2 * toX,
                (1 - t2) * (1 - t2) * fromY + 2 * (1 - t2) * t2 * controlPoint.getImaginary() + t2 * t2 * toY);
        graphics2D.setColor(ColorFactory.RED);
        Graphics2dUtils.drawPoint(graphics2D, framePoint2.getReal(), framePoint2.getImaginary(),
                2D, 2.4D);
    }

}
