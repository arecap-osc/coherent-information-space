package org.arecap.eden.ia.console.informationalstream.support.graph.builder;

import org.arecap.eden.ia.console.informationalstream.api.*;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.graph.InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder;
import org.arecap.eden.ia.console.informationalstream.support.graph.UpstreamVertexDoubleRangeIntegerIdentityGraph;

import java.util.Optional;

public class UpstreamVertexDoubleRangeIntegerIdentityGraphBuilder implements InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder {

    @Override
    public boolean accept(InformationalStreamNetting netting) {
        return netting.equals(InformationalStreamNetting.UpstreamVertex);
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getStepDimensionGraph(Double streamDistance, Integer step, Double scale) {
        return Optional.of(new UpstreamVertexDoubleRangeIntegerIdentityGraph(streamDistance, step, scale));
    }

    @Override
    public ComplexPlane getScroll(InformationalStreamDoubleRangeIntegerIdentityGraphBean root) {
        return new ComplexPlane(root.getWidth() * root.getScale(),  1D/2D * root.getHeight() * root.getScale());
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView1(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps - 1)) + 1;
        ComplexPlane root10Imaginary = RootsOfUnity.get10RootOf12(result.getHeight() * result.getScale() * imaginarySteps);
        ComplexPlane root12Real = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * realSteps);
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
            result.setReal(root12Real.getReal());
            result.setImaginary(root12Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root12Imaginary = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            result.setId(InformationalStreamUtils.getNGaussSum(6, realSteps) + realSteps - imaginarySteps + 2);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[imaginarySteps % 2 == 0 ? (realSteps + imaginarySteps + 1) % 2 : (realSteps + imaginarySteps) % 2]);
            result.setReal(root10Imaginary.getReal()+ root12Imaginary.getReal() );
            result.setImaginary(root10Imaginary.getImaginary() + root12Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView6(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps *-1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps - 1)) + 1;
        ComplexPlane root2Imaginary = RootsOfUnity.get2RootOf12(result.getHeight() * result.getScale() * imaginarySteps);
        ComplexPlane root12Real = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * realSteps);
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
            result.setReal(root12Real.getReal());
            result.setImaginary(root12Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root12Imaginary = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            result.setId(InformationalStreamUtils.getNGaussSum(6, realSteps) + realSteps + imaginarySteps + 2 );
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(realSteps + imaginarySteps + 1) % 2]);
            result.setReal(root2Imaginary.getReal() + root12Imaginary.getReal());
            result.setImaginary(root2Imaginary.getImaginary() + root12Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView3(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        ComplexPlane root4Imaginary = RootsOfUnity.get4RootOf12(result.getHeight() * result.getScale() * Math.abs(imaginarySteps));
        if(imaginarySteps < 0 && realSteps >= imaginarySteps && realSteps < imaginarySteps + Math.abs(imaginarySteps)) {
            ComplexPlane root12Imaginary = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            result.setId(InformationalStreamUtils.getNGaussSum(6,  Math.abs(imaginarySteps)) + 2 * imaginarySteps - (Math.abs(imaginarySteps) - Math.abs(realSteps)) - (Math.abs(imaginarySteps) - 1));
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(Math.abs(realSteps) ) % 2]);
            result.setReal(root4Imaginary.getReal() + root12Imaginary.getReal());
            result.setImaginary(root4Imaginary.getImaginary() + root12Imaginary.getImaginary());
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView2(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        realSteps = realSteps * -1;
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps * -1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps)) - 1 ;
        ComplexPlane root4Imaginary = RootsOfUnity.get4RootOf12(result.getHeight() * result.getScale() * imaginarySteps);
        ComplexPlane root6Real = RootsOfUnity.get6RootOf12(result.getHeight() * result.getScale() * realSteps);
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
            result.setReal(root6Real.getReal());
            result.setImaginary(root6Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps >= imaginarySteps) {
            ComplexPlane root6Imaginary = RootsOfUnity.get6RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            result.setId(realGaussSum - 2*(realSteps - 1) - imaginarySteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[realSteps % 2]);
            result.setReal(root4Imaginary.getReal() + root6Imaginary.getReal());
            result.setImaginary(root4Imaginary.getImaginary() + root6Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }

    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView5(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps = imaginarySteps *-1;
        Integer realGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.max(0, realSteps)) - 1 ;
        ComplexPlane root8Imaginary = RootsOfUnity.get8RootOf12(result.getHeight() * result.getScale() * imaginarySteps);
        ComplexPlane root6Real = RootsOfUnity.get6RootOf12(result.getHeight() * result.getScale() * realSteps);
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
            result.setReal(root6Real.getReal());
            result.setImaginary(root6Real.getImaginary());
            return Optional.of(result);

        }
        if(realSteps + 1 >= imaginarySteps) {
            ComplexPlane root6Imaginary = RootsOfUnity.get6RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps + 1));
            Integer imaginaryGaussSum = InformationalStreamUtils.getNGaussSum(6, realSteps + 1) - 1;
            result.setId(imaginaryGaussSum - 2*realSteps + imaginarySteps);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(realSteps + imaginarySteps + 1) % 2]);
            result.setReal(root8Imaginary.getReal() + root6Imaginary.getReal());
            result.setImaginary(root8Imaginary.getImaginary() + root6Imaginary.getImaginary());
            return Optional.of(result);

        }
        return Optional.empty();
    }


    @Override
    public Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraphPointOfView4(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition) {
        UpstreamVertexDoubleRangeIntegerIdentityGraph result = new UpstreamVertexDoubleRangeIntegerIdentityGraph(root.getStreamDistance(), root.getStep(), root.getScale());
        result.setRoot(root);
        ComplexPlane scroll = getScroll(root);
        Double realQuota = scroll.getReal();
        Integer realSteps = new Double((graphPosition.getReal() - origin.getReal()) / realQuota).intValue();
        if(graphPosition.getReal().compareTo(origin.getReal()) < 0) {
            realSteps--;
        }
        Double imaginaryQuota =  scroll.getImaginary();
        Integer imaginarySteps = new Double((graphPosition.getImaginary() - origin.getImaginary()) / imaginaryQuota).intValue();
        if(graphPosition.getImaginary().compareTo(origin.getImaginary()) < 0) {
            imaginarySteps--;
        }
        imaginarySteps  = imaginarySteps *-1;
        ComplexPlane root8Imaginary = RootsOfUnity.get8RootOf12(result.getHeight() * result.getScale() * Math.abs(imaginarySteps));
        if(imaginarySteps < 0 && realSteps >= imaginarySteps && realSteps < imaginarySteps + Math.abs(imaginarySteps)) {
            ComplexPlane root12Imaginary = RootsOfUnity.get12RootOf12(result.getHeight() * result.getScale() * Math.max(0, realSteps - imaginarySteps));
            Integer imaginaryGaussSum = InformationalStreamUtils.getNGaussSum(6, Math.abs(imaginarySteps));

            result.setId(imaginaryGaussSum + realSteps + 1);
            result.setVectorDirection(InformationalStreamVectorDirection.values()[(Math.abs(realSteps) + Math.abs(imaginarySteps)) % 2]);
            result.setReal(root8Imaginary.getReal() +root12Imaginary.getReal());
            result.setImaginary(root8Imaginary.getImaginary() + root12Imaginary.getImaginary());
            return Optional.of(result);
        }
        return Optional.empty();
    }



}
