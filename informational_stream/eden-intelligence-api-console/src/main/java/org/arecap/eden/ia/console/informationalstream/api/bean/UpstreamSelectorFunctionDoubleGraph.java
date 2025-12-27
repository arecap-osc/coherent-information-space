package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.RootsOfUnity;
import org.arecap.eden.ia.console.informationalstream.api.factory.UpstreamSelectorFunctionGraphPropertiesFactory;

public class UpstreamSelectorFunctionDoubleGraph extends ComplexPlane implements UpstreamSelectorFunctionGraphPropertiesFactory<Double> {

    public UpstreamSelectorFunctionDoubleGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        ComplexPlane graph = null;
        if(isUpstream(root)) {
             graph = RootsOfUnity.get10RootOf12(1D/ Math.sqrt(3) * root.getStepDistance() * root.getScale());
        } else  {
            graph = RootsOfUnity.get10RootOf12(1D / 3D * root.getStepDistance() * root.getScale());
        }
        setReal(root.getReal()+graph.getReal());
        setImaginary(root.getImaginary() + graph.getImaginary());
    }


}
