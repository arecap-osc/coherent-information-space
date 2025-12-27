package org.arecap.eden.ia.console.informationalstream.api.bean;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.RootsOfUnity;
import org.arecap.eden.ia.console.informationalstream.api.factory.UpstreamDetectorSystemGraphPropertiesFactory;

public class UpstreamDetectorSystemDoubleGraph extends ComplexPlane implements UpstreamDetectorSystemGraphPropertiesFactory<Double> {

    public UpstreamDetectorSystemDoubleGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        ComplexPlane graph = null;
        if(isUpstream(root)) {
            graph = RootsOfUnity.get8RootOf12(1D/ Math.sqrt(3) * root.getStepDistance() * root.getScale());
        } else  {
            graph = RootsOfUnity.get8RootOf12(1D / 3D * root.getStepDistance() * root.getScale());
        }
        setReal(root.getReal()+graph.getReal());
        setImaginary(root.getImaginary() + graph.getImaginary());
    }

}
