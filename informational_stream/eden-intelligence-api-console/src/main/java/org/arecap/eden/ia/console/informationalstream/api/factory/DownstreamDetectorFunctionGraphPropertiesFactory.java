package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface DownstreamDetectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.DownstreamDetectorFunction;
    }

    @Override
    default String getString() {
        return "d(D)";
    }

}
