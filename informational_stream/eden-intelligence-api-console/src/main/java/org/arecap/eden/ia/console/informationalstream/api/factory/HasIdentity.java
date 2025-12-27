package org.arecap.eden.ia.console.informationalstream.api.factory;

import java.io.Serializable;

public interface HasIdentity<ID extends Serializable> extends Serializable {

    ID getId();

}
