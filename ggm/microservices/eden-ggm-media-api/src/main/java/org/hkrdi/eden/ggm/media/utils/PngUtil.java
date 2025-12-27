package org.hkrdi.eden.ggm.media.utils;

import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.springframework.data.geo.Point;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public final class PngUtil {

    public static final byte[] getGridLettersPng(List<MapWord> mapWords, DrawingOption drawingOption) {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawGridLetters(bufferedImage, mapWords, drawingOption);
        return new PngEncoderB(bufferedImage, true).pngEncode(true);
    }

    public static final byte[] getGridFramePng(List<DataMap> sustainableOuterRoutes, List<DataMap> metabolicOuterRoutes, DrawingOption drawingOption) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawGridFrame(bufferedImage, sustainableOuterRoutes, metabolicOuterRoutes, drawingOption);
        return new PngEncoderB(bufferedImage, true).pngEncode(true);
    }

    public static final byte[] getGridAddressesPng(List<Point> gridPoints, DrawingOption drawingOption) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawGridAddresses(bufferedImage, gridPoints, drawingOption);
        return new PngEncoderB(bufferedImage, true).pngEncode(true);
    }

    public static final byte[] getConnectorsPng(List<DataMap> addresses, List<DataMap> dataMapSet, DrawingOption drawingOption) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawNetwork(bufferedImage, addresses, dataMapSet, drawingOption);
        return new PngEncoderB(bufferedImage, true).pngEncode(true);
    }

    public static final BufferedImage getConnectorsImagePng(List<DataMap> addresses, List<DataMap> dataMapSet, DrawingOption drawingOption) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawNetwork(bufferedImage, addresses, dataMapSet, drawingOption);
        return bufferedImage;
    }

    public static final BufferedImage getConnectorsFramePng(List<DataMap> dataMapSet, DrawingOption drawingOption, int frame) throws IOException {
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawFrame(bufferedImage, dataMapSet, drawingOption, frame);
        return bufferedImage;
    }

    public static final BufferedImage getAnimationFramePng(List<DataMap> addresses, List<DataMap> dataMapSet, DrawingOption drawingOption, int frame) throws IOException {
        long time = System.currentTimeMillis();
        BufferedImage bufferedImage = new BufferedImage((int) drawingOption.getBottomRight().getX(),
                (int)drawingOption.getBottomRight().getY(), BufferedImage.TYPE_INT_ARGB);
        Graphics2dUtils.drawNetwork(bufferedImage, addresses, dataMapSet, drawingOption, frame);
        System.out.println("Animation frame response time"+(System.currentTimeMillis()-time)+"ms");
        return bufferedImage;
    }


}
