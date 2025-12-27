package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface DownstreamConsumerFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.DownstreamConsumerFunction;
    }

    @Override
    default String getString() {
        return "d(C)";
    }

}
