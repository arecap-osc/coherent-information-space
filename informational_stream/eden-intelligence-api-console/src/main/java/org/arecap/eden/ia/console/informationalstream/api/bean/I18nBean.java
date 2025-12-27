package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableI18n;
import org.arecap.eden.ia.console.informationalstream.api.factory.I18nPropertyFactory;

import java.io.Serializable;

public interface I18nBean<ID extends Serializable>
        extends I18nPropertyFactory<ID>, ConfigurableI18n<ID> {
}
