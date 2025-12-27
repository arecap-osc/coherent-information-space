package org.arecap.eden.ia.console.media.util.encoder;

import java.util.Arrays;

public class Quant24 {
    private static final int IndexBits = 7;
    private static final int IndexBitsPlus = 8;
    private static final int DoubleIndexBits = 14;
    private static final int IndexCount = 129;
    private static final int TableLength = 2146689;
    private final long[] vwt;
    private final long[] vmr;
    private final long[] vmg;
    private final long[] vmb;
    private final double[] m2;
    
    public Quant24() {
        this.vwt = new long[2146689];
        this.vmr = new long[2146689];
        this.vmg = new long[2146689];
        this.vmb = new long[2146689];
        this.m2 = new double[2146689];
    }
    
    public byte[] getPalette(final int[] image, final int bw, final int bh) {
        this.histogram(image, bw, bh);
        this.M3d();
        final Cube[] cube = new Cube[256];
        this.buildCube(cube);
        final byte[] palette = new byte[768];
        int z = 0;
        for (int k = 0; k < 256; ++k) {
            final double weight = volume(cube[k], this.vwt);
            if (weight != 0.0) {
                palette[z++] = (byte)(volume(cube[k], this.vmr) / weight);
                palette[z++] = (byte)(volume(cube[k], this.vmg) / weight);
                palette[z++] = (byte)(volume(cube[k], this.vmb) / weight);
            }
            else {
                z += 3;
            }
        }
        return palette;
    }
    
    private static int indexify(final int r, final int g, final int b) {
        return (r << 14) + (r << 8) + (g << 7) + r + g + b;
    }
    
    private static double volume(final Cube cube, final long[] moment) {
        return (double)(moment[indexify(cube.R1, cube.G1, cube.B1)] - moment[indexify(cube.R1, cube.G1, cube.B0)] - moment[indexify(cube.R1, cube.G0, cube.B1)] + moment[indexify(cube.R1, cube.G0, cube.B0)] - moment[indexify(cube.R0, cube.G1, cube.B1)] + moment[indexify(cube.R0, cube.G1, cube.B0)] + moment[indexify(cube.R0, cube.G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)]);
    }
    
    private static long base(final Cube cube, final int direction, final long[] moment) {
        switch (direction) {
            case 2: {
                return -moment[indexify(cube.R0, cube.G1, cube.B1)] + moment[indexify(cube.R0, cube.G1, cube.B0)] + moment[indexify(cube.R0, cube.G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            }
            case 1: {
                return -moment[indexify(cube.R1, cube.G0, cube.B1)] + moment[indexify(cube.R1, cube.G0, cube.B0)] + moment[indexify(cube.R0, cube.G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            }
            case 0: {
                return -moment[indexify(cube.R1, cube.G1, cube.B0)] + moment[indexify(cube.R1, cube.G0, cube.B0)] + moment[indexify(cube.R0, cube.G1, cube.B0)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            }
            default: {
                return 0L;
            }
        }
    }
    
    private static long findTop(final Cube cube, final int direction, final int position, final long[] moment) {
        switch (direction) {
            case 2: {
                return moment[indexify(position, cube.G1, cube.B1)] - moment[indexify(position, cube.G1, cube.B0)] - moment[indexify(position, cube.G0, cube.B1)] + moment[indexify(position, cube.G0, cube.B0)];
            }
            case 1: {
                return moment[indexify(cube.R1, position, cube.B1)] - moment[indexify(cube.R1, position, cube.B0)] - moment[indexify(cube.R0, position, cube.B1)] + moment[indexify(cube.R0, position, cube.B0)];
            }
            case 0: {
                return moment[indexify(cube.R1, cube.G1, position)] - moment[indexify(cube.R1, cube.G0, position)] - moment[indexify(cube.R0, cube.G1, position)] + moment[indexify(cube.R0, cube.G0, position)];
            }
            default: {
                return 0L;
            }
        }
    }
    
    private void histogram(final int[] image, final int bw, final int bh) {
        final int mm = 1;
        int pp = 0;
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int val = image[pp++];
                final int r = val >> 16 & 0xFF;
                final int g = val >> 8 & 0xFF;
                final int b = val & 0xFF;
                final int inr = r >> 1;
                final int ing = g >> 1;
                final int inb = b >> 1;
                final int ind = indexify(inr + 1, ing + 1, inb + 1);
                final long[] vwt = this.vwt;
                final int n = ind;
                ++vwt[n];
                final long[] vmr = this.vmr;
                final int n2 = ind;
                vmr[n2] += r;
                final long[] vmg = this.vmg;
                final int n3 = ind;
                vmg[n3] += g;
                final long[] vmb = this.vmb;
                final int n4 = ind;
                vmb[n4] += b;
                final double[] m2 = this.m2;
                final int n5 = ind;
                m2[n5] += r * r + g * g + b * b;
            }
        }
    }
    
    private void M3d() {
        final long[] area = new long[129];
        final long[] areaR = new long[129];
        final long[] areaG = new long[129];
        final long[] areaB = new long[129];
        final double[] areaTemp = new double[129];
        for (int r = 1; r < 129; ++r) {
            Arrays.fill(area, 0L);
            Arrays.fill(areaR, 0L);
            Arrays.fill(areaG, 0L);
            Arrays.fill(areaB, 0L);
            Arrays.fill(areaTemp, 0.0);
            for (int g = 1; g < 129; ++g) {
                long ln = 0L;
                long lr = 0L;
                long lg = 0L;
                long lb = 0L;
                double line2 = 0.0;
                for (int b = 1; b < 129; ++b) {
                    final int ind1 = indexify(r, g, b);
                    ln += this.vwt[ind1];
                    lr += this.vmr[ind1];
                    lg += this.vmg[ind1];
                    lb += this.vmb[ind1];
                    line2 += this.m2[ind1];
                    final long[] array = area;
                    final int n = b;
                    array[n] += ln;
                    final long[] array2 = areaR;
                    final int n2 = b;
                    array2[n2] += lr;
                    final long[] array3 = areaG;
                    final int n3 = b;
                    array3[n3] += lg;
                    final long[] array4 = areaB;
                    final int n4 = b;
                    array4[n4] += lb;
                    final double[] array5 = areaTemp;
                    final int n5 = b;
                    array5[n5] += line2;
                    final int ind2 = ind1 - indexify(1, 0, 0);
                    this.vwt[ind1] = this.vwt[ind2] + area[b];
                    this.vmr[ind1] = this.vmr[ind2] + areaR[b];
                    this.vmg[ind1] = this.vmg[ind2] + areaG[b];
                    this.vmb[ind1] = this.vmb[ind2] + areaB[b];
                    this.m2[ind1] = this.m2[ind2] + areaTemp[b];
                }
            }
        }
    }
    
    private double variance(final Cube cube) {
        final double dr = volume(cube, this.vmr);
        final double dg = volume(cube, this.vmg);
        final double db = volume(cube, this.vmb);
        final double xx = this.m2[indexify(cube.R1, cube.G1, cube.B1)] - this.m2[indexify(cube.R1, cube.G1, cube.B0)] - this.m2[indexify(cube.R1, cube.G0, cube.B1)] + this.m2[indexify(cube.R1, cube.G0, cube.B0)] - this.m2[indexify(cube.R0, cube.G1, cube.B1)] + this.m2[indexify(cube.R0, cube.G1, cube.B0)] + this.m2[indexify(cube.R0, cube.G0, cube.B1)] - this.m2[indexify(cube.R0, cube.G0, cube.B0)];
        return xx - (dr * dr + dg * dg + db * db) / volume(cube, this.vwt);
    }
    
    private Object[] maximize(final Cube cube, final int direction, final int first, final int last, final double whole_r, final double whole_g, final double whole_b, final double whole_w) {
        final long base_r = base(cube, direction, this.vmr);
        final long base_g = base(cube, direction, this.vmg);
        final long base_b = base(cube, direction, this.vmb);
        final long base_w = base(cube, direction, this.vwt);
        double max = 0.0;
        int cut = -1;
        for (int i = first; i < last; ++i) {
            double half_r = (double)(base_r + findTop(cube, direction, i, this.vmr));
            double half_g = (double)(base_g + findTop(cube, direction, i, this.vmg));
            double half_b = (double)(base_b + findTop(cube, direction, i, this.vmb));
            double half_w = (double)(base_w + findTop(cube, direction, i, this.vwt));
            if (half_w != 0.0) {
                double temp = (half_r * half_r + half_g * half_g + half_b * half_b) / half_w;
                half_r = whole_r - half_r;
                half_g = whole_g - half_g;
                half_b = whole_b - half_b;
                half_w = whole_w - half_w;
                if (half_w != 0.0) {
                    temp += (half_r * half_r + half_g * half_g + half_b * half_b) / half_w;
                    if (temp > max) {
                        max = temp;
                        cut = i;
                    }
                }
            }
        }
        return new Object[] { cut, max };
    }
    
    private boolean cut(final Cube set1, final Cube set2) {
        final double whole_r = volume(set1, this.vmr);
        final double whole_g = volume(set1, this.vmg);
        final double whole_b = volume(set1, this.vmb);
        final double whole_w = volume(set1, this.vwt);
        Object[] temp = this.maximize(set1, 2, set1.R0 + 1, set1.R1, whole_r, whole_g, whole_b, whole_w);
        final int cutr = (int)temp[0];
        final double maxr = (double)temp[1];
        temp = this.maximize(set1, 1, set1.G0 + 1, set1.G1, whole_r, whole_g, whole_b, whole_w);
        final int cutg = (int)temp[0];
        final double maxg = (double)temp[1];
        temp = this.maximize(set1, 0, set1.B0 + 1, set1.B1, whole_r, whole_g, whole_b, whole_w);
        final int cutb = (int)temp[0];
        final double maxb = (double)temp[1];
        int dir;
        if (maxr >= maxg && maxr >= maxb) {
            dir = 2;
            if (cutr < 0) {
                return false;
            }
        }
        else if (maxg >= maxr && maxg >= maxb) {
            dir = 1;
        }
        else {
            dir = 0;
        }
        set2.R1 = set1.R1;
        set2.G1 = set1.G1;
        set2.B1 = set1.B1;
        switch (dir) {
            case 2: {
                final int n = cutr;
                set1.R1 = n;
                set2.R0 = n;
                set2.G0 = set1.G0;
                set2.B0 = set1.B0;
                break;
            }
            case 1: {
                final int n2 = cutg;
                set1.G1 = n2;
                set2.G0 = n2;
                set2.R0 = set1.R0;
                set2.B0 = set1.B0;
                break;
            }
            case 0: {
                final int n3 = cutb;
                set1.B1 = n3;
                set2.B0 = n3;
                set2.R0 = set1.R0;
                set2.G0 = set1.G0;
                break;
            }
        }
        set1.Volume = (set1.R1 - set1.R0) * (set1.G1 - set1.G0) * (set1.B1 - set1.B0);
        set2.Volume = (set2.R1 - set2.R0) * (set2.G1 - set2.G0) * (set2.B1 - set2.B0);
        return true;
    }
    
    private void buildCube(final Cube[] cube) {
        final double[] vv = new double[256];
        for (int i = 0; i < 256; ++i) {
            cube[i] = new Cube();
        }
        final Cube cube2 = cube[0];
        final Cube cube3 = cube[0];
        final Cube cube4 = cube[0];
        final int r0 = 0;
        cube4.B0 = r0;
        cube3.G0 = r0;
        cube2.R0 = r0;
        final Cube cube5 = cube[0];
        final Cube cube6 = cube[0];
        final Cube cube7 = cube[0];
        final int r2 = 128;
        cube7.B1 = r2;
        cube6.G1 = r2;
        cube5.R1 = r2;
        int next = 0;
        for (int j = 1; j < 256; ++j) {
            if (this.cut(cube[next], cube[j])) {
                vv[next] = ((cube[next].Volume > 1) ? this.variance(cube[next]) : 0.0);
                vv[j] = ((cube[j].Volume > 1) ? this.variance(cube[j]) : 0.0);
            }
            else {
                vv[next] = 0.0;
                --j;
            }
            next = 0;
            double temp = vv[0];
            for (int k = 1; k <= j; ++k) {
                if (vv[k] > temp) {
                    temp = vv[k];
                    next = k;
                }
            }
            if (temp <= 0.0) {
                break;
            }
        }
    }
    
    static class Cube
    {
        int R0;
        int R1;
        int G0;
        int G1;
        int B0;
        int B1;
        int Volume;
    }
}
