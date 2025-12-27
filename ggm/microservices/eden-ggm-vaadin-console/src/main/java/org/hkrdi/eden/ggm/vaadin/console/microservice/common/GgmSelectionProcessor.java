package org.hkrdi.eden.ggm.vaadin.console.microservice.common;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

public interface GgmSelectionProcessor extends MarkedSelectionProcessor {
    void processNodeWithNeighboursSelection(NodeBean nodeBean);

    void processNodeSelection(NodeBean nodeBean);

    void processEdgeSelection(EdgeBean edge);

    void resetSelection();
}