package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;

public interface I18nPropertyFactory<ID extends Serializable> extends HasIdentity<ID> {

    ID getI18nId();

}
