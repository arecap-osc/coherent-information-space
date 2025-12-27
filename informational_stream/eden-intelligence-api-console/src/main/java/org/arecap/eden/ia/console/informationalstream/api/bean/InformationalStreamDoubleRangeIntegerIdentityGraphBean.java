package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.*;

public interface InformationalStreamDoubleRangeIntegerIdentityGraphBean
        extends InformationalStreamGraphBean<Double, Integer> {

    @Override
    default UpstreamSelectorFunctionGraphPropertiesFactory<Double> getUpstreamSelectorFunctionGraph() {
        return new UpstreamSelectorFunctionDoubleGraph(this);
    }

    @Override
    default UpstreamSelectorSystemGraphPropertiesFactory<Double> getUpstreamSelectorSystemGraph() {
        return new UpstreamSelectorSystemDoubleGraph(this);
    }

    @Override
    default UpstreamDetectorFunctionGraphPropertiesFactory<Double> getUpstreamDetectorFunctionGraph() {
        return new UpstreamDetectorFunctionDoubleGraph(this);
    }

    @Override
    default UpstreamDetectorSystemGraphPropertiesFactory<Double> getUpstreamDetectorSystemGraph() {
        return new UpstreamDetectorSystemDoubleGraph(this);
    }

    @Override
    default UpstreamConsumerFunctionGraphPropertiesFactory<Double> getUpstreamConsumerFunctionGraph() {
        return new UpstreamConsumerFunctionDoubleGraph(this);
    }

    @Override
    default UpstreamConsumerSystemGraphPropertiesFactory<Double> getUpstreamConsumerSystemGraph() {
        return new UpstreamConsumerSystemDoubleGraph(this);
    }

    @Override
    default DownstreamSelectorFunctionGraphPropertiesFactory<Double> getDownstreamSelectorFunctionGraph() {
        return new DownstreamSelectorFunctionDoubleGraph(this);
    }

    @Override
    default DownstreamSelectorSystemGraphPropertiesFactory<Double> getDownstreamSelectorSystemGraph() {
        return new DownstreamSelectorSystemDoubleGraph(this);
    }

    @Override
    default DownstreamDetectorFunctionGraphPropertiesFactory<Double> getDownstreamDetectorFunctionGraph() {
        return new DownstreamDetectorFunctionDoubleGraph(this);
    }

    @Override
    default DownstreamDetectorSystemGraphPropertiesFactory<Double> getDownstreamDetectorSystemGraph() {
        return new DownstreamDetectorSystemDoubleGraph(this);
    }

    @Override
    default DownstreamConsumerFunctionGraphPropertiesFactory<Double> getDownstreamConsumerFunctionGraph() {
        return new DownstreamConsumerFunctionDoubleGraph(this);
    }

    @Override
    default DownstreamConsumerSystemGraphPropertiesFactory<Double> getDownstreamConsumerSystemGraph() {
        return new DownstreamConsumerSystemDoubleGraph(this);
    }

}
