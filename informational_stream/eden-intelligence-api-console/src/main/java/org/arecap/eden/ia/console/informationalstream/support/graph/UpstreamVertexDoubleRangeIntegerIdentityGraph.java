package org.arecap.eden.ia.console.informationalstream.support.graph;

import org.arecap.eden.ia.console.informationalstream.api.bean.UpstreamVertexDoubleRangeIntegerIdentityGraphBean;

public class UpstreamVertexDoubleRangeIntegerIdentityGraph extends InformationalStreamDoubleRangeIntegerIdentityGraph implements UpstreamVertexDoubleRangeIntegerIdentityGraphBean {

    public UpstreamVertexDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale) {
        super(streamDistance, step, scale);
    }
}
