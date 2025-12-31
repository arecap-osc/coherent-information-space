package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.PNGMediaRendererDrawer;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.Map;

public class InformationalStreamPointGraph extends InformationalStreamGraph implements PNGMediaRendererDrawer {

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().stream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
//                if(sgraph.getId() < 0) {
                    drawUpstreamSolution(graphics2D, mediaRendererTransform, sgraph);
                    drawDownstreamSolution(graphics2D, mediaRendererTransform, sgraph);
//                }
            });
        }
    }

    private void drawDownstreamSolution(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#DE1FCC"));
        sgraph.getDownstreamFunctionGraph().parallelStream()
                .forEach(cp -> Graphics2dUtils.drawPoint(graphics2D, cp.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, cp.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                        2.0D, 2.2D));
        graphics2D.setColor(ColorFactory.web("#1FDE31"));
        sgraph.getDownstreamSystemGraph().parallelStream()
                .forEach(cp -> Graphics2dUtils.drawPoint(graphics2D, cp.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, cp.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                        2.0D, 2.2D));
    }

    private void drawUpstreamSolution(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph) {
        graphics2D.setColor(ColorFactory.web("#DE711F"));
        sgraph.getUpstreamFunctionGraph().parallelStream()
                .forEach(cp -> Graphics2dUtils.drawPoint(graphics2D, cp.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, cp.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                        2.5D, 2.2D));
        graphics2D.setColor(ColorFactory.web("#1F8CDE"));
        sgraph.getUpstreamSystemGraph().parallelStream()
                .forEach(cp -> Graphics2dUtils.drawPoint(graphics2D, cp.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, cp.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2,
                        2.5D, 2.2D));
    }

}
