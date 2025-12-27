package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.ForceRefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.ForceRefreshRoadMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RoadApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RoadNetworkChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadApplicationAndNetworkComboboxAndButtonsPresenter extends ApplicationPresenter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoadApplicationAndNetworkComboboxAndButtonsPresenter.class);

	@Autowired
    private CoherentSpaceNetworkMediaLayerManager coherentSpaceNetworkMediaLayerManager;

    @Override
    public RoadApplicationAndNetworkComboboxAndButtonsView getView() {
        return (RoadApplicationAndNetworkComboboxAndButtonsView) super.getView();
    }

    @Override
    protected void notifyApplicationChangeEvent(Application application) {
    	getUIEventBus().publish(this,
                new RoadApplicationChangeEvent(Optional.ofNullable(application)));
    }

    public void handleImportApplicationButtonClick(ClickEvent<Button> buttonClickEvent) {

    }

    public void coherentSpaceNetworkComboBoxSetItemsAndSelectValue() {
    	boolean mustRefreshAndSetValue = false;
        List<String> comboValues = coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                .filter(mediaLayer -> mediaLayer.isVisible())
                .map(MediaLayer::getName)
                .collect(Collectors.toList());
        String oldValue = getView().getCoherentSpaceNetworkComboBox().getValue();
        
        if (
        		(oldValue!= null && comboValues!=null && !comboValues.contains(oldValue))||
        		(oldValue == null)){
        	mustRefreshAndSetValue = true;
        }
        
        getView().getCoherentSpaceNetworkComboBox().setItems(comboValues);
        if (mustRefreshAndSetValue) {
        	if (comboValues!=null && comboValues.size() > 0) {
	            Optional<String> firstComboValue = Optional.of(comboValues.get(0));
	            if(getApplicationDataIe().getNetwork() != null) {
	            	//get first value from combo
	                firstComboValue = comboValues.stream()
	                        .filter(network -> network.equalsIgnoreCase(getApplicationDataIe().getNetwork())).findFirst();
	                if(firstComboValue.isPresent() == false) {
	                    firstComboValue = Optional.of(comboValues.get(0));
	                }
	            }
	            if (firstComboValue.isPresent()) {
	                getView().getCoherentSpaceNetworkComboBox().setValue(firstComboValue.get());
	                //set new network
	                getApplicationDataIe().setNetwork(firstComboValue.get());

	            } else {
	                getView().getCoherentSpaceNetworkComboBox().setValue(null);
	                getApplicationDataIe().setNetwork(null);
	            }
	        }else {
	        	getView().getCoherentSpaceNetworkComboBox().setValue(null);
	        	getApplicationDataIe().setNetwork(null);
	        }
        	
        	getUIEventBus().publish(this,
        			new RoadNetworkChangeEvent(getApplicationDataIe().getNetwork()==null?"":getApplicationDataIe().getNetwork()));
        }else {
            getView().getCoherentSpaceNetworkComboBox().setValue(oldValue);
        }
        
        if (comboValues==null || comboValues.size()==0) {
        	getView().getCoherentSpaceNetworkComboBox().setEnabled(false);
        }else {
        	getView().getCoherentSpaceNetworkComboBox().setEnabled(true);
        }
    }

    public void handleCoherentSpaceNetworkComboBoxValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxApplicationComponentValueChangeEvent) {
    	if (comboBoxApplicationComponentValueChangeEvent.isFromClient()) {
//	        if(comboBoxApplicationComponentValueChangeEvent.getHasValue().isEmpty() == false) {
	            if(getApplicationDataIe().getNetwork() == null ||
	                    getApplicationDataIe().getNetwork().equalsIgnoreCase(getView().getCoherentSpaceNetworkComboBox().getValue()) == false) {
	
	            	getApplicationDataIe().setNetwork(comboBoxApplicationComponentValueChangeEvent.getValue());
	
	                if (comboBoxApplicationComponentValueChangeEvent.getValue() == null || 
	                		(comboBoxApplicationComponentValueChangeEvent.getValue()!=null &&
	                		!comboBoxApplicationComponentValueChangeEvent.getValue().equals(comboBoxApplicationComponentValueChangeEvent.getOldValue()))) {
						LOGGER.info("User changed current network to " + getApplicationDataIe().getNetwork());
						getUIEventBus().publish(this,
	                			new RoadNetworkChangeEvent(getApplicationDataIe().getNetwork()==null?"":getApplicationDataIe().getNetwork()));
	                }
	            }
//	        }else {
//	            getApplicationDataIe().setNetwork(null);
//	        }
    	}
    }

    @EventBusListenerMethod
    public void onForceRefreshGgmMultiImageEvent(ForceRefreshGgmMultiImageEvent event) {
        coherentSpaceNetworkComboBoxSetItemsAndSelectValue();
    }

    @EventBusListenerMethod
    public void onForceRefreshGgmMultiImageEvent(ForceRefreshRoadMultiImageEvent event) {
        coherentSpaceNetworkComboBoxSetItemsAndSelectValue();
    }
}
