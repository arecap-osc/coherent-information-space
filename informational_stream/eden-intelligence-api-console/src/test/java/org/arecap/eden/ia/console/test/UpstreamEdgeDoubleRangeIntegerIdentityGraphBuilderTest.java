package org.arecap.eden.ia.console.test;


import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.informationalstream.api.bean.InformationalStreamDoubleRangeIntegerIdentityGraphBean;
import org.arecap.eden.ia.console.informationalstream.support.graph.builder.UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilder;
import org.junit.Test;

import java.util.Map;

public class UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilderTest {



    @Test
    public void test() {
        UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilder graphBuilder = new UpstreamEdgeDoubleRangeIntegerIdentityGraphBuilder();
        Map<Integer, InformationalStreamDoubleRangeIntegerIdentityGraphBean> graph = graphBuilder.getGraph(300D, 1, 1D, new ComplexPlane(0D,0D), new ComplexPlane(500D,-450D));
        graph.keySet().stream().forEach(step -> {
            InformationalStreamDoubleRangeIntegerIdentityGraphBean sgraph = graph.get(step);
            System.out.println(sgraph.getId());
            System.out.println(sgraph.getReal());
            System.out.println(sgraph.getImaginary());
        });
    }

}
