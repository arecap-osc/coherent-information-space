package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.RootsOfUnity;
import org.arecap.eden.ia.console.informationalstream.api.factory.DownstreamSelectorFunctionGraphPropertiesFactory;

public class DownstreamSelectorFunctionDoubleGraph extends ComplexPlane implements DownstreamSelectorFunctionGraphPropertiesFactory<Double> {

    public DownstreamSelectorFunctionDoubleGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        ComplexPlane graph = null;
        if(isUpstream(root)) {
             graph = RootsOfUnity.get7RootOf12(1D/ 3D * root.getStepDistance() * root.getScale());
        } else  {//TODO
            graph = RootsOfUnity.get7RootOf12(1 / Math.sqrt(3)  * root.getStepDistance() * root.getScale());
        }
        setReal(root.getReal()+graph.getReal());
        setImaginary(root.getImaginary() + graph.getImaginary());
    }


}
