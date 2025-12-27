package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureBean;

import java.io.Serializable;

public interface ConfigurableFeatureSignal<ID extends Serializable, S extends FeatureBean> extends ConfigurableSignal<ID> {

    void setFeature(S feature);

}
