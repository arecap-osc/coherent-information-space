package org.hkrdi.eden.ggm.media.coherentspace;

import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public final class HexavalentNetworkMedia {

    public static final BufferedImage getLayerFramePng(String network,
                                                       boolean addressIndex, boolean trivalentLogic, boolean clusterIndex, boolean clusterType, 
                                                       boolean book, ApplicationDataParameter applicationDataParameter,
                                                       DrawingOption drawingOption, int frame, Map<String,String> selectedDataMapIds) throws IOException {
        long time = System.currentTimeMillis();
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        HexavalentNetworkGraphics2d.drawLayer(bufferedImage, network,
                addressIndex, trivalentLogic, clusterIndex, clusterType, book, applicationDataParameter , drawingOption, frame, selectedDataMapIds);
        System.out.println("Animation frame response time"+(System.currentTimeMillis()-time)+"ms");
        return bufferedImage;
    }

}
