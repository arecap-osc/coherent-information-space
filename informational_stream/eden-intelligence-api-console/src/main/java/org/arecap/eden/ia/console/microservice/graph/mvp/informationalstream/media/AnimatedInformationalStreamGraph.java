package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;
import org.arecap.eden.ia.console.media.GIFMediaRendererDrawer;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class AnimatedInformationalStreamGraph extends InformationalStreamGraph implements GIFMediaRendererDrawer {

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, Integer frameNo) {
        graphics2D.setStroke(new BasicStroke(3F));
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().parallelStream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
                if(sgraph.getNetting().name().contains(StreamTopology.Upstream.name())) {
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorFunctionGraph(), sgraph.getUpstreamSelectorFunctionUpstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamSelectorSystemGraph(), sgraph.getUpstreamSelectorSystemUpstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorFunctionGraph(), sgraph.getUpstreamDetectorFunctionUpstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamDetectorSystemGraph(), sgraph.getUpstreamDetectorSystemUpstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerFunctionGraph(), sgraph.getUpstreamConsumerFunctionUpstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getUpstreamConsumerSystemGraph(), sgraph.getUpstreamConsumerSystemUpstream(), frameNo);
                } else {
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorFunctionGraph(), sgraph.getDownstreamSelectorFunctionDownstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamSelectorSystemGraph(), sgraph.getDownstreamSelectorSystemDownstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorFunctionGraph(), sgraph.getDownstreamDetectorFunctionDownstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamDetectorSystemGraph(), sgraph.getDownstreamDetectorSystemDownstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerFunctionGraph(), sgraph.getDownstreamConsumerFunctionDownstream(), frameNo);
                    drawInformationalStream(graphics2D, mediaRendererTransform, sgraph.getDownstreamConsumerSystemGraph(), sgraph.getDownstreamConsumerSystemDownstream(), frameNo);
                }
            });
        }
    }

    private void drawInformationalStream(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double>  from, List<StreamApplicationGraphPropertiesFactory<Double>> upstream, Integer frameNo) {
        for(StreamApplicationGraphPropertiesFactory<Double> to: upstream) {
            drawInformationalStream(graphics2D, mediaRendererTransform, from, to, frameNo);
        }
    }

    private void drawInformationalStream(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, StreamApplicationGraphPropertiesFactory<Double> from, StreamApplicationGraphPropertiesFactory<Double> to, Integer frameNo) {
        drawFrameLines(graphics2D, from.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, from.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                to.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, to.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2, frameNo);
    }

    private void drawFrameLines(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY, Integer framNo) {
        int[] fp = frameParts(framNo);
        ComplexPlane from = new ComplexPlane(fromX, fromY);
        ComplexPlane to = new ComplexPlane(toX, toY);
        for (int i = 0; i < fp.length; i += 2) {
            ComplexPlane head = splitPoint(from, to, fp[i], 12);
            ComplexPlane tail = splitPoint(from, to, fp[i + 1], 12);
            graphics2D.setColor(ColorFactory.YELLOW);
            Graphics2dUtils.drawLine(graphics2D, head.getReal(), head.getImaginary(), tail.getReal(), tail.getImaginary());
            graphics2D.setColor(ColorFactory.YELLOWGREEN);
            Graphics2dUtils.drawPoint(graphics2D, tail.getReal(), tail.getImaginary(),
                    2D, 1.2D);
        }

    }

    private ComplexPlane splitPoint(ComplexPlane head, ComplexPlane tail, int part, int totalParts) {
        return new ComplexPlane(head.getReal() + ( tail.getReal() - head.getReal() ) / totalParts * part ,
                head.getImaginary() + ( tail.getImaginary() - head.getImaginary() ) / totalParts * part);
    }


}
