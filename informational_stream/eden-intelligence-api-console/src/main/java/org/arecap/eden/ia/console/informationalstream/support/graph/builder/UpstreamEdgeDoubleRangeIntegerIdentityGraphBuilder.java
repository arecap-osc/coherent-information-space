package org.arecap.eden.ia.console.informationalstream.support.graph.builder;

import org.arecap.eden.ia.console.informationalstream.api.*;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.graph.InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder;
import org.arecap.eden.ia.console.informationalstream.support.graph.UpstreamEdgeDoubleRangeIntegerIdentityGraph;

import java.util.Optional;

public class UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilder implements InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder {

    @Override
    public boolean accept(InformationalStreamNetting netting) {
        return netting.equals(InformationalStreamNetting.UpstreamEdge);
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getStepDimensionGraph(Double streamDistance, Integer step, Double scale) {
        return Optional.of(new UpstreamEdgeDoubleRangeIntegerIdentityGraph(streamDistance, step, scale));
    }

    @Override
    public ComplexPlane getScroll(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        return new ComplexPlane(1D/2D * root.getWidth() * root.getScale() , 1D/2D * root.getHeight() * root.getScale());
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView1(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps - 1)) + 1;
        ComplexPlane root11Imaginary = RootsOfUnity.get11RootOf12(result.getWidth() * result.getScale() * imaginarySteps);
        ComplexPlane root1Real = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * realSteps);
        if(realSteps < 0 || imaginarySteps < 0) {
            return Optional.empty();
        }
        if(realSteps == 0 && realSteps == imaginarySteps) {
            result.setId(0);
            result.setReal(origin.getReal());
            result.setImaginary(origin.getImaginary());
            result.setVectorDirection(InformationalStreamVectorDirection.values()[0]);
            return Optional.of(result);
        }
        if(imaginarySteps == 0) {
            result.setId(realGaussSum + realSteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root1Real.getReal());
            result.setImaginary(root1Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root1Imaginary = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            result.setId(InformationalStreamUtils.getNGaussSum(6, realSteps) + realSteps - imaginarySteps + 2);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[imaginarySteps % 2 == 0 ? (realSteps + imaginarySteps + 1) % 2 : (realSteps + imaginarySteps) % 2]);
            result.setReal(root11Imaginary.getReal() + root1Imaginary.getReal());
            result.setImaginary(root11Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView6(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps *-1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps - 1)) + 1;
        ComplexPlane root3Imaginary = RootsOfUnity.get3RootOf12(result.getWidth() * result.getScale() * imaginarySteps);
        ComplexPlane root1Real = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * realSteps);
        if(realSteps < 0 || imaginarySteps < 0) {
            return Optional.empty();
        }
        if(realSteps == 0 && realSteps == imaginarySteps) {
            result.setId(0);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[0]);
            result.setReal(origin.getReal());
            result.setImaginary(origin.getImaginary());
            return Optional.of(result);
        }
        if(imaginarySteps == 0) {
            result.setId(realGaussSum + realSteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root1Real.getReal());
            result.setImaginary(root1Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root1Imaginary = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            result.setId(InformationalStreamUtils.getNGaussSum(6, realSteps) + realSteps + imaginarySteps + 2 );
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(realSteps + imaginarySteps + 1) % 2]);
            result.setReal(root3Imaginary.getReal() + root1Imaginary.getReal());
            result.setImaginary(root3Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView3(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        ComplexPlane root5Imaginary = RootsOfUnity.get5RootOf12(result.getWidth() * result.getScale() * Math.abs(imaginarySteps));
        if(imaginarySteps < 0 && realSteps >= imaginarySteps && realSteps < imaginarySteps + Math.abs(imaginarySteps)) {
            ComplexPlane root1Imaginary = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            result.setId(InformationalStreamUtils.getNGaussSum(6,  Math.abs(imaginarySteps)) + 2 * imaginarySteps - (Math.abs(imaginarySteps) - Math.abs(realSteps)) - (Math.abs(imaginarySteps) - 1));
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(Math.abs(realSteps) ) % 2]);
            result.setReal(root5Imaginary.getReal() +root1Imaginary.getReal());
            result.setImaginary(root5Imaginary.getImaginary());
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView2(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        realSteps = realSteps * -1;
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps * -1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps)) - 1 ;
        ComplexPlane root5Imaginary = RootsOfUnity.get5RootOf12(result.getWidth() * result.getScale() * imaginarySteps);
        ComplexPlane root7Real = RootsOfUnity.get7RootOf12(result.getWidth() * result.getScale() * realSteps);
        if(realSteps < 0 || imaginarySteps < 0) {
            return Optional.empty();
        }
        if(realSteps == 0 && realSteps == imaginarySteps) {
            result.setId(0);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[0]);
            result.setReal(origin.getReal());
            result.setImaginary(origin.getImaginary());
            return Optional.of(result);
        }
        if(imaginarySteps == 0) {
            result.setId(realGaussSum - 2*(realSteps - 1));
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root7Real.getReal());
            result.setImaginary(root7Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps >= imaginarySteps) {
            ComplexPlane root7Imaginary = RootsOfUnity.get7RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            result.setId(realGaussSum - 2*(realSteps - 1) - imaginarySteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root5Imaginary.getReal() + root7Imaginary.getReal());
            result.setImaginary(root5Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView5(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps *-1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps)) - 1 ;
        ComplexPlane root9Imaginary = RootsOfUnity.get9RootOf12(result.getWidth() * result.getScale() * imaginarySteps);
        ComplexPlane root7Real = RootsOfUnity.get7RootOf12(result.getWidth() * result.getScale() * realSteps);
        if(realSteps < 0 || imaginarySteps < 0) {
            return Optional.empty();
        }
        if(realSteps == 0 && realSteps == imaginarySteps) {
            result.setId(0);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[0]);
            result.setReal(origin.getReal());
            result.setImaginary(origin.getImaginary());
            return Optional.of(result);
        }
        if(imaginarySteps == 0) {
            result.setId(realGaussSum - 2*(realSteps - 1));
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root7Real.getReal());
            result.setImaginary(root7Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root7Imaginary = RootsOfUnity.get7RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            Integer imaginaryGaussSum = InformationalStreamUtils.getNGaussSum(6, realSteps + 1) - 1;
            result.setId(imaginaryGaussSum - 2*realSteps + imaginarySteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(realSteps + imaginarySteps + 1) % 2]);
            result.setReal(root9Imaginary.getReal() + root7Imaginary.getReal());
            result.setImaginary(root9Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
  }


    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView4(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamEdgeDoubleRangeIntegerIdentityGraph result = new UpstreamEdgeDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        Double realQuota = result.getWidth() * result.getScale();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota = 3D / 4D * result.getHeight() * result.getScale();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps  = imaginarySteps *-1;
        ComplexPlane root9Imaginary = RootsOfUnity.get9RootOf12(result.getWidth() * result.getScale() * Math.abs(imaginarySteps));
        if(imaginarySteps < 0 && realSteps >= imaginarySteps && realSteps < imaginarySteps + Math.abs(imaginarySteps)) {
            ComplexPlane root1Imaginary = RootsOfUnity.get1RootOf12(result.getWidth() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            Integer imaginaryGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.abs(imaginarySteps));

            result.setId(imaginaryGaussSum + realSteps + 1);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(Math.abs(realSteps) + Math.abs(imaginarySteps)) % 2]);
            result.setReal(root9Imaginary.getReal() +root1Imaginary.getReal());
            result.setImaginary(root9Imaginary.getImaginary());
            return Optional.of(result);
        }
        return Optional.empty();
    }


}
