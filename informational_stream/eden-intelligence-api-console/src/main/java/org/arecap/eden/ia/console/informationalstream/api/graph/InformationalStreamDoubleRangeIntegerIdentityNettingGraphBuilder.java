package org.arecap.eden.ia.console.informationalstream.api.graph;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamNetting;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder {

    boolean accept(InformationalStreamNetting netting);

    default Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraph(Double streamDistance, Integer step, Double scale, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> displayBottomRight) {
        Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> root = getStepDimensionGraph(streamDistance, step, scale);
        if(root.isPresent()) {
            return getGraph(root.get(), origin, displayBottomRight);
        }
        return null;
    }

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getStepDimensionGraph(Double streamDistance, Integer step, Double scale);

    default Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> displayBottomRight) {
        Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> graph = new HashMap<>();
        ComplexPlanePropertiesFactory<Double> displayUpperLeft = getDisplayUpperLeft(displayBottomRight);
        ComplexPlane graphPosition = new ComplexPlane(displayUpperLeft.getReal(), displayBottomRight.getImaginary());
        ComplexPlane scroll = getScroll(root);
        while (graphPosition.getImaginary() <= displayUpperLeft.getImaginary()) {
            while (graphPosition.getReal() <= displayBottomRight.getReal()) {
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> selectorFunctionGraph = getSelectorFunctionGraph(root, origin, graphPosition);
                if(selectorFunctionGraph.isPresent()) {
                    graph.put(selectorFunctionGraph.get().getId(), selectorFunctionGraph.get());
                }
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> selectorSystemGraph = getSelectorSystemGraph(root, origin, graphPosition);
                if(selectorSystemGraph.isPresent()) {
                    graph.put(selectorSystemGraph.get().getId(), selectorSystemGraph.get());
                }
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> detectorFunctionGraph = getDetectorFunctionGraph(root, origin, graphPosition);
                if(detectorFunctionGraph.isPresent()) {
                    graph.put(detectorFunctionGraph.get().getId(), detectorFunctionGraph.get());
                }
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> detectorSystemGraph = getDetectorSystemGraph(root, origin, graphPosition);
                if(detectorSystemGraph.isPresent()) {
                    graph.put(detectorSystemGraph.get().getId(), detectorSystemGraph.get());
                }
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> consumerFunctionGraph = getConsumerFunctionGraph(root, origin, graphPosition);
                if(consumerFunctionGraph.isPresent()) {
                    graph.put(consumerFunctionGraph.get().getId(), consumerFunctionGraph.get());
                }
                Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> consumerSystemGraph = getConsumerSystemGraph(root, origin, graphPosition);
                if(consumerSystemGraph.isPresent()) {
                    graph.put(consumerSystemGraph.get().getId(), consumerSystemGraph.get());
                }
                graphPosition.setReal(graphPosition.getReal() + scroll.getReal());

            }
            graphPosition.setReal(displayUpperLeft.getReal());
            graphPosition.setImaginary(graphPosition.getImaginary() + scroll.getImaginary());
        }
        return graph;
    }

    ComplexPlane getScroll(InformationalStreamDoubleRangeIntegerIdentityGraphBean root);

    default ComplexPlanePropertiesFactory<Double> getDisplayUpperLeft(ComplexPlanePropertiesFactory<Double> displayBottomRight) {
        return new ComplexPlane(-displayBottomRight.getReal(), - displayBottomRight.getImaginary());
    }

    default Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> concatGraphs(Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> graphA, Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> graphB) {
        graphB.entrySet().stream().forEach(g -> {
            if(graphA.get(g.getKey()) == null) {
                graphA.put(g.getKey(), g.getValue());
            }
        });
        return graphA;
    }

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getSelectorFunctionGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getSelectorSystemGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getDetectorFunctionGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getDetectorSystemGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getConsumerFunctionGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

    Optional<InformationalStreamDoubleRangeIntegerIdentityGraphBean> getConsumerSystemGraph(InformationalStreamDoubleRangeIntegerIdentityGraphBean root, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> graphPosition);

}
