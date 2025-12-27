package org.hkrdi.eden.ggm.vaadin.console.microservice.common.application;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmNetworkChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.ForceRefreshGgmMultiImageEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.ExportDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class GgmApplicationAndNetworkComboboxAndButtonsPresenter extends ApplicationPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GgmApplicationAndNetworkComboboxAndButtonsPresenter.class);

    @Autowired
    private CoherentSpaceNetworkMediaLayerManager coherentSpaceNetworkMediaLayerManager;

    @Autowired
    private ExportDataService exportDataService;

    @Override
    public GgmApplicationAndNetworkComboboxAndButtonsView getView() {
        return (GgmApplicationAndNetworkComboboxAndButtonsView) super.getView();
    }

    @Override
    protected void notifyApplicationChangeEvent(Application application) {
        if (application != null) {
            getView().getDownload()
                    .configureDownload(application.getBrief().getLabel() + "_raport" + ".docx", () -> createExportStreamResource());
        }
        getUIEventBus().publish(this,
                new GgmApplicationChangeEvent(Optional.ofNullable(application)));
    }

    public void coherentSpaceNetworkComboBoxSetItemsAndSelectValue() {

        List<String> comboValues = coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                .filter(mediaLayer -> mediaLayer.isVisible())
                .map(MediaLayer::getName)
                .collect(Collectors.toList());
        getView().getCoherentSpaceNetworkComboBox().setItems(comboValues);
        getView().getCoherentSpaceNetworkComboBox().setEnabled(false);
        if (comboValues.size() > 0) {
            Optional<String> comboValue = Optional.of(comboValues.get(0));
            if (getApplicationDataIe().getNetwork() != null) {
                comboValue = comboValues.stream()
                        .filter(network -> network.equalsIgnoreCase(getApplicationDataIe().getNetwork())).findFirst();
                if (comboValue.isPresent() == false) {
                    comboValue = Optional.of(comboValues.get(0));
                }
            }
            if (comboValue.isPresent()) {
                getView().getCoherentSpaceNetworkComboBox().setEnabled(true);
                getApplicationDataIe().setNetwork(comboValue.get());
                getView().getCoherentSpaceNetworkComboBox().setValue(comboValue.get());
            } else {
                getView().getCoherentSpaceNetworkComboBox().setEnabled(false);
                getView().getCoherentSpaceNetworkComboBox().setValue(null);
                getApplicationDataIe().setNetwork(null);
            }
        }
    }

    public void handleCoherentSpaceNetworkComboBoxValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxApplicationComponentValueChangeEvent) {
        if (comboBoxApplicationComponentValueChangeEvent.isFromClient()) {
            if (comboBoxApplicationComponentValueChangeEvent.getHasValue().isEmpty() == false) {
                if (getApplicationDataIe().getNetwork() == null || getApplicationDataIe().getNetwork().equalsIgnoreCase(getView().getCoherentSpaceNetworkComboBox().getValue()) == false) {
                    getApplicationDataIe().setNetwork(comboBoxApplicationComponentValueChangeEvent.getValue());
                }
            } else {
                getApplicationDataIe().setNetwork(null);
            }
        }
        if (comboBoxApplicationComponentValueChangeEvent.getValue() == null ||
                (comboBoxApplicationComponentValueChangeEvent.getValue() != null &&
                        !comboBoxApplicationComponentValueChangeEvent.getValue().equals(comboBoxApplicationComponentValueChangeEvent.getOldValue()))) {
            LOGGER.info("User changed current network to " + getApplicationDataIe().getNetwork());
            getUIEventBus().publish(this,
                    new GgmNetworkChangeEvent(Optional.ofNullable(getApplicationDataIe().getNetwork())));
        }
    }

    private InputStream createExportStreamResource() {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(org.apache.commons.io.IOUtils.toByteArray(getClass().getResourceAsStream("/edenggm.docx")));
            List<String> visibleNetworks = coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                    .filter(MediaLayer::isVisible)
                    .map(MediaLayer::getName)
                    .collect(Collectors.toList());
            LOGGER.info("User clicked the export button. Generating report...");
            byte[] result = exportDataService.generate(getView().getApplicationComboBox().getValue(), is, visibleNetworks);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @EventBusListenerMethod
    public void onForceRefreshGgmMultiImageEvent(ForceRefreshGgmMultiImageEvent event) {
        coherentSpaceNetworkComboBoxSetItemsAndSelectValue();
    }


}
