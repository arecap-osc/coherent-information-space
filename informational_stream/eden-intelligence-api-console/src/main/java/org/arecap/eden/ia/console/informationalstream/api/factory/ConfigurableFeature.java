package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamBean;

import java.io.Serializable;

public interface ConfigurableFeature<ID extends Serializable, S extends InformationalStreamBean>
        extends IdentityAware<ID> {

    void setContent(String content);

    void setInformationalStream(S informationalStream);

}
