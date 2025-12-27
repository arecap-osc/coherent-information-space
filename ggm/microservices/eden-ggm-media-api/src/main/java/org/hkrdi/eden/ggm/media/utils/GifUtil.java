package org.hkrdi.eden.ggm.media.utils;

import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.v1.PngFrame;
import org.hkrdi.eden.ggm.repository.entity.DataMap;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public final class GifUtil {


    public static byte[] getNetworkAnimationFrameGif(List<DataMap> addresses, List<DataMap> dataMapSet, DrawingOption drawingOption) throws IOException {
        BufferedImage frame = PngUtil.getConnectorsImagePng(addresses, dataMapSet, drawingOption);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream img = ImageIO.createImageOutputStream(baos);
        GifSequenceWriter writer = new GifSequenceWriter(img, frame.getType(), 400, true);
        writer.writeToSequence(frame);
        for( int i = 1 ; i <= 4; i++) {
            BufferedImage aFrame = PngUtil.getConnectorsFramePng(dataMapSet, drawingOption, i);
            writer.writeToSequence(aFrame);
        }
        writer.close();
        img.close();
        writer.dispose();
        return baos.toByteArray();
    }

    public static PngFrame getNetworkAnimationFrameGif2(List<DataMap> addresses, List<DataMap> dataMapSet, DrawingOption drawingOption) throws IOException {
        PngFrame animationFrames = new PngFrame();
        animationFrames.setConnectorsFrame(PngUtil.getConnectorsPng(addresses, dataMapSet, drawingOption));
        animationFrames.setaFrame1(new PngEncoderB(PngUtil.getConnectorsFramePng(dataMapSet, drawingOption, 1), true).pngEncode(true));
        animationFrames.setaFrame2(new PngEncoderB(PngUtil.getConnectorsFramePng(dataMapSet, drawingOption, 2), true).pngEncode(true));
        animationFrames.setaFrame3(new PngEncoderB(PngUtil.getConnectorsFramePng(dataMapSet, drawingOption, 3), true).pngEncode(true));
        animationFrames.setaFrame4(new PngEncoderB(PngUtil.getConnectorsFramePng(dataMapSet, drawingOption, 4), true).pngEncode(true));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageOutputStream img = ImageIO.createImageOutputStream(baos);
//        GifSequenceWriter writer = new GifSequenceWriter(img, frame.getType(), 400, true);
//        writer.writeToSequence(frame);
        return animationFrames;
    }

}
