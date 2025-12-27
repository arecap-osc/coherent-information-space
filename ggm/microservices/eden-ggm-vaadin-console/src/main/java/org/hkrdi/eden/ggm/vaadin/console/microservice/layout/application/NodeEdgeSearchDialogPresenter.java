package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoordinatesUtil;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmStateSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.GgmSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

import java.util.*;

@SpringComponent
@UIScope
public class NodeEdgeSearchDialogPresenter implements FlowPresenter {
    private NodeEdgeSearchDialogView view;

    private Set<NodeBean> allNodes = new HashSet<>();

    @Autowired
    private ApplicationDataRepositoryService applicationDataRepositoryService;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    private StateSelectionProcessor stateSelectionProcessor;

    private MultiImagePresenter multiImagePresenter;

    @Override
    public void setView(FlowView view) {
        this.view = (NodeEdgeSearchDialogView) view;
    }

    @Override
    public NodeEdgeSearchDialogView getView() {
        return view;
    }

    public void setupPresenter(GgmStateSelectionProcessor stateSelectionProcessor, MultiImagePresenter multiImagePresenter) {
        this.stateSelectionProcessor = stateSelectionProcessor;
        this.multiImagePresenter = multiImagePresenter;
    }

    @Override
    public void prepareModel(EventObject event) {
        this.allNodes = coherentSpaceService.getNetworkNodes(applicationDataIe.getNetwork());
    }

    public void searchNode(ClickEvent<Button> buttonClickEvent) {
        if (getView().getIndexTextField().isInvalid() == false) {
            final String index = getView().getIndexValue();
            if ("".equals(index) == false) {
                selectNodeWithNeighbours(new Long(index));
                return;
            }
        }
        String semanticSyntaxValue = getView().getSemanticSyntaxTextField().getValue().toLowerCase();
        if ("".equals(semanticSyntaxValue)) {
            Notification.show("Informatii insuficiente");
            return;
        }

        Optional<ApplicationData> semanticSyntaxApplicationDataOptional = applicationDataRepositoryService.findByContainingSemanticOrSyntaxIgnoreCase(applicationDataIe.getApplicationId(),
                Arrays.asList(applicationDataIe.getNetwork()), semanticSyntaxValue).stream().findFirst();
        if (semanticSyntaxApplicationDataOptional.isPresent()) {
            final ApplicationData semanticSyntaxApplicationData = semanticSyntaxApplicationDataOptional.get();
            if (semanticSyntaxApplicationData.getSemantic().toLowerCase().contains(semanticSyntaxValue)) {
                selectNodeWithNeighbours(semanticSyntaxApplicationData.getAddressIndex());
            }
            if (semanticSyntaxApplicationData.getSyntax().toLowerCase().contains(semanticSyntaxValue)) {
                selectEdge(semanticSyntaxApplicationData.getAddressIndex(), semanticSyntaxApplicationData.getToAddressIndex());
            }
        } else {
            Notification.show("Niciun nod nu a fost gasit");
        }
    }

    private void selectEdge(Long addressIndex, Long toAddressIndex) {
       if(GgmSelectionProcessor.class.isAssignableFrom(stateSelectionProcessor.getClass())){
           GgmSelectionProcessor ggmSelectionProcessor = (GgmSelectionProcessor) stateSelectionProcessor;
           ggmSelectionProcessor.resetSelection();
           ggmSelectionProcessor.processEdgeSelection(new EdgeBean(new NodeBean(applicationDataIe.getNetwork(), addressIndex),
                   new NodeBean(applicationDataIe.getNetwork(), toAddressIndex)));
           multiImagePresenter.refreshAll();
           moveScreenToSelection(addressIndex);
           getView().close();
       }
    }

    private void selectNodeWithNeighbours(Long index) {
        if(GgmSelectionProcessor.class.isAssignableFrom(stateSelectionProcessor.getClass())) {
            GgmSelectionProcessor ggmSelectionProcessor = (GgmSelectionProcessor) stateSelectionProcessor;
            ggmSelectionProcessor.resetSelection();
            ggmSelectionProcessor.processNodeWithNeighboursSelection(new NodeBean(applicationDataIe.getNetwork(), index));
            multiImagePresenter.refreshAll();
            moveScreenToSelection(index);
            getView().close();
        }
    }

    private void moveScreenToSelection(Long addressIndex) {
        Optional<DataMap> dataMapAddressIndexOptional = coherentSpaceService.findDataMapByNetworkAndAddressIndex(applicationDataIe.getNetwork(), addressIndex);
        if (dataMapAddressIndexOptional.isPresent()) {
            DataMap dataMap = dataMapAddressIndexOptional.get();
            Point screenPoint = CoordinatesUtil.getAlgebraicToResizedGraphics(dataMap.getAtAddressCoordinates());
            multiImagePresenter.dragScreenCenter(screenPoint.getX() * -1, screenPoint.getY() * -1);
        }
    }

    public void validateIndexInput(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getView().validateIndexInput(allNodes);
    }
}