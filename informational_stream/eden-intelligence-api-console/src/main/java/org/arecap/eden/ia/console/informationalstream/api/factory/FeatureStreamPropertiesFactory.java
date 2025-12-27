package org.arecap.eden.ia.console.informationalstream.api.factory;


import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.SignalBean;

import java.io.Serializable;

public interface FeatureStreamPropertiesFactory<ID extends Serializable, S extends SignalBean> extends HasIdentity<ID> {

    S getDownstream();

    S getUpstream();

    StreamTopology getTopology();

}
