package org.arecap.eden.ia.console.microservice.nl.feature.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.service.FeatureService;
import org.arecap.eden.ia.console.microservice.event.FeatureSelectionChangedEvent;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamSelectionChangedEvent;
import org.arecap.eden.ia.console.mvp.DefaultFlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.component.HasNew;
import org.arecap.eden.ia.console.mvp.component.HasSelection;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringComponent
@UIScope
public class FeaturePresenter extends DefaultFlowEntityPresenter<FeatureView, Feature, Long, FeatureService> implements HasSelection<Feature>, HasNew<Feature> {

    private InformationalStream informationalStream;


    public Optional<InformationalStream> getInformationalStream() {
        return Optional.ofNullable(informationalStream);
    }

    public void setInformationalStream(InformationalStream informationalStream) {
        this.informationalStream = informationalStream;
    }

    @Override
    public void afterSave() {
        getView().setEditorFormVisibility(false);
        getView().setItems(getItems());
        getUIEventBus().publish(this, new FeatureSelectionChangedEvent(Optional.empty()));
    }

    @Override
    public void createItem() {
        getView().setEditorFormVisibility(getInformationalStream().isPresent());
        if(getInformationalStream().isPresent()) {
            Feature feature = new Feature();
            feature.setInformationalStream(informationalStream);
            setEntity(feature);
        }
    }

    @Override
    public void publishSelection(Optional<Feature> selection) {
        getUIEventBus().publish(this, new FeatureSelectionChangedEvent(selection));
    }

    @EventBusListenerMethod
    public void onInformationalStreamSelectionChangedEvent(InformationalStreamSelectionChangedEvent isSelectionChangedEvent) {
        Optional<InformationalStream> isSelection = (Optional<InformationalStream>) isSelectionChangedEvent.getSource();
        setInformationalStream(isSelection.orElse(null));
        getView().setItems(getItems());
        getView().setEnabled(isSelection.isPresent());
        getView().setI18nComboItems(new ArrayList<>());
        if(isSelection.isPresent()) {
            getView().setI18nComboItems(getService().findByInformationalStreamId(informationalStream.getI18nId()));
        }
        getUIEventBus().publish(this, new FeatureSelectionChangedEvent(Optional.empty()));
    }

    public void setI8nBean(Optional<Feature> value) {
        getEntity().setI18nId(value.isPresent() ? value.get().getId() : getEntity().getI18nId());
    }

    @Override
    public List<Feature> getItems() {
        return getInformationalStream().isPresent() ? getService().findAll(informationalStream.getId(), getView().getSearchText())
                : new ArrayList<>();
    }
}
