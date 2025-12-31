package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface DownstreamDoubleRangeIntegerIdentityGraphBean
        extends InformationalStreamDoubleRangeIntegerIdentityGraphBean {

    @Override
    default Double getStepDistance() {
        return getStep() == 0 ? getStreamDistance() : 1/ Math.sqrt(3) * getStreamDistance() / Math.pow(3, getStep());
    }

    @Override
    default Double getWidth() {
        return getHeight() * 2D / Math.sqrt(3);
    }

    @Override
    default Double getHeight() {
        return getStepDistance();
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamSelectorFunctionUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamSelectorSystemUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamDetectorFunctionUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamDetectorSystemUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamConsumerFunctionUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getUpstreamConsumerSystemUpstream() {
        return null;
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamSelectorFunctionDownstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) && getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) ?
                new ArrayList<>() : Arrays.asList(getDownstreamConsumerFunctionGraph(), getDownstreamConsumerSystemGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamSelectorSystemDownstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) && getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) ?
                Arrays.asList(getDownstreamDetectorFunctionGraph(), getDownstreamDetectorSystemGraph()) :
                Arrays.asList(getDownstreamDetectorFunctionGraph(), getDownstreamDetectorSystemGraph(), getDownstreamConsumerFunctionGraph(), getDownstreamConsumerSystemGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamDetectorFunctionDownstream() {
        return Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamConsumerFunctionGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamDetectorSystemDownstream() {
        return Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamConsumerFunctionGraph());
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamConsumerFunctionDownstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) && getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) ?
                Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamSelectorSystemGraph()) :
                new ArrayList<>();
    }

    @Override
    default List<StreamApplicationGraphPropertiesFactory<Double>> getDownstreamConsumerSystemDownstream() {
        return getOriginVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) && getVectorDirection().equals(InformationalStreamVectorDirection.CornerParity) ?
                Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamSelectorSystemGraph(), getDownstreamDetectorFunctionGraph(), getDownstreamDetectorSystemGraph()) :
                Arrays.asList(getDownstreamDetectorFunctionGraph(), getDownstreamDetectorSystemGraph());
    }

}
