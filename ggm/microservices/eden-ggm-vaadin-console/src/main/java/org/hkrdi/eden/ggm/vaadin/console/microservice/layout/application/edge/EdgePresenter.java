package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.edge;

import java.util.EventObject;
import java.util.List;
import java.util.Optional;

import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapLink;
import org.hkrdi.eden.ggm.vaadin.console.event.PhraseChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.ApplicationDataPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.MapLinkRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

@SpringComponent
@UIScope
public class EdgePresenter extends ApplicationDataPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EdgePresenter.class);

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Autowired
    private SemanticMapService semanticMapService;

    @Override
    public EdgeView getView() {
        return (EdgeView) super.getView();
    }

    @Override
    public Long getInitialEntityId() {
        return getApplicationDataIe().getSyntaxId();
    }

    @Override
    public void prepareModel(EventObject event) {
        super.prepareModel(event);
        if (getInitialEntityId() != null) {
            getView().getMapLinkFormLayout().setVisible(semanticMapIe.getSemanticMapId() != null);
            if (semanticMapIe.getSemanticMapId() != null) {
                getView().getMapLinkComboBox()
                        .setItems(semanticMapService.getMapLinksForDataMap(semanticMapIe.getSemanticMapId(), getDataMap()));
            }
        }
        getView().getMapLinkView().setVisible(false);
        getView().getMapLinkView().getPresenter().prepareModelAndView(event);
    }

    private MapLinkRepositoryService getMapLinkService() {
        return getView().getMapLinkView().getPresenter().getService();
    }

    @Override
    public void afterSave() {
        super.afterSave();
        LOGGER.info("Edge with id " + getApplicationDataIe().getSyntaxId() + " was modified");
    }

    public void handleMapLinkComboBoxValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<MapLink>, MapLink> comboBoxMapLinkComponentValueChangeEvent) {
        semanticMapIe.setFirstMapLinkId(null);
        if (comboBoxMapLinkComponentValueChangeEvent.getHasValue().isEmpty() == false) {
            semanticMapIe.setFirstMapLinkId(comboBoxMapLinkComponentValueChangeEvent.getValue().getId());
        }
        getView().getMapLinkView().getPresenter().prepareModelAndView(null);
    }

    public void handleRouteTextValueChange() {
        getUIEventBus().publish(this, new PhraseChangeEvent.SyntaxChange(getView().getSyntax().getValue()));
    }

    public void showMapLinkFormLayoutIfMapLinkPresent() {
        List<MapLink> mapLinksForDataMap = semanticMapService.getMapLinksForDataMap(semanticMapIe.getSemanticMapId(), getDataMap());
        if (mapLinksForDataMap.size() > 0) {
            getView().getMapLinkComboBox().setValue(mapLinksForDataMap.get(0));
            return;
        }

        getView().getMapLinkFormLayout().setVisible(false);
    }

    @EventBusListenerMethod
    public void onInformationSavedEvent(InformationSavedEvent event) {
        if (getEntity() != null && getEntity().getId() != null) {
            Optional<ApplicationData> entity = getService().findById(getEntity().getId());
            if (entity.isPresent()) {
                setEntity(entity.get());
            }
        }
    }
}