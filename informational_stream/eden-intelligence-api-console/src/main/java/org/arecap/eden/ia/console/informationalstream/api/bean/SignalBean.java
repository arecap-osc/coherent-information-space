package org.arecap.eden.ia.console.informationalstream.api.bean;


import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableSignal;
import org.arecap.eden.ia.console.informationalstream.api.factory.SignalPropertiesFactory;

import java.io.Serializable;
import java.util.Set;

public interface SignalBean<ID extends Serializable, S extends SignalBean>
        extends SignalPropertiesFactory<ID>, ConfigurableSignal<ID> {

    Set<S> getCongruence();

    Set<S> getSimilarity();

}
