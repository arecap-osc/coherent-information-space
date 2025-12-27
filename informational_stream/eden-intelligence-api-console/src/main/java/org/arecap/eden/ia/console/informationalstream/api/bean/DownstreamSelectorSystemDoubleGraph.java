package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.RootsOfUnity;
import org.arecap.eden.ia.console.informationalstream.api.factory.DownstreamSelectorSystemGraphPropertiesFactory;

public class DownstreamSelectorSystemDoubleGraph extends ComplexPlane implements DownstreamSelectorSystemGraphPropertiesFactory<Double> {

    public DownstreamSelectorSystemDoubleGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        ComplexPlane graph = null;
        if(isUpstream(root)) {
             graph = RootsOfUnity.get1RootOf12(1D/ 3D * root.getStepDistance() * root.getScale());
        } else  {//TODO
            graph = RootsOfUnity.get1RootOf12(1D/ Math.sqrt(3) * root.getStepDistance() * root.getScale());
        }
        setReal(root.getReal()+graph.getReal());
        setImaginary(root.getImaginary() + graph.getImaginary());
    }


}
