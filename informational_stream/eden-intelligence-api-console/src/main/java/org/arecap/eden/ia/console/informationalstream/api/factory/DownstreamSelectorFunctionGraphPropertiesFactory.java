package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface DownstreamSelectorFunctionGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.DownstreamSelectorFunction;
    }

    @Override
    default String getString() {
        return "d(S)";
    }

}
