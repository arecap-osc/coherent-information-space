package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;

public interface ConfigurableI18n<ID extends Serializable> extends IdentityAware<ID> {

    void setI18nId(ID i18nId);

}
