package org.arecap.eden.ia.console.media.util.encoder;

public class Quant32 {
    private static final int IndexBits = 6;
    private static final int IndexBitsPlus = 7;
    private static final int DoubleIndexBits = 12;
    private static final int IndexAlphaBits = 3;
    private static final int SumBits = 9;
    private static final int IndexCount = 65;
    private static final int IndexAlphaCount = 9;
    private static final int TableLength = 2471625;
    private final long[] vwt;
    private final long[] vmr;
    private final long[] vmg;
    private final long[] vmb;
    private final long[] vma;
    private final double[] m2;
    
    public Quant32() {
        this.vwt = new long[2471625];
        this.vmr = new long[2471625];
        this.vmg = new long[2471625];
        this.vmb = new long[2471625];
        this.vma = new long[2471625];
        this.m2 = new double[2471625];
    }
    
    private static int indexify(final int r, final int g, final int b, final int a) {
        return (r << 15) + (r << 10) + (g << 9) + (r << 12) + (r << 7) + (g << 6) + (r + g + b << 3) + r + g + b + a;
    }
    
    private static double volume(final Cube cube, final long[] moment) {
        return (double)(moment[indexify(cube.R1, cube.G1, cube.B1, cube.A1)] - moment[indexify(cube.R1, cube.G1, cube.B1, cube.A0)] - moment[indexify(cube.R1, cube.G1, cube.B0, cube.A1)] + moment[indexify(cube.R1, cube.G1, cube.B0, cube.A0)] - moment[indexify(cube.R1, cube.G0, cube.B1, cube.A1)] + moment[indexify(cube.R1, cube.G0, cube.B1, cube.A0)] + moment[indexify(cube.R1, cube.G0, cube.B0, cube.A1)] - moment[indexify(cube.R1, cube.G0, cube.B0, cube.A0)] - moment[indexify(cube.R0, cube.G1, cube.B1, cube.A1)] + moment[indexify(cube.R0, cube.G1, cube.B1, cube.A0)] + moment[indexify(cube.R0, cube.G1, cube.B0, cube.A1)] - moment[indexify(cube.R0, cube.G1, cube.B0, cube.A0)] + moment[indexify(cube.R0, cube.G0, cube.B1, cube.A1)] - moment[indexify(cube.R0, cube.G0, cube.B1, cube.A0)] - moment[indexify(cube.R0, cube.G0, cube.B0, cube.A1)] + moment[indexify(cube.R0, cube.G0, cube.B0, cube.A0)]);
    }
    
    private static long base(final Cube cube, final int direction, final long[] moment) {
        switch (direction) {
            case 3: {
                return -moment[indexify(cube.R0, cube.G1, cube.B1, cube.A1)] + moment[indexify(cube.R0, cube.G1, cube.B1, cube.A0)] + moment[indexify(cube.R0, cube.G1, cube.B0, cube.A1)] - moment[indexify(cube.R0, cube.G1, cube.B0, cube.A0)] + moment[indexify(cube.R0, cube.G0, cube.B1, cube.A1)] - moment[indexify(cube.R0, cube.G0, cube.B1, cube.A0)] - moment[indexify(cube.R0, cube.G0, cube.B0, cube.A1)] + moment[indexify(cube.R0, cube.G0, cube.B0, cube.A0)];
            }
            case 2: {
                return -moment[indexify(cube.R1, cube.G0, cube.B1, cube.A1)] + moment[indexify(cube.R1, cube.G0, cube.B1, cube.A0)] + moment[indexify(cube.R1, cube.G0, cube.B0, cube.A1)] - moment[indexify(cube.R1, cube.G0, cube.B0, cube.A0)] + moment[indexify(cube.R0, cube.G0, cube.B1, cube.A1)] - moment[indexify(cube.R0, cube.G0, cube.B1, cube.A0)] - moment[indexify(cube.R0, cube.G0, cube.B0, cube.A1)] + moment[indexify(cube.R0, cube.G0, cube.B0, cube.A0)];
            }
            case 1: {
                return -moment[indexify(cube.R1, cube.G1, cube.B0, cube.A1)] + moment[indexify(cube.R1, cube.G1, cube.B0, cube.A0)] + moment[indexify(cube.R1, cube.G0, cube.B0, cube.A1)] - moment[indexify(cube.R1, cube.G0, cube.B0, cube.A0)] + moment[indexify(cube.R0, cube.G1, cube.B0, cube.A1)] - moment[indexify(cube.R0, cube.G1, cube.B0, cube.A0)] - moment[indexify(cube.R0, cube.G0, cube.B0, cube.A1)] + moment[indexify(cube.R0, cube.G0, cube.B0, cube.A0)];
            }
            case 0: {
                return -moment[indexify(cube.R1, cube.G1, cube.B1, cube.A0)] + moment[indexify(cube.R1, cube.G1, cube.B0, cube.A0)] + moment[indexify(cube.R1, cube.G0, cube.B1, cube.A0)] - moment[indexify(cube.R1, cube.G0, cube.B0, cube.A0)] + moment[indexify(cube.R0, cube.G1, cube.B1, cube.A0)] - moment[indexify(cube.R0, cube.G1, cube.B0, cube.A0)] - moment[indexify(cube.R0, cube.G0, cube.B1, cube.A0)] + moment[indexify(cube.R0, cube.G0, cube.B0, cube.A0)];
            }
            default: {
                return 0L;
            }
        }
    }
    
    private static long findTop(final Cube cube, final int direction, final int position, final long[] moment) {
        switch (direction) {
            case 3: {
                return moment[indexify(position, cube.G1, cube.B1, cube.A1)] - moment[indexify(position, cube.G1, cube.B1, cube.A0)] - moment[indexify(position, cube.G1, cube.B0, cube.A1)] + moment[indexify(position, cube.G1, cube.B0, cube.A0)] - moment[indexify(position, cube.G0, cube.B1, cube.A1)] + moment[indexify(position, cube.G0, cube.B1, cube.A0)] + moment[indexify(position, cube.G0, cube.B0, cube.A1)] - moment[indexify(position, cube.G0, cube.B0, cube.A0)];
            }
            case 2: {
                return moment[indexify(cube.R1, position, cube.B1, cube.A1)] - moment[indexify(cube.R1, position, cube.B1, cube.A0)] - moment[indexify(cube.R1, position, cube.B0, cube.A1)] + moment[indexify(cube.R1, position, cube.B0, cube.A0)] - moment[indexify(cube.R0, position, cube.B1, cube.A1)] + moment[indexify(cube.R0, position, cube.B1, cube.A0)] + moment[indexify(cube.R0, position, cube.B0, cube.A1)] - moment[indexify(cube.R0, position, cube.B0, cube.A0)];
            }
            case 1: {
                return moment[indexify(cube.R1, cube.G1, position, cube.A1)] - moment[indexify(cube.R1, cube.G1, position, cube.A0)] - moment[indexify(cube.R1, cube.G0, position, cube.A1)] + moment[indexify(cube.R1, cube.G0, position, cube.A0)] - moment[indexify(cube.R0, cube.G1, position, cube.A1)] + moment[indexify(cube.R0, cube.G1, position, cube.A0)] + moment[indexify(cube.R0, cube.G0, position, cube.A1)] - moment[indexify(cube.R0, cube.G0, position, cube.A0)];
            }
            case 0: {
                return moment[indexify(cube.R1, cube.G1, cube.B1, position)] - moment[indexify(cube.R1, cube.G1, cube.B0, position)] - moment[indexify(cube.R1, cube.G0, cube.B1, position)] + moment[indexify(cube.R1, cube.G0, cube.B0, position)] - moment[indexify(cube.R0, cube.G1, cube.B1, position)] + moment[indexify(cube.R0, cube.G1, cube.B0, position)] + moment[indexify(cube.R0, cube.G0, cube.B1, position)] - moment[indexify(cube.R0, cube.G0, cube.B0, position)];
            }
            default: {
                return 0L;
            }
        }
    }
    
    private void histogram(final int[] image, final int bw, final int bh) {
        final int mm = 2;
        final int nn = 5;
        int pp = 0;
        for (int y = 0; y < bh; ++y) {
            for (int x = 0; x < bw; ++x) {
                final int val = image[pp++];
                final int a = val >> 24 & 0xFF;
                final int r = val >> 16 & 0xFF;
                final int g = val >> 8 & 0xFF;
                final int b = val & 0xFF;
                final int inr = r >> 2;
                final int ing = g >> 2;
                final int inb = b >> 2;
                final int ina = a >> 5;
                final int ind = indexify(inr + 1, ing + 1, inb + 1, ina + 1);
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
                final long[] vma = this.vma;
                final int n5 = ind;
                vma[n5] += a;
                final double[] m2 = this.m2;
                final int n6 = ind;
                m2[n6] += r * r + g * g + b * b + a * a;
            }
        }
    }
    
    private void M3d() {
        final int multiCount = 585;
        for (int r = 1; r < 65; ++r) {
            final long[] volume = new long[585];
            final long[] volR = new long[585];
            final long[] volG = new long[585];
            final long[] volB = new long[585];
            final long[] volA = new long[585];
            final double[] volTemp = new double[585];
            for (int g = 1; g < 65; ++g) {
                final long[] area = new long[9];
                final long[] areaR = new long[9];
                final long[] areaG = new long[9];
                final long[] areaB = new long[9];
                final long[] areaA = new long[9];
                final double[] areaTemp = new double[9];
                for (int b = 1; b < 65; ++b) {
                    long line = 0L;
                    long line_r = 0L;
                    long line_g = 0L;
                    long line_b = 0L;
                    long line_a = 0L;
                    double line2 = 0.0;
                    for (int a = 1; a < 9; ++a) {
                        final int ind1 = indexify(r, g, b, a);
                        line += this.vwt[ind1];
                        line_r += this.vmr[ind1];
                        line_g += this.vmg[ind1];
                        line_b += this.vmb[ind1];
                        line_a += this.vma[ind1];
                        line2 += this.m2[ind1];
                        final long[] array = area;
                        final int n = a;
                        array[n] += line;
                        final long[] array2 = areaR;
                        final int n2 = a;
                        array2[n2] += line_r;
                        final long[] array3 = areaG;
                        final int n3 = a;
                        array3[n3] += line_g;
                        final long[] array4 = areaB;
                        final int n4 = a;
                        array4[n4] += line_b;
                        final long[] array5 = areaA;
                        final int n5 = a;
                        array5[n5] += line_a;
                        final double[] array6 = areaTemp;
                        final int n6 = a;
                        array6[n6] += line2;
                        final int inv = b * 9 + a;
                        final long[] array7 = volume;
                        final int n7 = inv;
                        array7[n7] += area[a];
                        final long[] array8 = volR;
                        final int n8 = inv;
                        array8[n8] += areaR[a];
                        final long[] array9 = volG;
                        final int n9 = inv;
                        array9[n9] += areaG[a];
                        final long[] array10 = volB;
                        final int n10 = inv;
                        array10[n10] += areaB[a];
                        final long[] array11 = volA;
                        final int n11 = inv;
                        array11[n11] += areaA[a];
                        final double[] array12 = volTemp;
                        final int n12 = inv;
                        array12[n12] += areaTemp[a];
                        final int ind2 = ind1 - indexify(1, 0, 0, 0);
                        this.vwt[ind1] = this.vwt[ind2] + volume[inv];
                        this.vmr[ind1] = this.vmr[ind2] + volR[inv];
                        this.vmg[ind1] = this.vmg[ind2] + volG[inv];
                        this.vmb[ind1] = this.vmb[ind2] + volB[inv];
                        this.vma[ind1] = this.vma[ind2] + volA[inv];
                        this.m2[ind1] = this.m2[ind2] + volTemp[inv];
                    }
                }
            }
        }
    }
    
    private double variance(final Cube cube) {
        final double dr = volume(cube, this.vmr);
        final double dg = volume(cube, this.vmg);
        final double db = volume(cube, this.vmb);
        final double da = volume(cube, this.vma);
        final double cc = this.m2[indexify(cube.R1, cube.G1, cube.B1, cube.A1)] - this.m2[indexify(cube.R1, cube.G1, cube.B1, cube.A0)] - this.m2[indexify(cube.R1, cube.G1, cube.B0, cube.A1)] + this.m2[indexify(cube.R1, cube.G1, cube.B0, cube.A0)] - this.m2[indexify(cube.R1, cube.G0, cube.B1, cube.A1)] + this.m2[indexify(cube.R1, cube.G0, cube.B1, cube.A0)] + this.m2[indexify(cube.R1, cube.G0, cube.B0, cube.A1)] - this.m2[indexify(cube.R1, cube.G0, cube.B0, cube.A0)] - this.m2[indexify(cube.R0, cube.G1, cube.B1, cube.A1)] + this.m2[indexify(cube.R0, cube.G1, cube.B1, cube.A0)] + this.m2[indexify(cube.R0, cube.G1, cube.B0, cube.A1)] - this.m2[indexify(cube.R0, cube.G1, cube.B0, cube.A0)] + this.m2[indexify(cube.R0, cube.G0, cube.B1, cube.A1)] - this.m2[indexify(cube.R0, cube.G0, cube.B1, cube.A0)] - this.m2[indexify(cube.R0, cube.G0, cube.B0, cube.A1)] + this.m2[indexify(cube.R0, cube.G0, cube.B0, cube.A0)];
        return cc - (dr * dr + dg * dg + db * db + da * da) / volume(cube, this.vwt);
    }
    
    private Object[] maximize(final Cube cube, final int direction, final int first, final int last, final double whole_r, final double whole_g, final double whole_b, final double whole_a, final double whole_w) {
        final long base_r = base(cube, direction, this.vmr);
        final long base_g = base(cube, direction, this.vmg);
        final long base_b = base(cube, direction, this.vmb);
        final long base_a = base(cube, direction, this.vma);
        final long base_w = base(cube, direction, this.vwt);
        double max = 0.0;
        int cut = -1;
        for (int i = first; i < last; ++i) {
            double half_r = (double)(base_r + findTop(cube, direction, i, this.vmr));
            double half_g = (double)(base_g + findTop(cube, direction, i, this.vmg));
            double half_b = (double)(base_b + findTop(cube, direction, i, this.vmb));
            double half_a = (double)(base_a + findTop(cube, direction, i, this.vma));
            double half_w = (double)(base_w + findTop(cube, direction, i, this.vwt));
            if (half_w != 0.0) {
                double temp = (half_r * half_r + half_g * half_g + half_b * half_b + half_a * half_a) / half_w;
                half_r = whole_r - half_r;
                half_g = whole_g - half_g;
                half_b = whole_b - half_b;
                half_a = whole_a - half_a;
                half_w = whole_w - half_w;
                if (half_w != 0.0) {
                    temp += (half_r * half_r + half_g * half_g + half_b * half_b + half_a * half_a) / half_w;
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
        final double whole_a = volume(set1, this.vma);
        final double whole_w = volume(set1, this.vwt);
        Object[] temp = this.maximize(set1, 3, set1.R0 + 1, set1.R1, whole_r, whole_g, whole_b, whole_a, whole_w);
        final int cutr = (int)temp[0];
        final double maxr = (double)temp[1];
        temp = this.maximize(set1, 2, set1.G0 + 1, set1.G1, whole_r, whole_g, whole_b, whole_a, whole_w);
        final int cutg = (int)temp[0];
        final double maxg = (double)temp[1];
        temp = this.maximize(set1, 1, set1.B0 + 1, set1.B1, whole_r, whole_g, whole_b, whole_a, whole_w);
        final int cutb = (int)temp[0];
        final double maxb = (double)temp[1];
        temp = this.maximize(set1, 0, set1.A0 + 1, set1.A1, whole_r, whole_g, whole_b, whole_a, whole_w);
        final int cuta = (int)temp[0];
        final double maxa = (double)temp[1];
        int dir;
        if (maxr >= maxg && maxr >= maxb && maxr >= maxa) {
            dir = 3;
            if (cutr < 0) {
                return false;
            }
        }
        else if (maxg >= maxr && maxg >= maxb && maxg >= maxa) {
            dir = 2;
        }
        else if (maxb >= maxr && maxb >= maxg && maxb >= maxa) {
            dir = 1;
        }
        else {
            dir = 0;
        }
        set2.R1 = set1.R1;
        set2.G1 = set1.G1;
        set2.B1 = set1.B1;
        set2.A1 = set1.A1;
        switch (dir) {
            case 3: {
                final int n = cutr;
                set1.R1 = n;
                set2.R0 = n;
                set2.G0 = set1.G0;
                set2.B0 = set1.B0;
                set2.A0 = set1.A0;
                break;
            }
            case 2: {
                final int n2 = cutg;
                set1.G1 = n2;
                set2.G0 = n2;
                set2.R0 = set1.R0;
                set2.B0 = set1.B0;
                set2.A0 = set1.A0;
                break;
            }
            case 1: {
                final int n3 = cutb;
                set1.B1 = n3;
                set2.B0 = n3;
                set2.R0 = set1.R0;
                set2.G0 = set1.G0;
                set2.A0 = set1.A0;
                break;
            }
            case 0: {
                final int n4 = cuta;
                set1.A1 = n4;
                set2.A0 = n4;
                set2.R0 = set1.R0;
                set2.G0 = set1.G0;
                set2.B0 = set1.B0;
                break;
            }
        }
        set1.Volume = (set1.R1 - set1.R0) * (set1.G1 - set1.G0) * (set1.B1 - set1.B0) * (set1.A1 - set1.A0);
        set2.Volume = (set2.R1 - set2.R0) * (set2.G1 - set2.G0) * (set2.B1 - set2.B0) * (set2.A1 - set2.A0);
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
        final Cube cube5 = cube[0];
        final int n = 0;
        cube5.A0 = n;
        cube4.B0 = n;
        cube3.G0 = n;
        cube2.R0 = n;
        final Cube cube6 = cube[0];
        final Cube cube7 = cube[0];
        final Cube cube8 = cube[0];
        final int r1 = 64;
        cube8.B1 = r1;
        cube7.G1 = r1;
        cube6.R1 = r1;
        cube[0].A1 = 8;
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
    
    public Object[] getPalette(final int[] image, final int bw, final int bh) {
        final int colorCount = 256;
        this.histogram(image, bw, bh);
        this.M3d();
        final Cube[] cube = new Cube[256];
        this.buildCube(cube);
        final byte[] palette = new byte[768];
        final byte[] trns = new byte[256];
        int z = 0;
        for (int k = 0; k < 256; ++k) {
            final double weight = volume(cube[k], this.vwt);
            if (weight != 0.0) {
                trns[k] = (byte)(volume(cube[k], this.vma) / weight);
                palette[z++] = (byte)(volume(cube[k], this.vmr) / weight);
                palette[z++] = (byte)(volume(cube[k], this.vmg) / weight);
                palette[z++] = (byte)(volume(cube[k], this.vmb) / weight);
            }
            else {
                z += 4;
            }
        }
        return new Object[] { palette, trns };
    }
    
    static class Cube
    {
        int A0;
        int A1;
        int R0;
        int R1;
        int G0;
        int G1;
        int B0;
        int B1;
        int Volume;
    }
}
