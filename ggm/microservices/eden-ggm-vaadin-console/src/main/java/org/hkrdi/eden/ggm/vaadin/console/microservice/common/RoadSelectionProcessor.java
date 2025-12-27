package org.hkrdi.eden.ggm.vaadin.console.microservice.common;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

public interface RoadSelectionProcessor extends MarkedSelectionProcessor {
     void processNodeOrEdgeSelection(NodeBean clickedNode, EdgeBean clickedEdge) ;
}