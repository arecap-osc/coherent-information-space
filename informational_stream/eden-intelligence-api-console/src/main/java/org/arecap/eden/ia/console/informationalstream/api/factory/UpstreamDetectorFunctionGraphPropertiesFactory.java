package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface UpstreamDetectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.UpstreamDetectorFunction;
    }

    @Override
    default String getString() {
        return "u(D)";
    }

}
