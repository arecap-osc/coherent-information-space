package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamNetting;
import org.arecap.eden.ia.console.informationalstream.api.factory.InformationalStreamGraphPropertiesFactory;

public interface DownstreamEdgeGraphBean<R extends Number, ID extends Number> extends InformationalStreamGraphPropertiesFactory<R, ID> {

    @Override
    default InformationalStreamNetting getNetting() {
        return InformationalStreamNetting.DownstreamEdge;
    }

}
