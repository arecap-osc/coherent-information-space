package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import java.util.EventObject;

import org.hkrdi.eden.ggm.repository.entity.EmbeddedBrief;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ApplicationDialogPresenter extends DefaultFlowEntityPresenter<Application, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDialogPresenter.class);

    private ApplicationPresenter applicationPresenter;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @Override
    public ApplicationDialogView getView() {
        return (ApplicationDialogView)super.getView();
    }

    @Override
    public ApplicationRepositoryService getService() {
        return applicationService;
    }

    @Override
    public Class<Application> getEntityType() {
        return Application.class;
    }

    @Override
    public Long getInitialEntityId() {
        return applicationPresenter.getApplicationDataIe().getApplicationId();
    }

    @Override
    public void setBinder(BeanValidationBinder<Application> binder) {
        super.setBinder(binder);
        getBinder().bind(getView().getBrief(), "brief.brief");
        getBinder().bind(getView().getLabel(), "brief.label");
        getBinder().bind(getView().getDescription(), "brief.description");
    }

    @Override
    public void afterPrepareModel(EventObject event) {
        getView().getApplicationGrid().setItems(getService().getAllApplications());
        getView().getSemanticMapComboBox().setItems(getService().getAllSemanticMaps());
        
        getView().getRelatedAppComboBox().setItems(getService().getAllApplicationsAndEmpty());
    }

    private Application createEmptyApplication() {
        Application application = new Application();
        application.setBrief(new EmbeddedBrief());
        application.getBrief().setLabel("");
        application.getBrief().setBrief("");
        application.getBrief().setDescription("");
        return application;
    }

    private SemanticMap createEmptySemanticMap() {
        SemanticMap result = new SemanticMap();
        result.setBrief("");
        result.setLabel("");
        result.setDescription("");
        return result;
    }

    @Override
    public void afterSave() {
        getView().getApplicationGrid().setItems(getService().getAllApplications());
        getView().getEditorForm().setVisible(false);
        applicationPresenter.applicationComboBoxSetItemsAndSelectValue();
        LOGGER.info("Application with id " + getEntity().getId() + " was created");
    }

    @Override
    public void setEntity(Application application) {
        if (application.getBrief() == null) {
            application.setBrief(new EmbeddedBrief());
        }
        getBinder().setBean(application);
    }

    public void setApplicationPresenter(ApplicationPresenter applicationPresenter) {
        this.applicationPresenter = applicationPresenter;
    }

    public void handleAddNewEvent(ClickEvent<Button> buttonClickEvent) {
        setEntity(createEmptyApplication());
        getView().getEditorForm().setVisible(true);
    }

    public void handleFilterValueChangeEvent(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getView().getApplicationGrid().setItems(getService().getAllApplicationsByBriefLabelStartsWithIgnoreCase(textFieldStringComponentValueChangeEvent.getValue()));
    }

    public void handleDeleteEvent(ClickEvent<Button> buttonClickEvent) {
        try {
            delete();
            getView().getEditorForm().setVisible(false);
            getView().getApplicationGrid().setItems(getService().getAllApplications());
            applicationPresenter.applicationComboBoxSetItemsAndSelectValue();
            LOGGER.info("Application with id " + getEntity().getId() + " was deleted");
        } catch (Exception e) {
            Notification.show("Aplicatia are informatii atribuite si nu poate fi stearsa.");
        }
    }

    public void handleCancelEvent(ClickEvent<Button> buttonClickEvent) {
        getView().getEditorForm().setVisible(false);
    }

    public void handleApplicationSelectionEvent(SelectionEvent<Grid<Application>, Application> gridApplicationSelectionEvent) {
        if (gridApplicationSelectionEvent.getFirstSelectedItem().isPresent()) {
            Application selectedApplication = gridApplicationSelectionEvent.getFirstSelectedItem().get();
            setEntity(selectedApplication);
            if (selectedApplication.getSemanticMap() == null || selectedApplication.getSemanticMap().getLabel() == null) {
                getView().getSemanticMapComboBox().setValue(createEmptySemanticMap());
            } else {
                getView().getSemanticMapComboBox().setValue(selectedApplication.getSemanticMap());
            }
            
            if (selectedApplication.getLanguage() == null) {
            	getView().getLanguageComboBox().setValue("");
            }else {
            	getView().getLanguageComboBox().setValue(selectedApplication.getLanguage().toLowerCase());
            }
            
            if (selectedApplication.getRelatedApplicationId() == null) {
            	getView().getRelatedAppComboBox().setValue(getService().getEmpty());
            }else {
            	getView().getRelatedAppComboBox().setValue(getService().getApplicationById(selectedApplication.getRelatedApplicationId()));
            }
            getView().getEditorForm().setVisible(true);
            return;
        }
        getView().getEditorForm().setVisible(false);
    }

    public void setApplicationSemanticGridId(AbstractField.ComponentValueChangeEvent<ComboBox<SemanticMap>, SemanticMap> comboBoxSemanticGridComponentValueChangeEvent) {
        if (comboBoxSemanticGridComponentValueChangeEvent.getHasValue().isEmpty()) {
            getEntity().setSemanticGridId(null);
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed semantic applicationGrid id to null");
        } else {
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed semantic applicationGrid id to " + comboBoxSemanticGridComponentValueChangeEvent.getValue().getLabel());
            getEntity().setSemanticGridId(comboBoxSemanticGridComponentValueChangeEvent.getValue().getId());
            getService().setApplicationSemanticMap(getEntity());
        }
    }
    
    public void setRelatedApplicationId(AbstractField.ComponentValueChangeEvent<ComboBox<Application>, Application> comboBoxSemanticGridComponentValueChangeEvent) {
        if (comboBoxSemanticGridComponentValueChangeEvent.getHasValue().isEmpty()) {
            getEntity().setRelatedApplicationId(null);
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed related application id to null");
        } else {
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed related applicationGrid id to " + comboBoxSemanticGridComponentValueChangeEvent.getValue().getId());
            getEntity().setRelatedApplicationId(comboBoxSemanticGridComponentValueChangeEvent.getValue().getId());
//            getService().save(getEntity());
        }
    }
    
    public void setLanguage(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxSemanticGridComponentValueChangeEvent) {
        if (comboBoxSemanticGridComponentValueChangeEvent.getHasValue().isEmpty()) {
            getEntity().setLanguage(null);
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed language to null");
        } else {
            LOGGER.info("Application " + getEntity().getBrief().getLabel() + " changed lang to " + comboBoxSemanticGridComponentValueChangeEvent.getValue());
            getEntity().setLanguage(comboBoxSemanticGridComponentValueChangeEvent.getValue());
//            getService().save(getEntity());
        }
    }

}
