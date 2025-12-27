package org.arecap.eden.ia.console.informationalstream.api.factory;

public interface ConfigurableComplexPlane<R extends Number> extends ComplexPlanePropertiesFactory<R> {

    void setReal(R real);

    void setImaginary(R imaginary);

    default void config(R real, R imaginary) {
        if(ConfigurableComplexPlane.class.isAssignableFrom(this.getClass())) {
            setReal(real);
            setImaginary(imaginary);
        }
    }
}
