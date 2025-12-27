package org.arecap.eden.ia.console.microservice.nl.feature.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.api.InformationalStreamUtils;
import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.informationalstream.support.bean.FeatureStream;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.service.FeatureStreamService;
import org.arecap.eden.ia.console.informationalstream.support.service.SignalService;
import org.arecap.eden.ia.console.microservice.event.FeatureSelectionChangedEvent;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamSelectionChangedEvent;
import org.arecap.eden.ia.console.mvp.DefaultFlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.component.HasNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class FeatureStreamPresenter extends DefaultFlowEntityPresenter<FeatureStreamView, FeatureStream, Long, FeatureStreamService> implements HasNew<FeatureStream> {

    @Autowired
    private SignalService signalService;

    private InformationalStream informationalStream;

    private Feature feature;

    private Long getInformationalStreamId() {
        return getInformationalStream().isPresent() ? getInformationalStream().get().getId() : null;
    }

    private Long getFeatureId() {
        return getFeature().isPresent() ? getFeature().get().getId() : null;
    }

    public Optional<InformationalStream> getInformationalStream() {
        return Optional.ofNullable(informationalStream);
    }

    public void setInformationalStream(InformationalStream informationalStream) {
        this.informationalStream = informationalStream;
    }

    public Optional<Feature> getFeature() {
        return Optional.ofNullable(feature);
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @EventBusListenerMethod
    public void onInformationalStreamSelectionChangedEvent(InformationalStreamSelectionChangedEvent isSelectionChangedEvent) {
        Optional<InformationalStream> isSelection = (Optional<InformationalStream>) isSelectionChangedEvent.getSource();
        setInformationalStream(isSelection.orElse(null));
        getView().setItems(getItems());
        getView().setUpstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
        getView().setDownstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
    }

    @EventBusListenerMethod
    public void onFeatureSelectionChangedEvent(FeatureSelectionChangedEvent featureSelectionChangedEvent) {
        Optional<Feature> featureSelection = (Optional<Feature>) featureSelectionChangedEvent.getSource();
        setFeature(featureSelection.orElse(null));
        getView().setItems(getItems());
        getView().setUpstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
        getView().setDownstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
    }

    @Override
    public void createItem() {
        setEntity(new FeatureStream());
        getView().setUpstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
        getView().setDownstreamItems(signalService.findAll(getInformationalStreamId(), getFeatureId(), ""));
    }

    @Override
    public void beforeSave() {
        InformationalStreamUtils.setFeatureStreamTopology(getEntity());
    }

    @Override
    public List<FeatureStream> getItems() {
        return getService().findAll(getInformationalStreamId(), getFeatureId());
    }
}
