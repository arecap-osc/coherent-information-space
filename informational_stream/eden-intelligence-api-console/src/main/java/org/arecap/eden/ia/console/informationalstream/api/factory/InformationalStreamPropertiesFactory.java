package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;
import java.util.Locale;

public interface InformationalStreamPropertiesFactory<ID extends Serializable> extends HasIdentity<ID> {

    String getName();

    Locale getLocale();

    String getFunctionalDescription();

}
