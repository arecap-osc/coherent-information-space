package org.hkrdi.eden.ggm.vaadin.console.media.util.encoder;

import java.util.BitSet;

public class PngBitReader {
    private int p;
    private BitSet bitset;
    private byte[] data;
    private final boolean hasSmallBits;
    
    public PngBitReader(final byte[] data, final boolean hasSmallBits) {
        this.hasSmallBits = hasSmallBits;
        final int totalBitLen = data.length * 8;
        if (this.hasSmallBits) {
            this.bitset = new BitSet(totalBitLen);
            int c = 0;
            for (final byte b : data) {
                for (int j = 7; j >= 0; --j) {
                    final boolean isOn = (b >> j & 0x1) == 0x1;
                    this.bitset.set(c, isOn);
                    ++c;
                }
            }
        }
        else {
            this.data = data;
        }
    }
    
    private int readBits(final int lenToRead) {
        int retVal = 0;
        if (this.hasSmallBits) {
            final BitSet smallSet = this.bitset.get(this.p, this.p + lenToRead);
            for (int i = 0; i < lenToRead; ++i) {
                if (smallSet.get(i)) {
                    retVal = (retVal << 1 | 0x1);
                }
                else {
                    retVal <<= 1;
                }
            }
            this.p += lenToRead;
        }
        else {
            for (int len = lenToRead / 8, i = 0; i < len; ++i) {
                retVal <<= 8;
                retVal |= (this.data[this.p / 8] & 0xFF);
                this.p += 8;
            }
        }
        return retVal;
    }
    
    public int getPositive(final int bitLen) {
        return this.readBits(bitLen);
    }
}
