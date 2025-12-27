package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamNetting;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;

import java.util.Arrays;
import java.util.List;

public interface InformationalStreamGraphPropertiesFactory<R extends Number, ID extends Number> extends ComplexPlanePropertiesFactory<R>, HasIdentity<ID> {

    R getStreamDistance();

    Integer getStep();

    R getScale();

    R getStepDistance();

    R getWidth();

    R getHeight();

    InformationalStreamNetting getNetting();

    InformationalStreamGraphPropertiesFactory<R, ID> getRoot();

    InformationalStreamVectorDirection getVectorDirection();

    InformationalStreamVectorDirection getOriginVectorDirection();

    UpstreamSelectorFunctionGraphPropertiesFactory<R> getUpstreamSelectorFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamSelectorFunctionUpstream();

    UpstreamSelectorSystemGraphPropertiesFactory<R> getUpstreamSelectorSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamSelectorSystemUpstream();

    UpstreamDetectorFunctionGraphPropertiesFactory<R> getUpstreamDetectorFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamDetectorFunctionUpstream();

    UpstreamDetectorSystemGraphPropertiesFactory<R> getUpstreamDetectorSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamDetectorSystemUpstream();

    UpstreamConsumerFunctionGraphPropertiesFactory<R> getUpstreamConsumerFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamConsumerFunctionUpstream();

    UpstreamConsumerSystemGraphPropertiesFactory<R> getUpstreamConsumerSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamConsumerSystemUpstream();

    DownstreamSelectorFunctionGraphPropertiesFactory<R> getDownstreamSelectorFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamSelectorFunctionDownstream();

    DownstreamSelectorSystemGraphPropertiesFactory<R> getDownstreamSelectorSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamSelectorSystemDownstream();

    DownstreamDetectorFunctionGraphPropertiesFactory<R> getDownstreamDetectorFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamDetectorFunctionDownstream();

    DownstreamDetectorSystemGraphPropertiesFactory<R> getDownstreamDetectorSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamDetectorSystemDownstream();

    DownstreamConsumerFunctionGraphPropertiesFactory<R> getDownstreamConsumerFunctionGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamConsumerFunctionDownstream();

    DownstreamConsumerSystemGraphPropertiesFactory<R> getDownstreamConsumerSystemGraph();

    List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamConsumerSystemDownstream();

    default List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamGraph() {
        return Arrays.asList(getUpstreamSelectorFunctionGraph(), getUpstreamConsumerSystemGraph(), getUpstreamDetectorFunctionGraph(),
                getUpstreamSelectorSystemGraph(), getUpstreamConsumerFunctionGraph(), getUpstreamDetectorSystemGraph());
    }

    default List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamGraph() {
        return Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamConsumerSystemGraph(), getDownstreamDetectorFunctionGraph(),
                getDownstreamSelectorSystemGraph(), getDownstreamConsumerFunctionGraph(), getDownstreamDetectorSystemGraph());
    }

    default List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamFunctionGraph() {
        return Arrays.asList(getUpstreamSelectorFunctionGraph(), getUpstreamDetectorFunctionGraph(), getUpstreamConsumerFunctionGraph());
    }

    default List<StreamApplicationGraphPropertiesFactory<R>> getUpstreamSystemGraph() {
        return Arrays.asList(getUpstreamConsumerSystemGraph(), getUpstreamSelectorSystemGraph(), getUpstreamDetectorSystemGraph());
    }

    default List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamFunctionGraph() {
        return Arrays.asList(getDownstreamSelectorFunctionGraph(), getDownstreamDetectorFunctionGraph(), getDownstreamConsumerFunctionGraph());
    }

    default List<StreamApplicationGraphPropertiesFactory<R>> getDownstreamSystemGraph() {
        return Arrays.asList(getDownstreamConsumerSystemGraph(), getDownstreamSelectorSystemGraph(), getDownstreamDetectorSystemGraph());
    }

}
