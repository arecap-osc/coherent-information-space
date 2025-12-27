package org.arecap.eden.ia.console.informationalstream.api.bean;


import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableFeature;
import org.arecap.eden.ia.console.informationalstream.api.factory.FeaturePropertiesFactory;

import java.io.Serializable;

public interface FeatureBean<ID extends Serializable, S extends InformationalStreamBean>
        extends FeaturePropertiesFactory<ID, S>, ConfigurableFeature<ID, S> {
}
