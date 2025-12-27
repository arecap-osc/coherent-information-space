package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;
import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamGraphBean;

public interface StreamApplicationGraphPropertiesFactory<R extends Number> extends ComplexPlanePropertiesFactory<R> {

    StreamApplicationType getType();

    String getString();

    default boolean isUpstream(InformationalStreamGraphBean root) {
        return root.getNetting().name().contains(StreamTopology.Upstream.name());
    }

}
