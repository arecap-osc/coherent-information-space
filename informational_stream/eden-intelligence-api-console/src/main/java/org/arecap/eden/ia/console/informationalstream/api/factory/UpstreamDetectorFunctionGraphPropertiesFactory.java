package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

public interface UpstreamDetectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamProcessType getType() {
        return StreamProcessType.UpstreamDetectorFunction;
    }

    @Override
    default String getString() {
        return "u(D)";
    }

}
