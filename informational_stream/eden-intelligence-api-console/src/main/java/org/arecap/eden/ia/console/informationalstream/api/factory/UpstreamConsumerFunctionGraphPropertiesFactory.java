package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface UpstreamConsumerFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.UpstreamConsumerFunction;
    }

    @Override
    default String getString() {
        return "u(C)";
    }

}
