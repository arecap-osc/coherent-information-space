package org.arecap.eden.ia.console.informationalstream.support.graph.builder;

import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamNetting;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.ComplexPlanePropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.graph.InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class InformationalStreamDoubleRangeIntegerIdentityGraphBuilder {

    private List<InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder> nettingGraphBuilders =
            Arrays.asList(new UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilder(), new UpstreamVertexDoubleRangeIntegerIdentityGraphBuilder(),
                    new DownstreamVertexDoubleRangeIntegerIdentityGraphBuilder(), new DownstreamEdgeDoubleRangeIntegerIdentityGraphBuilder());

    public Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> getGraph(InformationalStreamNetting netting, Double streamDistance, Integer step, Double scale, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> displayBottomRight) {
        for(InformationalStreamDoubleRangeIntegerIdentityNettingGraphBuilder nettingGraphBuilder: nettingGraphBuilders) {
            if(nettingGraphBuilder.accept(netting)) {
                return nettingGraphBuilder.getGraph(streamDistance, step, scale, origin, displayBottomRight);
            }
        }
        return null;
    }

    public List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> getNettingGraphs(Double streamDistance, Integer step, Double scale, ComplexPlanePropertiesFactory<Double> origin, ComplexPlanePropertiesFactory<Double> displayBottomRight) {
        List<Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean>> graphs = new ArrayList<>();
        graphs.add(getGraph(InformationalStreamNetting.UpstreamEdge, streamDistance, step, scale, origin, displayBottomRight));
        graphs.add(getGraph(InformationalStreamNetting.DownstreamEdge, streamDistance, step, scale, origin, displayBottomRight));
        graphs.add(getGraph(InformationalStreamNetting.UpstreamVertex, streamDistance, step, scale, origin, displayBottomRight));
        graphs.add(getGraph(InformationalStreamNetting.DownstreamVertex, streamDistance, step, scale, origin, displayBottomRight));
        return graphs;
    }

}
