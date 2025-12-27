package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

public interface UpstreamSelectorSystemGraphPropertiesFactory<R extends Number> extends StreamApplicationGraphPropertiesFactory<R> {

    default StreamApplicationType getType() {
        return StreamApplicationType.UpstreamSelectorSystem;
    }

    @Override
    default String getString() {
        return "S(u)";
    }

}
