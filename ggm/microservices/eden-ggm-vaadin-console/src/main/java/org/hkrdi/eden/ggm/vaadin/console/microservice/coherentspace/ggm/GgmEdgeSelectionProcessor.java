package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.event.EdgeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@SpringComponent
@UIScope
public class GgmEdgeSelectionProcessor extends GgmStateSelectionProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void processNodeSelection(NodeBean nodeBean) {
        resetSelectionOnMediaRendererLayers(nodeBean.getNetwork());
        if (getEdge().isPresent() == false) {
            LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-edges route");
            selectFistNode(nodeBean);
//            publishNodeSelection(nodeBean);
            return;
        }
        if (getEdge().get().getFromNode().equals(nodeBean)) {
            LOGGER.info("User deselected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-edges route");
            setEdge(Optional.empty());
            publishResetSelection();
            return;
        }
        if (getEdge().get().getToNode() == null) {
            if (getCoherentSpaceService().findNodeNeighbors(getEdge().get().getFromNode()).contains(nodeBean)) {
                if (getCoherentSpaceService().findEdgeDataMap(new EdgeBean(getEdge().get().getFromNode(), nodeBean)).isPresent()) {
                    processEdgeSelection(new EdgeBean(getEdge().get().getFromNode(), nodeBean));
                    return;
                } else {
                    processEdgeSelection(new EdgeBean(nodeBean, getEdge().get().getFromNode()));
                    return;
                }

//                selectFistNode(getEdge().get().getFromNode());
//                selectSecondNode(nodeBean);
//                publishNodeSelection(nodeBean);
            }
            LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-edges route");
            selectFistNode(nodeBean);
//            publishNodeSelection(nodeBean);
            return;
        }
        if (getEdge().get().getToNode().equals(nodeBean)) {
            LOGGER.info("User deselected TO node #" + nodeBean.getAddressIndex() + " of edge with FROM node #"
                    + getEdge().get().getFromNode().getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-edges route");

//            publishNodeSelection(edge.get().getFromNode());
            selectFistNode(getEdge().get().getFromNode());
            return;
        }

        LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-edges route");
        selectFistNode(nodeBean);
//        publishNodeSelection(nodeBean);
    }

    public void processEdgeSelection(EdgeBean edge) {
        LOGGER.info("User selected edge composed by FROM node #" + edge.getFromNode().getAddressIndex() +
                " and TO node #" + edge.getToNode().getAddressIndex() + " from network " + edge.getFromNode().getNetwork() + " on coherent-space-edges route");
        resetSelectionOnMediaRendererLayers(edge.getFromNode().getNetwork());
        publishEdgeSelection(edge);
        if (this.getEdge().isPresent() && this.getEdge().get().equals(edge)) {
            resetSelection();
            return;
        }
        selectEdge(edge);
    }

    @Override
    protected void publishResetSelection() {
        getUiEventBus().publish(this, new EdgeSelectionChangeEvent(Optional.empty()));
    }

    private void publishEdgeSelection(EdgeBean edge) {
        getUiEventBus().publish(this, new EdgeSelectionChangeEvent(Optional.of(edge)));
    }

}
