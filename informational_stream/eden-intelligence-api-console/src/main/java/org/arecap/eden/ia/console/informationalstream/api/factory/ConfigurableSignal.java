package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamApplicationType;

import java.io.Serializable;
import java.util.Locale;

public interface ConfigurableSignal<ID extends Serializable> extends IdentityAware<ID> {

    void setStreamApplicationType(StreamApplicationType streamApplicationType);

    void setLocale(Locale locale);

    void setFeaturePart(String featurePart);

    void setDetails(String details);
}
