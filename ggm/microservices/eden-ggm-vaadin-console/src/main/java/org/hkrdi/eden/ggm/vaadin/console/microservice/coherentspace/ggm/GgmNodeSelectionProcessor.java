package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm;

import java.util.Optional;

import org.hkrdi.eden.ggm.vaadin.console.event.NodeSelectionChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class GgmNodeSelectionProcessor extends GgmStateSelectionProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void processNodeSelection(NodeBean nodeBean) {
        resetSelectionOnMediaRendererLayers(nodeBean.getNetwork());
        if (getEdge().isPresent() == false) {
            LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
            selectFistNode(nodeBean);
            publishNodeSelection(nodeBean);
            return;
        }
        if (getEdge().get().getFromNode().equals(nodeBean)) {
            LOGGER.info("User deselected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
            setEdge(Optional.empty());
            publishResetSelection();
            return;
        }
        if (getEdge().get().getToNode() == null) {
            if (getCoherentSpaceService().findNodeNeighbors(getEdge().get().getFromNode()).contains(nodeBean)) {
                LOGGER.info("User selected edge composed by FROM node #" + getEdge().get().getFromNode().getAddressIndex() +
                        " and TO node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
                selectFistNode(getEdge().get().getFromNode());
                selectSecondNode(nodeBean);
                publishNodeSelection(nodeBean);
                return;
            }

            LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
            selectFistNode(nodeBean);
            publishNodeSelection(nodeBean);
            return;
        }
        if (getEdge().get().getToNode().equals(nodeBean)) {
            LOGGER.info("User deselected TO node #" + nodeBean.getAddressIndex() + " of edge with FROM node #"
                    + getEdge().get().getFromNode().getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
            publishNodeSelection(getEdge().get().getFromNode());
            selectFistNode(getEdge().get().getFromNode());
            return;
        }

        LOGGER.info("User selected FROM node #" + nodeBean.getAddressIndex() + " from network " + nodeBean.getNetwork() + " on coherent-space-nodes route");
        selectFistNode(nodeBean);
        publishNodeSelection(nodeBean);
    }

    public void processEdgeSelection(EdgeBean edge) {
        resetSelectionOnMediaRendererLayers(edge.getFromNode().getNetwork());
//        publishEdgeSelection(edge);
        if (this.getEdge().isPresent() && this.getEdge().get().equals(edge)) {
            resetSelection();
            return;
        }
        publishNodeSelection(edge.getFromNode());
        publishNodeSelection(edge.getToNode());
        selectEdge(edge);
    }

    @Override
    protected void publishResetSelection() {
        getUiEventBus().publish(this, new NodeSelectionChangeEvent(Optional.empty()));
    }

    private void publishNodeSelection(NodeBean node) {
        getUiEventBus().publish(this, new NodeSelectionChangeEvent(Optional.of(node)));
    }

}
