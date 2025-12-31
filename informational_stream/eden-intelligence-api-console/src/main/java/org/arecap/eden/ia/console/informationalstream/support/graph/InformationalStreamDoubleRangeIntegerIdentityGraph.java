package org.arecap.eden.ia.console.informationalstream.support.graph;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.InformationalStreamGraphPropertiesFactory;

public abstract class InformationalStreamDoubleRangeIntegerIdentityGraph implements InformationalStreamDoubleRangeIntegerIdentityGraphBean {

    private Integer id;

    private Double streamDistance;

    private InformationalStreamVectorDirection vectorDirection;

    private InformationalStreamVectorDirection originVectorDirection = InformationalStreamVectorDirection.CornerParity;

    private Integer step;

    private Double scale;

    private Double real;

    private Double imaginary;

    private InformationalStreamDoubleRangeIntegerIdentityGraph root;

    public InformationalStreamDoubleRangeIntegerIdentityGraph() {

    }

    public InformationalStreamDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale, ComplexPlane complexPlane) {
        this(streamDistance, step, scale);
        this.real = real;
        this.imaginary = imaginary;
    }

    public InformationalStreamDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale) {
        this.streamDistance = streamDistance;
        this.step = step;
        this.scale = scale;
    }

    public InformationalStreamDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale, InformationalStreamDoubleRangeIntegerIdentityGraph root) {
        this(streamDistance, step, scale);
        this.root = root;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Double getStreamDistance() {
        return streamDistance;
    }

    @Override
    public void setStreamDistance(Double streamDistance) {
        this.streamDistance = streamDistance;
    }

    @Override
    public InformationalStreamVectorDirection getVectorDirection() {
        return vectorDirection;
    }

    @Override
    public void setVectorDirection(InformationalStreamVectorDirection vectorDirection) {
        this.vectorDirection = vectorDirection;
    }

    @Override
    public InformationalStreamVectorDirection getOriginVectorDirection() {
        return originVectorDirection;
    }

    @Override
    public void setOriginVectorDirection(InformationalStreamVectorDirection originVectorDirection) {
        this.originVectorDirection = originVectorDirection;
    }

    @Override
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public Double getScale() {
        return scale;
    }

    @Override
    public void setScale(Double scale) {
        this.scale = scale;
    }

    @Override
    public Double getReal() {
        return real;
    }

    @Override
    public void setReal(Double real) {
        this.real = real;
    }

    @Override
    public Double getImaginary() {
        return imaginary;
    }

    @Override
    public void setImaginary(Double imaginary) {
        this.imaginary = imaginary;
    }

    @Override
    public InformationalStreamDoubleRangeIntegerIdentityGraph getRoot() {
        return root;
    }

    @Override
    public void setRoot(InformationalStreamGraphPropertiesFactory<Double, Integer> root) {
        this.root = (InformationalStreamDoubleRangeIntegerIdentityGraph) root;
    }
}
