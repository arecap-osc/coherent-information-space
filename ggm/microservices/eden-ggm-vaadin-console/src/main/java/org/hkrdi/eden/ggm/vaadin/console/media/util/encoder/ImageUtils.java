package org.hkrdi.eden.ggm.vaadin.console.media.util.encoder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class ImageUtils
{
    public static BufferedImage fixSubBufferedImage(final BufferedImage image) {
        final boolean needFix = image.getSampleModel().getWidth() != image.getWidth() || image.getSampleModel().getHeight() != image.getHeight();
        if (!needFix) {
            return image;
        }
        if (image.getType() == 10) {
            final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), 10);
            final byte[] pixelBytes = ((DataBufferByte)result.getRaster().getDataBuffer()).getData();
            int p = 0;
            for (int y = 0; y < image.getHeight(); ++y) {
                for (int x = 0; x < image.getWidth(); ++x) {
                    pixelBytes[p++] = (byte)(image.getRGB(x, y) & 0xFF);
                }
            }
            return result;
        }
        final BufferedImage result = image.getColorModel().hasAlpha() ? new BufferedImage(image.getWidth(), image.getHeight(), 2) : new BufferedImage(image.getWidth(), image.getHeight(), 1);
        final int[] pixelInts = ((DataBufferInt)result.getRaster().getDataBuffer()).getData();
        int p = 0;
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                pixelInts[p++] = image.getRGB(x, y);
            }
        }
        return result;
    }
}

