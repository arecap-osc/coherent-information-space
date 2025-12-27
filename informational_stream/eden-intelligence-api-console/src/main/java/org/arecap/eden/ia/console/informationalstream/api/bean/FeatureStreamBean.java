package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableFeatureStream;
import org.arecap.eden.ia.console.informationalstream.api.factory.FeatureStreamPropertiesFactory;

import java.io.Serializable;

public interface FeatureStreamBean<ID extends Serializable, S extends SignalBean>
        extends FeatureStreamPropertiesFactory<ID, S>, ConfigurableFeatureStream<ID, S> {
}
