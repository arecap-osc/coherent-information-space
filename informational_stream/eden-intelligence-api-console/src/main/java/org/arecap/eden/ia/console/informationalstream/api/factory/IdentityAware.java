package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;

public interface IdentityAware<ID extends Serializable> {

    void setId(ID id);

}
