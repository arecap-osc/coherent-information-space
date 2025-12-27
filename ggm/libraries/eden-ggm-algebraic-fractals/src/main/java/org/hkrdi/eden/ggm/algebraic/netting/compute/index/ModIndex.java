package org.hkrdi.eden.ggm.algebraic.netting.compute.index;

public final class ModIndex  {

    public static int getMod6Index0(int index) {
        return getModNIndex(6, 6, index);
    }

    public static int getMod6Index1(int index) {
        return getModNIndex(6, 1, index);
    }

    public static int getMod6Index2(int index) {
        return getModNIndex(6, 2, index);
    }

    public static int getMod6Index3(int index) {
        return getModNIndex(6, 3, index);
    }

    public static int getMod6Index4(int index) {
        return getModNIndex(6, 4, index);
    }

    public static int getMod6Index5(int index) {
        return getModNIndex(6, 5, index);
    }

    public static int getModNIndex(int n, int modN, int index) {
        return  n * index * (index + 1) / 2 - (n - modN) * index;
    }

}
