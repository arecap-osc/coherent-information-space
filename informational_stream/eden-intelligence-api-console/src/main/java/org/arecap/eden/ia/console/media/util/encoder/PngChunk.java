package org.arecap.eden.ia.console.media.util.encoder;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public final class PngChunk {
    public static final byte[] SIGNATURE;
    private static final byte[] IHDR;
    private static final byte[] PLTE;
    private static final byte[] tRNS;
    private static final byte[] IDAT;
    private static final byte[] IEND;
    private final byte[] length;
    private final byte[] name;
    private final byte[] data;
    
    private PngChunk(final byte[] length, final byte[] name, final byte[] data) {
        this.length = length;
        this.name = name;
        this.data = data;
    }
    
    public static PngChunk createHeaderChunk(final int width, final int height, final byte bitDepth, final byte colorType, final byte compression, final byte filter, final byte interlace) {
        final ByteBuffer buff = ByteBuffer.allocate(13);
        buff.putInt(width);
        buff.putInt(height);
        buff.put(bitDepth);
        buff.put(colorType);
        buff.put(compression);
        buff.put(filter);
        buff.put(interlace);
        final byte[] data = buff.array();
        return new PngChunk(intToBytes(13), PngChunk.IHDR, data);
    }
    
    public static PngChunk createPaleteChunk(final byte[] palBytes) {
        return new PngChunk(intToBytes(palBytes.length), PngChunk.PLTE, palBytes);
    }
    
    public static PngChunk createTrnsChunk(final byte[] trnsBytes) {
        return new PngChunk(intToBytes(trnsBytes.length), PngChunk.tRNS, trnsBytes);
    }
    
    public static PngChunk createDataChunk(final byte[] zLibBytes) {
        return new PngChunk(intToBytes(zLibBytes.length), PngChunk.IDAT, zLibBytes);
    }
    
    public static PngChunk createEndChunk() {
        return new PngChunk(intToBytes(0), PngChunk.IEND, new byte[0]);
    }
    
    public byte[] getCRCValue() {
        final CRC32 crc32 = new CRC32();
        crc32.update(this.name);
        crc32.update(this.data);
        final byte[] temp = longToBytes(crc32.getValue());
        return new byte[] { temp[4], temp[5], temp[6], temp[7] };
    }
    
    public static byte[] intToBytes(final int value) {
        return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
    }
    
    private static byte[] longToBytes(final long value) {
        return new byte[] { (byte)(value >> 56), (byte)(value >> 48), (byte)(value >> 40), (byte)(value >> 32), (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
    }
    
    public byte[] getLength() {
        return this.length;
    }
    
    public byte[] getName() {
        return this.name;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    static {
        SIGNATURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
        IHDR = new byte[] { 73, 72, 68, 82 };
        PLTE = new byte[] { 80, 76, 84, 69 };
        tRNS = new byte[] { 116, 82, 78, 83 };
        IDAT = new byte[] { 73, 68, 65, 84 };
        IEND = new byte[] { 73, 69, 78, 68 };
    }
}
