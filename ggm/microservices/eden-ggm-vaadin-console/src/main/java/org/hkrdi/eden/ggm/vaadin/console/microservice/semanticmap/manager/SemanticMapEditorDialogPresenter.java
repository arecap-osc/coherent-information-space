package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.manager;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.RefreshSemanticMapComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@SpringComponent
@UIScope
public class SemanticMapEditorDialogPresenter extends DefaultFlowEntityPresenter<SemanticMap, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticMapEditorDialogPresenter.class);

    @Autowired
    private SemanticMapRepositoryService service;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Override
    public SemanticMapEditorDialogView getView() {
        return (SemanticMapEditorDialogView)super.getView();
    }

    @Override
    public SemanticMapRepositoryService getService() {
        return service;
    }

    @Override
    public Class<SemanticMap> getEntityType() {
        return SemanticMap.class;
    }

    @Override
    public Long getInitialEntityId() {
        return semanticMapIe.getSemanticMapId();
    }

    @Override
    public void afterSave() {
        getView().getEditorForm().setVisible(false);
        getView().getSemanticMapGrid().setItems(getService().findAll());
        LOGGER.info("Semantic map with id " + getEntity().getId() + " was created");
        getUIEventBus().publish(this, new RefreshSemanticMapComboBoxEvent(Optional.of(getEntity())));
    }

    public void handleAddNewEvent(ClickEvent<Button> addNewButtonClickEvent) {
        getBinder().setBean(new SemanticMap());
        getView().getEditorForm().setVisible(true);
    }

    public void handleFilterValueChangeEvent(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getView().getSemanticMapGrid().setItems(getService().findAllByLabelStartsWithIgnoreCase(textFieldStringComponentValueChangeEvent.getValue()));
    }

    public void handleSemanticMapSelectionEvent(SelectionEvent<Grid<SemanticMap>, SemanticMap> gridSemanticMapSelectionEvent) {
        if(gridSemanticMapSelectionEvent.getFirstSelectedItem().isPresent()) {
            setEntity(gridSemanticMapSelectionEvent.getFirstSelectedItem().get());
            getView().getEditorForm().setVisible(true);
            return;
        }
        getView().getEditorForm().setVisible(false);
    }

    public void handleCancelEvent(ClickEvent<Button> cancelButtonClickEvent) {
        getView().getEditorForm().setVisible(false);
    }

    public void handleDeleteEvent(ClickEvent<Button> deleteButtonClickEvent) {
        try {
            delete();
            getUIEventBus().publish(this, new RefreshSemanticMapComboBoxEvent(Optional.empty()));
        } catch (DataIntegrityViolationException e) {
            Notification.show("Harta semantica are informatii atribuite si nu poate fi stearsa.");
        }
        getView().getSemanticMapGrid().setItems(getService().findAll());
        getView().getEditorForm().setVisible(false);
    }

}
