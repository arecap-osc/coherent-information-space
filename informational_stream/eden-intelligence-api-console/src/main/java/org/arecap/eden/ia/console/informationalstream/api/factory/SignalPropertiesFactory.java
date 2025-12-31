package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.StreamProcessType;

import java.io.Serializable;
import java.util.Locale;

public interface SignalPropertiesFactory<ID extends Serializable> extends HasIdentity<ID> {

    StreamProcessType getStreamApplicationType();

    Locale getLocale();

    String getFeaturePart();

    String getDetails();
}
