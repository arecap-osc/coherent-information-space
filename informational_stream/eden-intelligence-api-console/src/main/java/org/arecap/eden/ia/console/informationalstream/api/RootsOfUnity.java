package org.arecap.eden.ia.console.informationalstream.api;

public final class RootsOfUnity {


    public static ComplexPlane get1RootOf12(Double quota) {
        return new ComplexPlane(quota, 0D);
    }

    public static ComplexPlane get2RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(Math.PI / 6), quota * Math.sin(Math.PI / 6));
    }

    public static ComplexPlane get3RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(Math.PI / 3), quota * Math.sin(Math.PI / 3));
    }

    public static ComplexPlane get4RootOf12(Double quota) {
        return new ComplexPlane(0D, quota);
    }

    public static ComplexPlane get5RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(2 * Math.PI / 3), quota * Math.sin(2 * Math.PI / 3));
    }

    public static ComplexPlane get6RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(5 * Math.PI / 6), quota * Math.sin(5 * Math.PI / 6));
    }

    public static ComplexPlane get7RootOf12(Double quota) {
        return new ComplexPlane(- quota, 0D);
    }

    public static ComplexPlane get8RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(- 5 * Math.PI / 6), quota * Math.sin(- 5 * Math.PI / 6));
    }

    public static ComplexPlane get9RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(- 2 * Math.PI / 3), quota * Math.sin(- 2 * Math.PI / 3));
    }

    public static ComplexPlane get10RootOf12(Double quota) {
        return new ComplexPlane(0D, -quota);
    }

    public static ComplexPlane get11RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(- Math.PI / 3), quota * Math.sin(- Math.PI / 3));
    }

    public static ComplexPlane get12RootOf12(Double quota) {
        return new ComplexPlane(quota * Math.cos(- Math.PI / 6), quota * Math.sin(- Math.PI / 6));
    }

}
