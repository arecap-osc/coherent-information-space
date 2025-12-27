package org.arecap.eden.ia.console.informationalstream.api.factory;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection;

public interface ConfigurableInformationalStreamGraph<R extends Number, ID extends Number> extends ConfigurableComplexPlane<R>, IdentityAware<ID> {

    void setStreamDistance(R streamDistance);

    void setScale(R scale);

    void setRoot(InformationalStreamGraphPropertiesFactory<R, ID> root);

    void setVectorDirection(InformationalStreamVectorDirection vectorDirection);

    void setOriginVectorDirection(InformationalStreamVectorDirection vectorDirection);

}
