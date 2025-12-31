package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

public interface DownstreamConsumerSystemGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamProcessType getType() {
        return StreamProcessType.DownstreamConsumerSystem;
    }

    @Override
    default String getString() {
        return "C(d)";
    }

}
