package org.arecap.eden.ia.console.informationalstream.support.graph;

import org.arecap.eden.ia.console.informationalstream.api.bean.UpstreamEdgeDoubleRangeIntegerIdentityGraphBean;

public class UpstreamEdgeDoubleRangeIntegerIdentityGraph extends InformationalStreamDoubleRangeIntegerIdentityGraph implements UpstreamEdgeDoubleRangeIntegerIdentityGraphBean {

    public UpstreamEdgeDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale) {
        super(streamDistance, step, scale);
    }
}
