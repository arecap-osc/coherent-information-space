package org.arecap.eden.ia.console.informationalstream.api.factory;


import org.arecap.eden.ia.console.informationalstream.api.StreamTopology;
import org.arecap.eden.ia.console.informationalstream.api.bean.SignalBean;

import java.io.Serializable;

public interface ConfigurableFeatureStream<ID extends Serializable, S extends SignalBean> extends IdentityAware<ID> {

    void setDownstream(S downstream);

    void setUpstream(S upstream);

    void setTopology(StreamTopology topology);

}
