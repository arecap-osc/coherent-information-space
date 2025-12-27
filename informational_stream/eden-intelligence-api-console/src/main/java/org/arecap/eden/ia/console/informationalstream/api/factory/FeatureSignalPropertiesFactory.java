package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureBean;

import java.io.Serializable;

public interface FeatureSignalPropertiesFactory<ID extends Serializable, S extends FeatureBean>
        extends SignalPropertiesFactory<ID> {

    S getFeature();

}
