package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import java.util.List;
import java.util.Optional;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;

public abstract class ApplicationPresenter extends DefaultFlowPresenter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private ApplicationRepositoryService applicationRepositoryService;

    @Override
    public ApplicationView getView() {
        return (ApplicationView)super.getView();
    }

    @Override
    public ApplicationRepositoryService getService() {
        return applicationRepositoryService;
    }

    public void applicationComboBoxSetItemsAndSelectValue() {
        List<Application> comboValues = getService().getAllApplications();
        getView().getApplicationComboBox().setItems(comboValues);
        if (comboValues.size() > 0) {
            Optional<Application> comboValue = Optional.of(comboValues.get(0));
            if(applicationDataIe.getApplicationId() != null) {
                comboValue = comboValues.stream()
                        .filter(application -> application.getId().compareTo(applicationDataIe.getApplicationId()) == 0).findFirst();
            }
            if (comboValue.isPresent()) {
                getView().getApplicationComboBox().setValue(comboValue.get());
            }
        }
    }

    public void applicationComboBoxSetItemsAndSelectValue(String language) {
    	if (applicationDataIe.getApplicationId() != null) {
    		Application selectedApplication = getView().getApplicationComboBox().getValue();
            if (selectedApplication != null && !selectedApplication.getLanguage().toLowerCase().equals(language)) {
            	Application appForLang = getService().getRelatedApplications(selectedApplication).stream().filter(app->app.getLanguage().toLowerCase().equals(language)).findFirst().orElse(null);
            	if (appForLang != null) {
            		applicationDataIe.setApplicationId(appForLang.getId());
            		applicationComboBoxSetItemsAndSelectValue();
            	}
            }
    	}
    	
    }

    public void handleApplicationComboBoxValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<Application>, Application> comboBoxApplicationComponentValueChangeEvent) {
        if(comboBoxApplicationComponentValueChangeEvent.getHasValue().isEmpty() == false) {
            if(applicationDataIe.getApplicationId() == null ||
                    applicationDataIe.getApplicationId().compareTo(comboBoxApplicationComponentValueChangeEvent.getValue().getId()) != 0) {
                applicationDataIe.setApplicationId(comboBoxApplicationComponentValueChangeEvent.getValue().getId());
            }
        }
        Application selectedApplication = getView().getApplicationComboBox().getValue();
        if (selectedApplication != null) {
	        LOGGER.info("User selected application " + selectedApplication.getBrief().getLabel());
	        notifyApplicationChangeEvent(selectedApplication);
        }else {
        	LOGGER.info("User selected application as empty. Check code.");
        }
    }

    protected GgmRouteApplicationDataIe getApplicationDataIe() {
        return applicationDataIe;
    }

    protected abstract void notifyApplicationChangeEvent(Application application);

}
