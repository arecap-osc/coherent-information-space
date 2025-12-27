package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface DownstreamSelectorSystemGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.DownstreamSelectorSystem;
    }

    @Override
    default String getString() {
        return "S(d)";
    }

}
