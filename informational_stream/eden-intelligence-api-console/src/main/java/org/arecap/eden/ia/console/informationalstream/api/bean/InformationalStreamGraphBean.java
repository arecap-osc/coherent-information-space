package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableInformationalStreamGraph;
import org.arecap.eden.ia.console.informationalstream.api.factory.InformationalStreamGraphPropertiesFactory;

public interface InformationalStreamGraphBean<R extends Number, ID extends Number>
        extends InformationalStreamGraphPropertiesFactory<R, ID>, ConfigurableInformationalStreamGraph<R, ID> {
}
