package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamBean;

import java.io.Serializable;

public interface FeaturePropertiesFactory<ID extends Serializable, S extends InformationalStreamBean> extends HasIdentity<ID> {

    String getContent();

    S getInformationalStream();

}
