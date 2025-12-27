package org.arecap.eden.ia.console.media.util.encoder;

import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.Deflater;

public class PngEncoder {
    private boolean compress;
    private boolean optimizeBasedOnColors;

    public void write(final BufferedImage image, final OutputStream outputStream) throws IOException {
        BufferedImage img = ImageUtils.fixSubBufferedImage(image);
        if (this.optimizeBasedOnColors) {
            img = getColorCountIndexed(img);
        }
        if (this.compress) {
            this.compress8Bit(img, outputStream);
        }
        else {
            this.compressNormal(img, outputStream);
        }
    }
    
    public void write(final BufferedImage image, final File file) throws IOException {
        final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        this.write(image, bos);
        bos.flush();
        bos.close();
    }
    
    private static BufferedImage getColorCountIndexed(final BufferedImage image) {
        byte[] pixBytes = null;
        int[] pixInts = null;
        final int iw = image.getWidth();
        final int ih = image.getHeight();
        int count = 0;
        final int dim = image.getWidth() * image.getHeight();
        int p = 0;
        int a = 0;
        final int[] countMap = new int[255];
        boolean hasBlack = false;
        final int nComp = image.getColorModel().getNumComponents();
        switch (image.getType()) {
            case 5:
            case 6:
            case 7: {
                pixBytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < dim; ++i) {
                    if (nComp == 4) {
                        a = (pixBytes[p++] & 0xFF);
                    }
                    final int b = pixBytes[p++] & 0xFF;
                    final int g = pixBytes[p++] & 0xFF;
                    final int r = pixBytes[p++] & 0xFF;
                    final int v = a << 24 | r << 16 | g << 8 | b;
                    if (v == 0) {
                        hasBlack = true;
                    }
                    else {
                        if (count >= 255) {
                            return image;
                        }
                        if (!checkColorInArray(countMap, count + 1, v)) {
                            countMap[count++] = v;
                        }
                    }
                }
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                pixInts = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                for (final int v : pixInts) {
                    if (v == 0) {
                        hasBlack = true;
                    }
                    else {
                        if (count >= 255) {
                            return image;
                        }
                        if (!checkColorInArray(countMap, count + 1, v)) {
                            countMap[count++] = v;
                        }
                    }
                }
                break;
            }
            default: {
                return image;
            }
        }
        final int zeroCount = hasBlack ? 1 : 0;
        final int nColors = count + zeroCount;
        if (nColors <= 256) {
            return getOptimizedImage(iw, ih, nColors, nComp, pixBytes, pixInts, countMap);
        }
        return image;
    }
    
    private static boolean checkColorInArray(final int[] arr, final int max, final int color) {
        final int temp = arr[0];
        for (int i = 0; i < max; ++i) {
            if (arr[i] == color) {
                arr[0] = arr[i];
                arr[i] = temp;
                return true;
            }
        }
        return false;
    }
    
    private static BufferedImage getOptimizedImage(final int iw, final int ih, final int nColors, final int nComp, final byte[] pixBytes, final int[] pixInts, final int[] countMap) {
        int a = 0;
        final int[] palette = new int[nColors];
        System.arraycopy(countMap, 0, palette, 0, nColors);
        int p = 0;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final BitWriter bw = new BitWriter(bos);
        final int bps = getBps(nColors);
        final int paletteLen = 1 << bps;
        final int gap = 8 - iw * bps % 8;
        for (int y = 0; y < ih; ++y) {
            for (int x = 0; x < iw; ++x) {
                int v;
                if (pixBytes != null) {
                    if (nComp == 4) {
                        a = (pixBytes[p++] & 0xFF);
                    }
                    final int b = pixBytes[p++] & 0xFF;
                    final int g = pixBytes[p++] & 0xFF;
                    final int r = pixBytes[p++] & 0xFF;
                    v = (a << 24 | r << 16 | g << 8 | b);
                }
                else {
                    v = pixInts[p++];
                }
                int f = 0;
                for (int i = 0; i < nColors; ++i) {
                    if (v == palette[i]) {
                        f = i;
                        break;
                    }
                }
                bw.writeBits(f, bps);
            }
            if (gap != 8) {
                bw.writeBits(0, gap);
            }
        }
        bw.end();
        final byte[] aa = new byte[paletteLen];
        final byte[] rr = new byte[paletteLen];
        final byte[] gg = new byte[paletteLen];
        final byte[] bb = new byte[paletteLen];
        for (int j = 0; j < nColors; ++j) {
            final int v = palette[j];
            aa[j] = (byte)(v >> 24 & 0xFF);
            rr[j] = (byte)(v >> 16 & 0xFF);
            gg[j] = (byte)(v >> 8 & 0xFF);
            bb[j] = (byte)(v & 0xFF);
        }
        IndexColorModel cm;
        if (nComp == 4) {
            cm = new IndexColorModel(bps, paletteLen, rr, gg, bb, aa);
        }
        else {
            cm = new IndexColorModel(bps, paletteLen, rr, gg, bb);
        }
        BufferedImage img;
        if (bps <= 4) {
            img = new BufferedImage(iw, ih, 12, cm);
        }
        else {
            img = new BufferedImage(iw, ih, 13, cm);
        }
        final byte[] dd = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(bos.toByteArray(), 0, dd, 0, dd.length);
        return img;
    }

    private static int getBps(final int nColors) {
        int bps = 0;
        switch (nColors) {
            case 1:
            case 2: {
                bps = 1;
                break;
            }
            case 3:
            case 4: {
                bps = 2;
                break;
            }
            default: {
                bps = 8;
                break;
            }
        }
        return bps;
    }

    public boolean isCompressed() {
        return this.compress;
    }

    public void setCompressed(final boolean compress) {
        this.compress = compress;
    }

    public boolean isOptimizeBasedOnColors() {
        return this.optimizeBasedOnColors;
    }

    public void setOptimizeBasedOnColors(final boolean optimizeBasedOnColors) {
        this.optimizeBasedOnColors = optimizeBasedOnColors;
    }

    private void compressNormal(final BufferedImage image, final OutputStream outputStream) throws IOException {
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        final ColorModel colorModel = image.getColorModel();
        final boolean hasAlpha = colorModel.hasAlpha();
        final int pLen = colorModel.getPixelSize();
        int nComp = colorModel.getNumComponents();
        final boolean isIndexed = colorModel instanceof IndexColorModel;
        final int bitDepth = calculateBitDepth(pLen, nComp);
        int colType;
        if (isIndexed) {
            colType = 3;
            nComp = 1;
        }
        else if (nComp < 3) {
            colType = (hasAlpha ? 4 : 0);
        }
        else if (bitDepth < 8) {
            colType = (hasAlpha ? 4 : 0);
        }
        else {
            colType = (hasAlpha ? 6 : 2);
        }
        outputStream.write(PngChunk.SIGNATURE);
        PngChunk chunk = PngChunk.createHeaderChunk(bw, bh, (byte)bitDepth, (byte)colType, (byte)0, (byte)0, (byte)0);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        byte[] pixels;
        if (isIndexed && bitDepth != 8) {
            pixels = getIndexedPaletteData(image);
        }
        else {
            pixels = getPixelData(image, bitDepth, nComp, bw, bh);
        }
        if (isIndexed) {
            handleindexed(outputStream, (IndexColorModel)colorModel, bitDepth, pixels);
        }
        pixels = this.getDeflatedData(pixels);
        chunk = PngChunk.createDataChunk(pixels);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        chunk = PngChunk.createEndChunk();
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
    }

    private static void handleindexed(final OutputStream outputStream, final IndexColorModel colorModel, final int bitDepth, final byte[] pixels) throws IOException {
        int indexModelMapSize = colorModel.getMapSize();
        final int[] rgbs = new int[indexModelMapSize];
        colorModel.getRGBs(rgbs);
        if (bitDepth == 8) {
            indexModelMapSize = reduceIndexMap(indexModelMapSize, rgbs, pixels);
        }
        final ByteBuffer bb = ByteBuffer.allocate(indexModelMapSize * 3);
        for (final int color : rgbs) {
            bb.put(new byte[] { (byte)(color >> 16), (byte)(color >> 8), (byte)color });
        }
        PngChunk chunk = PngChunk.createPaleteChunk(bb.array());
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        if (colorModel.getNumComponents() == 4) {
            final byte[] trnsBytes = new byte[indexModelMapSize];
            for (int j = 0; j < indexModelMapSize; ++j) {
                trnsBytes[j] = (byte)(rgbs[j] >> 24);
            }
            chunk = PngChunk.createTrnsChunk(trnsBytes);
            outputStream.write(chunk.getLength());
            outputStream.write(chunk.getName());
            outputStream.write(chunk.getData());
            outputStream.write(chunk.getCRCValue());
        }
    }

    private static int reduceIndexMap(final int indexModelMapSize, final int[] rgbs, final byte[] pixels) {
        int numColors = 0;
        final byte[] indexMap = new byte[indexModelMapSize];
        final Map<Integer, Integer> colors = new LinkedHashMap<Integer, Integer>();
        for (int i = 0; i < indexModelMapSize; ++i) {
            final int color = rgbs[i];
            if (!colors.containsKey(color)) {
                indexMap[i] = (byte)numColors;
                colors.put(color, numColors);
                ++numColors;
            }
            else {
                indexMap[i] = (byte)(int)colors.get(color);
            }
        }
        if (numColors < indexModelMapSize) {
            for (int i = 0; i < pixels.length; ++i) {
                pixels[i] = indexMap[pixels[i] & 0xFF];
            }
            final Set<Integer> colorSet = colors.keySet();
            int temp = 0;
            for (final int c : colorSet) {
                rgbs[temp++] = c;
            }
        }
        return numColors;
    }

    private static boolean isAlphaUsed(final byte[] trnsBytes) {
        for (final byte trn : trnsBytes) {
            if (trn != -1) {
                return true;
            }
        }
        return false;
    }

    private void compress8Bit(final BufferedImage image, final OutputStream outputStream) throws IOException {
        final int type = image.getType();
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        final int dim = bh * bw;
        int[] argb = null;
        int[] rgb = null;
        switch (type) {
            case 5: {
                rgb = handleType3ByteBGR(image);
                break;
            }
            case 6:
            case 7: {
                argb = handleType4ByteABGR(image);
                break;
            }
            case 4: {
                rgb = handleTypeIntBGR(image);
                break;
            }
            case 2:
            case 3: {
                argb = handleTypeIntRGBorARGB(image);
                break;
            }
            case 1: {
                rgb = handleTypeIntRGBorARGB(image);
                break;
            }
            default: {
                this.compressNormal(image, outputStream);
                return;
            }
        }
        if (argb != null) {
            this.compressARGB(outputStream, bh, bw, dim, argb);
        }
        else {
            this.compressRGB(outputStream, bh, bw, dim, rgb);
        }
    }

    private void compressRGB(final OutputStream outputStream, final int bh, final int bw, final int dim, final int[] rgb) throws IOException {
        final byte[] indexedPixels = new byte[dim + bh];
        final Object[] objs = getIndexedMap(rgb, bw, bh);
        byte[] qBytes;
        byte[] colorPalette;
        if (objs != null) {
            qBytes = (byte[])objs[0];
            colorPalette = (byte[])objs[1];
        }
        else {
            final Quant24 wu = new Quant24();
            colorPalette = wu.getPalette(rgb, bw, bh);
            qBytes = D3.process(colorPalette, rgb, bh, bw);
        }
        int k = 0;
        int z = 0;
        for (int i = 0; i < bh; ++i) {
            indexedPixels[z++] = 0;
            for (int j = 0; j < bw; ++j) {
                indexedPixels[z++] = qBytes[k++];
            }
        }
        this.writePNGToStream(outputStream, null, colorPalette, indexedPixels, bw, bh);
    }

    private void compressARGB(final OutputStream outputStream, final int bh, final int bw, final int dim, final int[] argb) throws IOException {
        final byte[] indexedPixels = new byte[dim + bh];
        final Object[] objs = getIndexedMap(argb, bw, bh);
        byte[] qBytes;
        byte[] colorPalette;
        byte[] trnsBytes;
        if (objs != null) {
            qBytes = (byte[])objs[0];
            colorPalette = (byte[])objs[1];
            trnsBytes = (byte[])objs[2];
            if (!isAlphaUsed(trnsBytes)) {
                trnsBytes = null;
            }
        }
        else {
            final Quant32 wu = new Quant32();
            final Object[] obj = wu.getPalette(argb, bw, bh);
            colorPalette = (byte[])obj[0];
            trnsBytes = (byte[])obj[1];
            qBytes = D4.process(colorPalette, trnsBytes, argb, bh, bw);
            if (!isAlphaUsed(trnsBytes)) {
                trnsBytes = null;
            }
        }
        int k = 0;
        int z = 0;
        for (int i = 0; i < bh; ++i) {
            indexedPixels[z++] = 0;
            for (int j = 0; j < bw; ++j) {
                indexedPixels[z++] = qBytes[k++];
            }
        }
        this.writePNGToStream(outputStream, trnsBytes, colorPalette, indexedPixels, bw, bh);
    }

    private static int[] handleTypeIntRGBorARGB(final BufferedImage image) {
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        int p = 0;
        int pp = 0;
        final int[] intPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        final int[] rgb = new int[bh * bw];
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                rgb[pp++] = intPixels[p++];
            }
        }
        return rgb;
    }

    private static int[] handleTypeIntBGR(final BufferedImage image) {
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        int p = 0;
        int pp = 0;
        final int[] intPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        final int[] rgb = new int[bh * bw];
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int val = intPixels[p++];
                final int b = val >> 16 & 0xFF;
                final int g = val >> 8 & 0xFF;
                final int r = val & 0xFF;
                rgb[pp++] = (r << 16 | g << 8 | b);
            }
        }
        return rgb;
    }

    private static int[] handleType4ByteABGR(final BufferedImage image) {
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        int p = 0;
        int pp = 0;
        final byte[] pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        final int[] argb = new int[bh * bw];
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int a = pixels[p++] & 0xFF;
                final int b = pixels[p++] & 0xFF;
                final int g = pixels[p++] & 0xFF;
                final int r = pixels[p++] & 0xFF;
                argb[pp++] = (a << 24 | r << 16 | g << 8 | b);
            }
        }
        return argb;
    }

    private static int[] handleType3ByteBGR(final BufferedImage image) {
        final int bh = image.getHeight();
        final int bw = image.getWidth();
        int p = 0;
        int pp = 0;
        final byte[] pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        final int[] rgb = new int[bh * bw];
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int b = pixels[p++] & 0xFF;
                final int g = pixels[p++] & 0xFF;
                final int r = pixels[p++] & 0xFF;
                rgb[pp++] = (r << 16 | g << 8 | b);
            }
        }
        return rgb;
    }

    private void writePNGToStream(final OutputStream outputStream, final byte[] trnsBytes, final byte[] colorPalette, final byte[] indexedPixels, final int bw, final int bh) throws IOException {
        outputStream.write(PngChunk.SIGNATURE);
        PngChunk chunk = PngChunk.createHeaderChunk(bw, bh, (byte)8, (byte)3, (byte)0, (byte)0, (byte)0);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        final byte[] pixels = this.getDeflatedData(indexedPixels);
        chunk = PngChunk.createPaleteChunk(colorPalette);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        if (trnsBytes != null) {
            chunk = PngChunk.createTrnsChunk(trnsBytes);
            outputStream.write(chunk.getLength());
            outputStream.write(chunk.getName());
            outputStream.write(chunk.getData());
            outputStream.write(chunk.getCRCValue());
        }
        chunk = PngChunk.createDataChunk(pixels);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        chunk = PngChunk.createEndChunk();
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
    }

    private static Object[] getIndexedMap(final int[] pixel, final int bw, final int bh) {
        final int[] colors = new int[256];
        int c = 0;
        int p = 0;
        int t = 0;
        final byte[] indexedBytes = new byte[bh * bw];
        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        int pp = 0;
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int key = pixel[pp++];
                final Integer val = map.get(key);
                if (val == null) {
                    if (c > 255) {
                        return null;
                    }
                    map.put(key, c);
                    colors[c] = key;
                    indexedBytes[p++] = (byte)c;
                    ++c;
                }
                else {
                    indexedBytes[p++] = (byte)(int)val;
                }
            }
        }
        final byte[] palette = new byte[c * 3];
        final byte[] trns = new byte[c];
        p = 0;
        for (final int val2 : colors) {
            trns[t++] = (byte)(val2 >> 24 & 0xFF);
            palette[p++] = (byte)(val2 >> 16 & 0xFF);
            palette[p++] = (byte)(val2 >> 8 & 0xFF);
            palette[p++] = (byte)(val2 & 0xFF);
        }
        return new Object[] { indexedBytes, palette, trns };
    }

    private static byte[] getIndexedPaletteData(final BufferedImage buff) throws IOException {
        final byte[] pixels = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        final int ih = buff.getHeight();
        final int len = pixels.length / ih;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int k = 0;
            for (int i = 0; i < ih; ++i) {
                bos.write(0);
                final byte[] temp = new byte[len];
                System.arraycopy(pixels, k, temp, 0, len);
                bos.write(temp);
                k += len;
            }
            bos.close();
            final byte[] byteArray = bos.toByteArray();
            bos.close();
            return byteArray;
        }
        catch (Throwable t) {
            try {
                bos.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }

    private static byte[] getBIPixelData(final BufferedImage buff, final int bitDepth, final int bw) throws IOException {
        final byte[] pixels = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        final int multi = (bitDepth == 1) ? 8 : ((bitDepth == 2) ? 4 : 2);
        final PngBitReader reader = new PngBitReader(pixels, true);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final BitWriter writer = new BitWriter(bos);
        int cc2 = 0;
        for (int iter = pixels.length * multi, i = 0; i < iter; ++i) {
            if (cc2 == 0) {
                writer.writeByte();
            }
            writer.writeBits(reader.getPositive(bitDepth), bitDepth);
            if (++cc2 == bw) {
                cc2 = 0;
            }
        }
        writer.end();
        bos.flush();
        bos.close();
        return bos.toByteArray();
    }
    
    private static byte[] getUnknowPixelData(final BufferedImage buff, final int bw, final int bh, final int nComp) {
        final ByteBuffer out = ByteBuffer.allocate(bw * bh * nComp + bh);
        final int[] pixInt = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        int clm = 0;
        for (final int i : pixInt) {
            if (clm == 0) {
                out.put((byte)0);
            }
            final byte[] t = PngChunk.intToBytes(i);
            switch (nComp) {
                case 4: {
                    out.put(new byte[] { t[1], t[2], t[3], t[0] });
                    break;
                }
                case 3: {
                    out.put(new byte[] { t[1], t[2], t[3] });
                    break;
                }
                case 2: {
                    out.put(new byte[] { t[2], t[3] });
                    break;
                }
                case 1: {
                    out.put(t[3]);
                    break;
                }
            }
            if (++clm == bw) {
                clm = 0;
            }
        }
        return out.array();
    }
    
    private static byte[] getShortPixelData(final BufferedImage buff, final int bw, final int bh, final int nComp) {
        final short[] shortPixels = ((DataBufferUShort)buff.getRaster().getDataBuffer()).getData();
        final ByteBuffer bos16 = ByteBuffer.allocate(shortPixels.length * 2 + bh);
        int scol = 0;
        for (int p = 0; p < shortPixels.length; p += nComp) {
            if (scol == 0) {
                bos16.put((byte)0);
            }
            for (int i = 0; i < nComp; ++i) {
                bos16.putShort(shortPixels[p + i]);
            }
            if (++scol == bw) {
                scol = 0;
            }
        }
        return bos16.array();
    }
    
    private static byte[] getPixelData(final BufferedImage buff, final int bitDepth, final int nComp, final int bw, final int bh) throws IOException {
        final ColorModel model = buff.getColorModel();
        switch (bitDepth) {
            case 1:
            case 2:
            case 4: {
                return getBIPixelData(buff, bitDepth, bw);
            }
            case 8: {
                return get8BitData(buff, nComp, bw, bh, model);
            }
            case 16: {
                return getShortPixelData(buff, bw, bh, nComp);
            }
            default: {
                return null;
            }
        }
    }
    
    private static byte[] get8BitData(final BufferedImage buff, final int nComp, final int bw, final int bh, final ColorModel model) {
        final DataBuffer dataBuff = buff.getRaster().getDataBuffer();
        switch (dataBuff.getDataType()) {
            case 0: {
                switch (buff.getType()) {
                    case 5: {
                        return handleType3(nComp, buff, bh, bw);
                    }
                    case 6:
                    case 7: {
                        return handleType4(nComp, buff, bh, bw);
                    }
                    default: {
                        return handleDefaultTypeByte(nComp, buff, bh, bw);
                    }
                }
            }
            case 3: {
                if (buff.getType() == 2 || buff.getType() == 3) {
                    return handleTypeIntARGB(bw, bh, buff);
                }
                if (buff.getType() == 1) {
                    return handleTypeIntRGB(bw, bh, buff);
                }
                if (buff.getType() == 4) {
                    return handleTypeIntBGR(bw, bh, buff);
                }
                if (model instanceof DirectColorModel) {
                    return handleDirectColorModel(model, bw, bh, buff);
                }
                return getUnknowPixelData(buff, bw, bh, nComp);
            }
            default: {
                return null;
            }
        }
    }
    
    private static byte[] handleDirectColorModel(final ColorModel model, final int bw, final int bh, final BufferedImage buff) {
        int k = 0;
        int p = 0;
        final int[] pixInt = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        final DirectColorModel dm = (DirectColorModel)model;
        final long rMask = getMaskValue(dm.getRedMask());
        final long gMask = getMaskValue(dm.getGreenMask());
        final long bMask = getMaskValue(dm.getBlueMask());
        final long aMask = getMaskValue(dm.getAlphaMask());
        final byte[] output = new byte[bw * bh * 4 + bh];
        for (int i = 0; i < bh; ++i) {
            output[k++] = 0;
            for (int j = 0; j < bw; ++j) {
                final int val = pixInt[p++];
                output[k++] = (byte)(val >> (int)rMask);
                output[k++] = (byte)(val >> (int)gMask);
                output[k++] = (byte)(val >> (int)bMask);
                output[k++] = (byte)(val >> (int)aMask);
            }
        }
        return output;
    }
    
    private static byte[] handleTypeIntBGR(final int bw, final int bh, final BufferedImage buff) {
        int k = 0;
        int p = 0;
        final byte[] output = new byte[bw * bh * 3 + bh];
        final int[] pixInt = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < bh; ++i) {
            output[k++] = 0;
            for (int j = 0; j < bw; ++j) {
                final int val = pixInt[p++];
                output[k++] = (byte)val;
                output[k++] = (byte)(val >> 8);
                output[k++] = (byte)(val >> 16);
            }
        }
        return output;
    }
    
    private static byte[] handleTypeIntRGB(final int bw, final int bh, final BufferedImage buff) {
        int p = 0;
        int k = 0;
        final byte[] output = new byte[bw * bh * 3 + bh];
        final int[] pixInt = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < bh; ++i) {
            output[k++] = 0;
            for (int j = 0; j < bw; ++j) {
                final int val = pixInt[p++];
                output[k++] = (byte)(val >> 16);
                output[k++] = (byte)(val >> 8);
                output[k++] = (byte)val;
            }
        }
        return output;
    }
    
    private static byte[] handleTypeIntARGB(final int bw, final int bh, final BufferedImage buff) {
        int k = 0;
        int p = 0;
        final byte[] output = new byte[bw * bh * 4 + bh];
        final int[] pixInt = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < bh; ++i) {
            output[k++] = 0;
            for (int j = 0; j < bw; ++j) {
                final int val = pixInt[p++];
                output[k++] = (byte)(val >> 16);
                output[k++] = (byte)(val >> 8);
                output[k++] = (byte)val;
                output[k++] = (byte)(val >> 24);
            }
        }
        return output;
    }
    
    private static byte[] handleDefaultTypeByte(final int nComp, final BufferedImage buff, final int bh, final int bw) {
        final byte[] pixels8 = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        final ByteBuffer bOut = ByteBuffer.allocate(bw * bh * nComp + bh);
        final int pLen = pixels8.length;
        int col = 0;
        for (int p = 0; p < pLen; p += nComp) {
            if (col == 0) {
                bOut.put((byte)0);
            }
            for (int i = 0; i < nComp; ++i) {
                bOut.put(pixels8[p + i]);
            }
            if (++col == bw) {
                col = 0;
            }
        }
        return bOut.array();
    }
    
    private static byte[] handleType4(final int nComp, final BufferedImage buff, final int bh, final int bw) {
        final byte[] pixels8 = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        final ByteBuffer bOut = ByteBuffer.allocate(bw * bh * nComp + bh);
        final int pLen = pixels8.length;
        int col = 0;
        for (int p = 0; p < pLen; p += nComp) {
            if (col == 0) {
                bOut.put((byte)0);
            }
            final byte[] b = { pixels8[p + 3], pixels8[p + 2], pixels8[p + 1], pixels8[p] };
            bOut.put(b);
            if (++col == bw) {
                col = 0;
            }
        }
        return bOut.array();
    }
    
    private static byte[] handleType3(final int nComp, final BufferedImage buff, final int bh, final int bw) {
        final byte[] pixels8 = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        final int pLen = pixels8.length;
        int col = 0;
        final ByteBuffer bOut = ByteBuffer.allocate(bw * bh * nComp + bh);
        for (int p = 0; p < pLen; p += nComp) {
            if (col == 0) {
                bOut.put((byte)0);
            }
            final byte[] b = { pixels8[p + 2], pixels8[p + 1], pixels8[p] };
            bOut.put(b);
            if (++col == bw) {
                col = 0;
            }
        }
        return bOut.array();
    }
    
    private static int getMaskValue(final int mask) {
        switch (mask) {
            case 255: {
                return 0;
            }
            case 65280: {
                return 8;
            }
            case 16711680: {
                return 16;
            }
            default: {
                return 24;
            }
        }
    }
    
    private static int calculateBitDepth(final int pixelBits, final int nComp) {
        if (pixelBits < 8) {
            return pixelBits;
        }
        final int c = pixelBits / nComp;
        if (c == 8 || c == 16) {
            return c;
        }
        return 8;
    }
    
    private byte[] getDeflatedData(final byte[] pixels) throws IOException {
        Deflater deflater;
        if (this.compress || this.optimizeBasedOnColors) {
            deflater = new Deflater(9);
        }
        else {
            deflater = new Deflater(1);
        }
        deflater.setInput(pixels);
        final int min = Math.min(pixels.length / 2, 4096);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(min);
        try {
            deflater.finish();
            final byte[] buffer = new byte[min];
            while (!deflater.finished()) {
                final int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            deflater.end();
            outputStream.close();
            final byte[] byteArray = outputStream.toByteArray();
            outputStream.close();
            return byteArray;
        }
        catch (Throwable t) {
            try {
                outputStream.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
}
