package org.arecap.eden.ia.console.microservice.graph.mvp.informationalstream.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.PNGMediaRendererDrawer;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.beryx.awt.color.ColorFactory;

import java.awt.*;
import java.util.Map;

public class InformationalStreamInformationGraph extends InformationalStreamGraph implements PNGMediaRendererDrawer {

    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        for(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> isg: getGraph()) {
            isg.keySet().stream().forEach(step -> {
                InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = isg.get(step);
//                if(sgraph.getId() < 0) {
                    graphics2D.setColor(sgraph.getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ? ColorFactory.RED : ColorFactory.BLUE);
                    Graphics2dUtils.drawStringInCenter(graphics2D, sgraph.getId() + "", sgraph.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, (sgraph.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2));
                    graphics2D.setColor(sgraph.getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ? ColorFactory.web("#DE711F") : ColorFactory.web("#1F8CDE"));
                    sgraph.getUpstreamGraph().stream().forEach(usgraph -> {
                        ComplexPlane display = splitPoint(usgraph, sgraph, 1, 3);
                        Graphics2dUtils.drawStringInCenter(graphics2D, usgraph.getString() + "", display.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, (display.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2));
                    });
                    graphics2D.setColor(sgraph.getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ? ColorFactory.web("#DE1FCC") : ColorFactory.web("#1FDE31"));
                    sgraph.getDownstreamGraph().stream().forEach(usgraph -> {
                        ComplexPlane display = splitPoint(usgraph, sgraph, 1, 3);
                        Graphics2dUtils.drawStringInCenter(graphics2D, usgraph.getString() + "", display.getReal() + mediaRendererTransform.getCenterX() + mediaRendererTransform.getWidth() / 2, (display.getImaginary() + mediaRendererTransform.getCenterY() + mediaRendererTransform.getHeight() / 2));
                    });
//                }
            });
        }
    }

    private ComplexPlane splitPoint(ComplexPlanePropertiesFactory<Double> head, ComplexPlanePropertiesFactory<Double> tail, int part, int totalParts) {
        return new ComplexPlane(head.getReal() + ( tail.getReal() - head.getReal() ) / totalParts * part ,
                head.getImaginary() + ( tail.getImaginary() - head.getImaginary() ) / totalParts * part);
    }


}
