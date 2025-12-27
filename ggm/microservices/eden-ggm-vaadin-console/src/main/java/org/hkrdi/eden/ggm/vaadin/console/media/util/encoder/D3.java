package org.hkrdi.eden.ggm.vaadin.console.media.util.encoder;

public final class D3 {
    private final int r;
    private final int g;
    private final int b;
    private final int rgb;
    
    private D3(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.rgb = (r << 16 | g << 8 | b);
    }
    
    private static int diff(final int r, final int g, final int b, final D3 pal) {
        final int Rdiff = r - pal.r;
        final int Gdiff = g - pal.g;
        final int Bdiff = b - pal.b;
        return Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
    }
    
    private static void findClosest(final int r, final int g, final int b, final D3[] palette, final int[] obj) {
        D3 closest = palette[0];
        int cDiff = diff(r, g, b, closest);
        int found = 0;
        for (int i = 1; i < 256; ++i) {
            final D3 n = palette[i];
            final int nDiff = diff(r, g, b, n);
            if (nDiff < cDiff) {
                closest = n;
                found = i;
                cDiff = nDiff;
            }
        }
        obj[0] = closest.rgb;
        obj[1] = found;
    }
    
    public static byte[] process(final byte[] colorPalette, final int[] image, final int h, final int w) {
        int p = 0;
        final D3[] palette = new D3[256];
        for (int i = 0; i < 256; ++i) {
            final int r = colorPalette[p++] & 0xFF;
            final int g = colorPalette[p++] & 0xFF;
            final int b = colorPalette[p++] & 0xFF;
            palette[i] = new D3(r, g, b);
        }
        final byte[] indexedPixels = new byte[h * w];
        p = 0;
        final int[] obj = new int[2];
        for (int y = 0; y < h; ++y) {
            final int y1w = (y + 1) * w;
            for (int x = 0; x < w; ++x) {
                final int argb = image[y * w + x];
                final int r = argb >> 16 & 0xFF;
                final int g = argb >> 8 & 0xFF;
                final int b = argb & 0xFF;
                findClosest(r, g, b, palette, obj);
                final int nextArgb = obj[0];
                indexedPixels[p++] = (byte)obj[1];
                final int errR = r - (nextArgb >> 16 & 0xFF);
                final int errG = g - (nextArgb >> 8 & 0xFF);
                final int errB = b - (nextArgb & 0xFF);
                if (x + 1 < w) {
                    image[y * w + x + 1] = applyFloyd(image[y * w + x + 1], errR, errG, errB, 7);
                    if (y + 1 < h) {
                        image[y1w + x + 1] = applyFloyd(image[y1w + x + 1], errR, errG, errB, 1);
                    }
                }
                if (y + 1 < h) {
                    image[y1w + x] = applyFloyd(image[y1w + x], errR, errG, errB, 5);
                    if (x - 1 >= 0) {
                        image[y1w + x - 1] = applyFloyd(image[y1w + x - 1], errR, errG, errB, 3);
                    }
                }
            }
        }
        return indexedPixels;
    }
    
    private static int applyFloyd(final int argb, final int errR, final int errG, final int errB, final int mul) {
        int r = argb >> 16 & 0xFF;
        int g = argb >> 8 & 0xFF;
        int b = argb & 0xFF;
        r += errR * mul / 16;
        g += errG * mul / 16;
        b += errB * mul / 16;
        if (r < 0) {
            r = 0;
        }
        else if (r > 255) {
            r = 255;
        }
        if (g < 0) {
            g = 0;
        }
        else if (g > 255) {
            g = 255;
        }
        if (b < 0) {
            b = 0;
        }
        else if (b > 255) {
            b = 255;
        }
        return r << 16 | g << 8 | b;
    }
}
