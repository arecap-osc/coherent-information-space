package org.hkrdi.eden.ggm.vaadin.console.media.util.encoder;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private int bitCount;
    private int pointer;
    private final OutputStream stream;
    
    public BitWriter(final OutputStream stream) {
        this.stream = stream;
    }
    
    public void end() {
        while (this.bitCount > 0) {
            this.pointer <<= 1;
            ++this.bitCount;
            if (this.bitCount == 8) {
                try {
                    this.stream.write(this.pointer);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                this.pointer = 0;
                this.bitCount = 0;
            }
        }
    }
    
    public void writeBits(final int bits, int num) {
        while (num > 0) {
            final int cbit = Math.min(num, 8 - this.bitCount);
            this.pointer = (this.pointer << cbit | (bits >>> num - cbit & (1 << cbit) - 1));
            this.bitCount += cbit;
            num -= cbit;
            if (this.bitCount == 8) {
                try {
                    this.stream.write(this.pointer);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                this.pointer = 0;
                this.bitCount = 0;
            }
        }
    }
    
    public void writeByte() {
        if (this.bitCount == 0) {
            try {
                this.stream.write(0);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.writeBits(0, 8);
        }
    }
}
