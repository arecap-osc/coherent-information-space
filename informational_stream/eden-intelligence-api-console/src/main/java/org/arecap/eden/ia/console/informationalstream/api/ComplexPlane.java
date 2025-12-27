package org.arecap.eden.ia.console.informationalstream.api;


import org.arecap.eden.ia.console.informationalstream.api.bean.ComplexPlaneBean;

public class ComplexPlane implements ComplexPlaneBean<Double> {
    private Double real;
    private Double imaginary;

    public ComplexPlane() {
    }

    public ComplexPlane(Double real, Double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Double getReal() {
        return real;
    }

    public void setReal(Double real) {
        this.real = real;
    }

    public Double getImaginary() {
        return imaginary;
    }

    public void setImaginary(Double imaginary) {
        this.imaginary = imaginary;
    }

    public void add(ComplexPlane complexPlane) {
        this.real += complexPlane.getReal();
        this.imaginary += complexPlane.getImaginary();
    }

}

