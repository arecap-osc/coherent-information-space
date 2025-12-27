package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;
import java.util.Locale;

public interface ConfigurableInformationalStream<ID extends Serializable> extends IdentityAware<ID> {

    void getName(String name);

    void setLocale(Locale locale);

    void getFunctionalDescription(String functionalDescription);

}
