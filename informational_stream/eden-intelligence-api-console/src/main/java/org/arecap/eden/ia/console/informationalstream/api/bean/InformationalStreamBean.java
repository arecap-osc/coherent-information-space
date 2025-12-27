package org.arecap.eden.ia.console.informationalstream.api.bean;


import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableInformationalStream;
import org.arecap.eden.ia.console.informationalstream.api.factory.InformationalStreamPropertiesFactory;

import java.io.Serializable;

public interface InformationalStreamBean<ID extends Serializable>
        extends InformationalStreamPropertiesFactory<ID>, ConfigurableInformationalStream<ID> {
}
