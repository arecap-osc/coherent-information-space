package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.factory.ConfigurableComplexPlane;

public interface ComplexPlaneBean<R extends Number> extends ComplexPlanePropertiesFactory<R>, ConfigurableComplexPlane<R> {
}
