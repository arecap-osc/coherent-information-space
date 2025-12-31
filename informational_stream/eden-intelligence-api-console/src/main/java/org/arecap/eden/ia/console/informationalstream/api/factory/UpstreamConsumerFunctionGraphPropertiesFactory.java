package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

public interface UpstreamConsumerFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamProcessType getType() {
        return StreamProcessType.UpstreamConsumerFunction;
    }

    @Override
    default String getString() {
        return "u(C)";
    }

}
