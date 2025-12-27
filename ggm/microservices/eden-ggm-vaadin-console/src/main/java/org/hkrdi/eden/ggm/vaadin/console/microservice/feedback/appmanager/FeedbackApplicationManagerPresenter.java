package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.appmanager;

import java.util.List;
import java.util.Optional;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.event.RefreshFeedbackApplicationComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class FeedbackApplicationManagerPresenter extends DefaultFlowPresenter {
    @Autowired
    private FeedbackApplicationService service;

    @Autowired
    private FeedbackSelectionIe feedbackSelection;

    @Override
    public FeedbackApplicationManagerView getView() {
        return (FeedbackApplicationManagerView)super.getView();
    }

    @Override
    public FeedbackApplicationService getService() {
        return service;
    }
    
    public void onSemanticGridComboBoxChangeEvent(AbstractField.ComponentValueChangeEvent<ComboBox<FeedbackApplication>, FeedbackApplication> comboBoxFeedbackApplicationComponentValueChangeEvent) {
        if(!comboBoxFeedbackApplicationComponentValueChangeEvent.getHasValue().isEmpty()) {
            feedbackSelection.setApplication(comboBoxFeedbackApplicationComponentValueChangeEvent.getValue());
        }
    }

    public void handleDialogButtonClick(ClickEvent<Button> clickEvent) {
        getView().getFeedbackApplicationEditorDialogView().open();
    }

    public void feedbackApplicationsGridComboBoxSetItemsSelectFirst() {
    	List<FeedbackApplication> allFeedBackApplications = getService().getAllApplications();
        getView().getFeedbackApplicationsComboBox().setItems(allFeedBackApplications);
        if (feedbackSelection.getApplication() == null) {
	        Optional<FeedbackApplication> comboValues = getView().getFeedbackApplicationsComboBox().getDataProvider().fetch(new Query<>()).findFirst();
	        if (comboValues.isPresent()) {
	            getView().getFeedbackApplicationsComboBox().setValue(comboValues.get());
	        }
        }else {
        	Optional<FeedbackApplication> current = allFeedBackApplications.stream().filter(a -> a.getId().equals(feedbackSelection.getApplication().getId())).findFirst();
        	if (current.isPresent()) {
        		getView().getFeedbackApplicationsComboBox().setValue(current.get());
        	}
        }
    }

    private void refreshFeedbackApplicationComboBox() {
        getView().getFeedbackApplicationsComboBox().setItems(getService().getAllApplications());
    }
    
    @EventBusListenerMethod
    public void onRefreshFeedbackApplicationEvent(RefreshFeedbackApplicationComboBoxEvent refreshFeedbackApplicationComboBoxEvent) {
        refreshFeedbackApplicationComboBox();
        if(((Optional<SemanticMap>)refreshFeedbackApplicationComboBoxEvent.getSource()).isPresent() ) {
            getView().getFeedbackApplicationsComboBox().setValue(((Optional<FeedbackApplication>)refreshFeedbackApplicationComboBoxEvent.getSource()).get());
        }
    }
}
