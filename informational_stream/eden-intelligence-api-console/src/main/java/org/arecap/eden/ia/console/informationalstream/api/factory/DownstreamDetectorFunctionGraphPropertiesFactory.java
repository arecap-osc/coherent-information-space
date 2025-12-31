package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

public interface DownstreamDetectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamProcessType getType() {
        return StreamProcessType.DownstreamDetectorFunction;
    }

    @Override
    default String getString() {
        return "d(D)";
    }

}
