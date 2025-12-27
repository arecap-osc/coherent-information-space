package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.manager;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.SemanticMapIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.RefreshSemanticMapComboBoxEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.event.SemanticMapChangedEvent;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Optional;

@SpringComponent
@UIScope
public class SemanticMapManagerPresenter extends DefaultFlowPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SemanticMapManagerPresenter.class);

    @Autowired
    private SemanticMapRepositoryService semanticMapRepositoryService;

    @Autowired
    private SemanticMapIe semanticMapIe;

    @Autowired
    private SemanticMapEditorDialogView semanticMapEditorDialogView;

    @Override
    public SemanticMapManagerView getView() {
        return (SemanticMapManagerView)super.getView();
    }

    @Override
    public SemanticMapRepositoryService getService() {
        return semanticMapRepositoryService;
    }

    public void handleEditEvent(ClickEvent<Button> editButtonClickEvent) {
        semanticMapEditorDialogView.open();
    }

    public void semanticMapComboBoxSetItemsSelectFirst() {
        getView().getSemanticMapComboBox().setItems(getService().findAll());
        Optional<SemanticMap> comboValue = getView().getSemanticMapComboBox().getDataProvider().fetch(new Query<>()).findFirst();
        if (comboValue.isPresent()) {
            getView().getSemanticMapComboBox().setValue(comboValue.get());
        }
    }

    public void onSemanticMapComboBoxChangeEvent(AbstractField.ComponentValueChangeEvent<ComboBox<SemanticMap>, SemanticMap> semanticMapValueChangeEvent) {
        semanticMapIe.setSemanticMapId(semanticMapValueChangeEvent.getHasValue().isEmpty() ? null : semanticMapValueChangeEvent.getValue().getId());
        semanticMapIe.setFirstMapWordId(null);
        semanticMapIe.setSecondMapWordId(null);
        semanticMapIe.setFirstMapLinkId(null);
        semanticMapIe.setSecondMapLinkId(null);
        if (semanticMapValueChangeEvent.getHasValue().isEmpty() == false) {
            LOGGER.info("User selected new semantic map: " + semanticMapValueChangeEvent.getValue().getLabel());
            getUIEventBus().publish(this, new SemanticMapChangedEvent(semanticMapValueChangeEvent.getValue()));
        }

    }

    public void refreshSemanticMapComboBox() {
        getView().getSemanticMapComboBox().setItems(getService().findAll());
    }

    @EventBusListenerMethod
    public void onRefreshSemanticMapViewsEvent(RefreshSemanticMapComboBoxEvent refreshSemanticMapComboBoxEvent) {
        refreshSemanticMapComboBox();
        if(((Optional<SemanticMap>)refreshSemanticMapComboBoxEvent.getSource()).isPresent() ) {
           getView().getSemanticMapComboBox().setValue(((Optional<SemanticMap>)refreshSemanticMapComboBoxEvent.getSource()).get());
        }
    }

    public void handleImportEvent(ClickEvent<Button> buttonClickEvent) {
        getView().getSemanticMapImportView().open();
    }
}
