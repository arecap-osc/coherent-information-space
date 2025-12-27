package org.arecap.eden.ia.console.informationalstream.support.graph;

import org.arecap.eden.ia.console.informationalstream.api.bean.DownstreamVertexDoubleRangeIntegerIdentityGraphBean;

public class DownstreamVertexDoubleRangeIntegerIdentityGraph  extends InformationalStreamDoubleRangeIntegerIdentityGraph implements DownstreamVertexDoubleRangeIntegerIdentityGraphBean {

    public DownstreamVertexDoubleRangeIntegerIdentityGraph(Double streamDistance, Integer step, Double scale) {
        super(streamDistance, step, scale);
    }

}
