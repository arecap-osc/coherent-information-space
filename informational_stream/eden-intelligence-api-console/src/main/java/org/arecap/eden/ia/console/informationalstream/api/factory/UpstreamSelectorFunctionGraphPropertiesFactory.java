package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

public interface UpstreamSelectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamProcessType getType() {
        return StreamProcessType.UpstreamSelectorFunction;
    }

    @Override
    default String getString() {
        return "u(S)";
    }

}
