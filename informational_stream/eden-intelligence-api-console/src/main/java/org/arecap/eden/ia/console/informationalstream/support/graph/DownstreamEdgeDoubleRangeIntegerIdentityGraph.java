package org.arecap.eden.ia.console.informationalstream.support.graph;

import org.arecap.eden.ia.console.informationalstream.api.bean.DownstreamEdgeDoubleRangeIntegerIdentityGraphBean;

public class DownstreamEdgeDoubleRangeIntegerIdentityGraph extends InformationalStreamDoubleRangeIntegerIdentityGraph implements DownstreamEdgeDoubleRangeIntegerIdentityGraphBean {

    public DownstreamEdgeDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale) {
        super(streamDistance, step, scale);
    }

}
