package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.roadmanager;

import java.util.EventObject;
import java.util.Optional;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.service.RoadService;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.event.RefreshFeedbackApplicationComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadEditorDialogPresenter extends DefaultFlowEntityPresenter<Road, Long> {
    @Autowired
    private RoadService service;

//    @Autowired
//    private FeedbackSelectionIe feedbackSelection;

    @Override
    public RoadEditorDialogView getView() {
        return (RoadEditorDialogView)super.getView();
    }

    @Override
    public RoadService getService() {
        return service;
    }

    @Override
    public Class getEntityType() {
        return Road.class;
    }

    @Override
	public void prepareModel(EventObject event) {
		super.prepareModel(event);
		
		getBinder().forField(getView().getNetworkTextField())
		.bind(Road::getNetwork, Road::setNetwork);
		
		getBinder().forField(getView().getGroupNameTextField())
		.withNullRepresentation("")
		.bind(Road::getGroupName, Road::setGroupName);
	
		getBinder().forField(getView().getNameTextField())
		.withNullRepresentation("")
		.bind(Road::getName, Road::setName);
		
		getBinder().forField(getView().getRoadTextField())
		.withNullRepresentation("")
		.bind(Road::getRoad, Road::setRoad);
		
		getBinder().forField(getView().getExcludeClusterTextField())
		.withNullRepresentation("")
		.bind(Road::getExcludeClusters, Road::setExcludeClusters);
		
		getBinder().forField(getView().getFractolonTextField())
		.withNullRepresentation("")
		.bind(Road::getFractolon, Road::setFractolon);
		
		getBinder().forField(getView().getOrderTextField())
		.withNullRepresentation("")
		.withConverter(new StringToIntegerConverter("Se asteapta un numar!"))
		.bind(Road::getOrderPosition, Road::setOrderPosition);
    }
		
	public Road createEmptyRoad() {
        Road road = new Road();
        road.setRoad("");
        road.setName("");
        road.setGroupName("");
        return road;
    }

    public void handleAddNewEvent(ClickEvent<Button> clickEvent) {
        setEntity(createEmptyRoad());
        getView().getRoadEditorForm().setVisible(true);
    }
    
    public void handleClickSaveNewEntityFromParent() {
    	getView().getRoadEditorForm().setVisible(true);
		setEntity(getView().getRoadFromParent());
		updateFieldsFromEntity(getView().getRoadFromParent());
    }

    private void updateFieldsFromEntity(Road road) {
    	if (road.getNetwork()!=null) {
    		getView().getNetworkTextField().setValue(road.getNetwork());
    	}
    	if (road.getGroupName()!=null) {getView().getGroupNameTextField().setValue(road.getGroupName());}
    	if (road.getName()!=null) {getView().getNameTextField().setValue(road.getName());}
    	if (road.getOrderPosition()!=null) {getView().getOrderTextField().setValue(road.getOrderPosition()+"");}
    	if (road.getRoad()!=null) {getView().getRoadTextField().setValue(road.getRoad());}
    	if (road.getExcludeClusters()!=null) {
    		getView().getExcludeClusterTextField().setValue(road.getExcludeClusters());
    	}
    	if (road.getFractolon()!=null) {getView().getFractolonTextField().setValue(road.getFractolon());}
    }
    
    public void handleFilterValueChangeEvent(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getView().getRoadGrid().setItems(
        		getService().findAllByGroupNameStartsWithIgnoreCaseOrNameStartsWithIgnoreCaseOrderByNetworkAscAndGroupNameAscAndOrderPositionAsc(textFieldStringComponentValueChangeEvent.getValue()));
    }

    public void handleDeleteEvent(ClickEvent<Button> clickEvent) {
        try {
            delete();
            getUIEventBus().publish(this, new RefreshFeedbackApplicationComboBoxEvent(Optional.empty()));
        } catch (DataIntegrityViolationException e) {
            Notification.show("Aplicatia are informatii atribuite si nu poate fi stearsa.");
        } catch (AccessDeniedException e) {
            Notification.show("Drepturi insuficiente pentru stergerea aplicatiei");
        }
        getView().getRoadGrid().setItems(getService().getAllRoads());
        getView().getRoadEditorForm().setVisible(false);
    }

    public void handleCancelEvent(ClickEvent<Button> clickEvent) {
        getView().getRoadEditorForm().setVisible(false);
    }

    public void handleRoadSelectionEvent(SelectionEvent<Grid<Road>, Road> gridRoadSelectionEvent) {
        if (gridRoadSelectionEvent.getFirstSelectedItem().isPresent()) {
            setEntity(gridRoadSelectionEvent.getFirstSelectedItem().get());
            getView().getRoadEditorForm().setVisible(true);
            return;
        }

        getView().getRoadEditorForm().setVisible(false);
    }

    @Override
    public void afterSave() {
//        boolean isNew = (getEntity().getId() == null);
//        getBinder().validate();
//        if (!getBinder().isValid()) {
//            return;
//        }
//
//        Road updatedFeedbackApplication = getService().save(getEntity());
//        setEntity(updatedFeedbackApplication);
//        if (isNew) {
//            unicursalService.generateAndSave(updatedFeedbackApplication.getId());
//            List<UnicursalDataMap> us = unicursalService.findAllByApplicationId(updatedFeedbackApplication.getId());
//            us.stream().forEach(u -> dataMapService.generateAllCombinationsAndSave(updatedFeedbackApplication.getId(), u.getRow(), u.getColumn()));
//        }
//
//        feedbackSelection.setApplication(updatedFeedbackApplication);
        getView().getRoadEditorForm().setVisible(false);
        getView().getRoadGrid().setItems(getService().getAllRoads());
//        getUIEventBus().publish(this, new RefreshFeedbackApplicationComboBoxEvent(Optional.of(updatedFeedbackApplication)));
    }
}
