package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableFeatureSignal;
import org.arecap.eden.ia.console.informationalstream.api.factory.FeatureSignalPropertiesFactory;

import java.io.Serializable;

public interface FeatureSignalBean <ID extends Serializable, S extends FeatureBean>
        extends FeatureSignalPropertiesFactory<ID, S>, ConfigurableFeatureSignal<ID, S> {
}
