package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager;

import java.util.List;
import java.util.Optional;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.event.RefreshFeedbackApplicationComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackApplicationService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class FeedbackApplicationEditorDialogPresenter extends DefaultFlowEntityPresenter<FeedbackApplication, Long> {
    @Autowired
    private UnicursalDataMapService unicursalService;

    @Autowired
    private FeedbackDataMapService dataMapService;

    @Autowired
    private FeedbackApplicationService service;

    @Autowired
    private FeedbackSelectionIe feedbackSelection;

    @Override
    public FeedbackApplicationEditorDialogView getView() {
        return (FeedbackApplicationEditorDialogView)super.getView();
    }

    @Override
    public FeedbackApplicationService getService() {
        return service;
    }

    @Override
    public Class getEntityType() {
        return FeedbackApplication.class;
    }

    public FeedbackApplication createEmptyFeedbackApplication() {
        FeedbackApplication feedbackApplication = new FeedbackApplication();
        feedbackApplication.setLabel("");
        feedbackApplication.setBrief("");
        feedbackApplication.setDescription("");
        return feedbackApplication;
    }

    public void handleAddNewEvent(ClickEvent<Button> clickEvent) {
        setEntity(createEmptyFeedbackApplication());
        getView().getFeedbackApplicationEditorForm().setVisible(true);
    }

    public void handleFilterValueChangeEvent(AbstractField.ComponentValueChangeEvent<TextField, String> textFieldStringComponentValueChangeEvent) {
        getView().getApplicationGrid().setItems(getService().getAllApplicationsByBriefLabelStartsWithIgnoreCase(textFieldStringComponentValueChangeEvent.getValue()));
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
        getView().getApplicationGrid().setItems(getService().getAllApplications());
        getView().getFeedbackApplicationEditorForm().setVisible(false);
    }

    public void handleCancelEvent(ClickEvent<Button> clickEvent) {
        getView().getFeedbackApplicationEditorForm().setVisible(false);
    }

    public void handleApplicationSelectionEvent(SelectionEvent<Grid<FeedbackApplication>, FeedbackApplication> gridFeedbackApplicationSelectionEvent) {
        if (gridFeedbackApplicationSelectionEvent.getFirstSelectedItem().isPresent()) {
            setEntity(gridFeedbackApplicationSelectionEvent.getFirstSelectedItem().get());
            getView().getFeedbackApplicationEditorForm().setVisible(true);
            return;
        }

        getView().getFeedbackApplicationEditorForm().setVisible(false);
    }

    @Override
    public void save() {
        boolean isNew = (getEntity().getId() == null);
        getBinder().validate();
        if (!getBinder().isValid()) {
            return;
        }

        FeedbackApplication updatedFeedbackApplication = getService().save(getEntity());
        setEntity(updatedFeedbackApplication);
        if (isNew) {
            unicursalService.generateAndSave(updatedFeedbackApplication.getId());
            List<UnicursalDataMap> us = unicursalService.findAllByApplicationId(updatedFeedbackApplication.getId());
            us.stream().forEach(u -> dataMapService.generateAllCombinationsAndSave(updatedFeedbackApplication.getId(), u.getRow(), u.getColumn()));
        }

        feedbackSelection.setApplication(updatedFeedbackApplication);
        getView().getFeedbackApplicationEditorForm().setVisible(false);
        getView().getApplicationGrid().setItems(getService().getAllApplications());
        getUIEventBus().publish(this, new RefreshFeedbackApplicationComboBoxEvent(Optional.of(updatedFeedbackApplication)));
    }
}
