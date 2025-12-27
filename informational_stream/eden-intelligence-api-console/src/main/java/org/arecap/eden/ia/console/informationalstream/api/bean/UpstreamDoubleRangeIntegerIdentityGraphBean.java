package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;

import java.util.Arrays;
import java.util.List;

public interface UpstreamDoubleRangeIntegerIdentityGraphBean
        extends InformationalStreamDoubleRangeIntegerIdentityGraphBean {

    @Override
    default Double getStepDistance() {
        return getStep() == 0 ? getStreamDistance() : getStreamDistance() / Math.pow(3, getStep());
    }

    @Override
    default Double getWidth() {
        return getStepDistance();
    }

    @Override
    default Double getHeight() {
        return getWidth() * 2 / Math.sqrt(3);
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamSelectorFunctionUpstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) && getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ?
                Arrays.asList(getUpstreamDetectorSystemGraph(), getUpstreamConsumerFunctionGraph()) : Arrays.asList(getUpstreamDetectorSystemGraph(), getUpstreamConsumerSystemGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamSelectorSystemUpstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) && getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ?
                Arrays.asList(getUpstreamDetectorSystemGraph(), getUpstreamConsumerSystemGraph()) : Arrays.asList(getUpstreamDetectorSystemGraph(), getUpstreamConsumerFunctionGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamDetectorFunctionUpstream() {
        return Arrays.asList(getUpstreamSelectorFunctionGraph(), getUpstreamSelectorSystemGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamDetectorSystemUpstream() {
        return Arrays.asList(getUpstreamConsumerFunctionGraph(), getUpstreamConsumerSystemGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamConsumerFunctionUpstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) && getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ?
                Arrays.asList(getUpstreamSelectorSystemGraph(), getUpstreamDetectorFunctionGraph()) : Arrays.asList(getUpstreamSelectorFunctionGraph(), getUpstreamDetectorFunctionGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamConsumerSystemUpstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) && getVectorDirection().equals(InformationalStreamVectorDirection.SelectorDetectorConsumer) ?
                Arrays.asList(getUpstreamSelectorFunctionGraph(), getUpstreamDetectorFunctionGraph()) : Arrays.asList(getUpstreamSelectorSystemGraph(), getUpstreamDetectorFunctionGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamSelectorFunctionDownstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamSelectorSystemDownstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamDetectorFunctionDownstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamDetectorSystemDownstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamConsumerFunctionDownstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamConsumerSystemDownstream() {
        return null;
    }

}
