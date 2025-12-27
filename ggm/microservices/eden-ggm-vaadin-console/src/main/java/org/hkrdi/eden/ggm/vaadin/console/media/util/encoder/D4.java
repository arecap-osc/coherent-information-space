package org.hkrdi.eden.ggm.vaadin.console.media.util.encoder;

public final class D4 {
    private final int a;
    private final int r;
    private final int g;
    private final int b;
    private final int argb;
    
    private D4(final int a, final int r, final int g, final int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.argb = (a << 24 | r << 16 | g << 8 | b);
    }
    
    private static int diff(final int a, final int r, final int g, final int b, final D4 pal) {
        final int Adiff = a - pal.a;
        final int Rdiff = r - pal.r;
        final int Gdiff = g - pal.g;
        final int Bdiff = b - pal.b;
        return Adiff * Adiff + Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
    }
    
    private static void findClosest(final int argb, final D4[] palette, final int[] obj) {
        D4 closest = palette[0];
        final int a = argb >> 24 & 0xFF;
        final int r = argb >> 16 & 0xFF;
        final int g = argb >> 8 & 0xFF;
        final int b = argb & 0xFF;
        int cDiff = diff(a, r, g, b, closest);
        int found = 0;
        for (int i = 1; i < 256; ++i) {
            final D4 n = palette[i];
            final int nDiff = diff(a, r, g, b, n);
            if (nDiff < cDiff) {
                closest = n;
                found = i;
                cDiff = nDiff;
            }
        }
        obj[0] = closest.argb;
        obj[1] = found;
    }
    
    public static byte[] process(final byte[] colorPalette, final byte[] trns, final int[] image, final int h, final int w) {
        int p = 0;
        final D4[] palette = new D4[256];
        for (int i = 0; i < 256; ++i) {
            final int a = trns[i] & 0xFF;
            final int r = colorPalette[p++] & 0xFF;
            final int g = colorPalette[p++] & 0xFF;
            final int b = colorPalette[p++] & 0xFF;
            palette[i] = new D4(a, r, g, b);
        }
        final byte[] indexedPixels = new byte[h * w];
        p = 0;
        final int[] obj = new int[2];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                final int argb = image[y * w + x];
                findClosest(argb, palette, obj);
                final int nextArgb = obj[0];
                indexedPixels[p++] = (byte)obj[1];
                final int a2 = argb >> 24 & 0xFF;
                final int r2 = argb >> 16 & 0xFF;
                final int g2 = argb >> 8 & 0xFF;
                final int b2 = argb & 0xFF;
                final int na = nextArgb >> 24 & 0xFF;
                final int nr = nextArgb >> 16 & 0xFF;
                final int ng = nextArgb >> 8 & 0xFF;
                final int nb = nextArgb & 0xFF;
                final int errA = a2 - na;
                final int errR = r2 - nr;
                final int errG = g2 - ng;
                final int errB = b2 - nb;
                if (x + 1 < w) {
                    image[y * w + x + 1] = applyFloyd(image[y * w + x + 1], errA, errR, errG, errB, 7);
                    if (y + 1 < h) {
                        image[(y + 1) * w + x + 1] = applyFloyd(image[(y + 1) * w + x + 1], errA, errR, errG, errB, 1);
                    }
                }
                if (y + 1 < h) {
                    image[(y + 1) * w + x] = applyFloyd(image[(y + 1) * w + x], errA, errR, errG, errB, 5);
                    if (x - 1 >= 0) {
                        image[(y + 1) * w + x - 1] = applyFloyd(image[(y + 1) * w + x - 1], errA, errR, errG, errB, 3);
                    }
                }
            }
        }
        return indexedPixels;
    }
    
    private static int applyFloyd(final int argb, final int errA, final int errR, final int errG, final int errB, final int mul) {
        int a = argb >> 24 & 0xFF;
        int r = argb >> 16 & 0xFF;
        int g = argb >> 8 & 0xFF;
        int b = argb & 0xFF;
        a += errA * mul / 16;
        r += errR * mul / 16;
        g += errG * mul / 16;
        b += errB * mul / 16;
        if (a < 0) {
            a = 0;
        }
        else if (a > 255) {
            a = 255;
        }
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
        return a << 24 | r << 16 | g << 8 | b;
    }
}
