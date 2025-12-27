package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.RootsOfUnity;
import org.arecap.eden.ia.console.informationalstream.api.factory.UpstreamConsumerSystemGraphPropertiesFactory;

public class UpstreamConsumerSystemDoubleGraph extends ComplexPlane implements UpstreamConsumerSystemGraphPropertiesFactory<Double> {

    public UpstreamConsumerSystemDoubleGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        ComplexPlane graph = null;
        if(isUpstream(root)) {
            graph = RootsOfUnity.get12RootOf12(1D/ Math.sqrt(3) * root.getStepDistance() * root.getScale());
        } else  {
            graph = RootsOfUnity.get12RootOf12(1D / 3D * root.getStepDistance() * root.getScale());
        }
        setReal(root.getReal()+graph.getReal());
        setImaginary(root.getImaginary() + graph.getImaginary());
    }

}
