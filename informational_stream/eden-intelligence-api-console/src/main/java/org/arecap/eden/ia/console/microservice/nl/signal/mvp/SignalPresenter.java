package org.arecap.eden.ia.console.microservice.nl.signal.mvp;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.informationalstream.support.bean.Feature;
import org.arecap.eden.ia.console.informationalstream.support.bean.InformationalStream;
import org.arecap.eden.ia.console.informationalstream.support.bean.Signal;
import org.arecap.eden.ia.console.informationalstream.support.service.SignalService;
import org.arecap.eden.ia.console.microservice.event.FeatureSelectionChangedEvent;
import org.arecap.eden.ia.console.microservice.event.InformationalStreamSelectionChangedEvent;
import org.arecap.eden.ia.console.microservice.layout.UiI18nUtil;
import org.arecap.eden.ia.console.mvp.DefaultFlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.component.HasNew;
import org.arecap.eden.ia.console.mvp.component.HasSelection;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.*;
import java.util.stream.Collectors;


@SpringComponent
@UIScope
public class SignalPresenter extends DefaultFlowEntityPresenter<SignalView, Signal, Long, SignalService> implements HasSelection<Signal>, HasNew<Signal> {

    private InformationalStream informationalStream;

    private Feature feature;

    @Override
    public void afterPrepareModel(EventObject event) {
        getView().setItems(getItems());
    }

    public List<Signal> getItems() {
        return getService().findAll(getInformationalStreamId(), getFeatureId(), getView().getSearchText()).stream()
                .filter(signal -> !getView().getStreamApplicationTypeFilter().isPresent() || signal.getStreamApplicationType().equals(getView().getStreamApplicationTypeFilter().get()))
                .filter(signal -> !getView().getI18nFilter().isPresent() || signal.getLocale().equals(getView().getI18nFilter().get()))
                .collect(Collectors.toList());
    }

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
        if(isSelection.isPresent() && isSelection.get().getLocale().equals(UI.getCurrent().getLocale())) {
            refreshItems(isSelection.get().getLocale());
        }
        setInformationalStream(isSelection.orElse(null));
        getView().setItems(getItems());
    }

    @EventBusListenerMethod
    public void onFeatureSelectionChangedEvent(FeatureSelectionChangedEvent featureSelectionChangedEvent) {
        Optional<Feature> featureSelection = (Optional<Feature>) featureSelectionChangedEvent.getSource();
        setFeature(featureSelection.orElse(null));
        getView().setItems(getItems());
    }

    public void setLocale(Locale locale) {
        if(locale != null && getBinder() != null) {
            if(getEntity().getId() == null) {
                getEntity().setLocale(locale);
            }else {
                setEntity(getEntity());
            }
            refreshItems(locale);
        }
    }

    private void refreshItems(Locale locale){
        List<Signal> congruenceI18nItems = new ArrayList<>();
        List<Signal> similarityCongruenceItems = new ArrayList<>();
        if(getEntity() != null && getEntity().getId() != null) {
            congruenceI18nItems.addAll(getEntity().getCongruence());
            getService().getI18nBeans(getEntity()).stream().filter(s -> !s.getLocale().equals(getEntity().getLocale())).forEach(i18n -> {
                congruenceI18nItems.add(i18n);
                congruenceI18nItems.addAll(getService().getCongruenceSignals(i18n));
            });
            similarityCongruenceItems.addAll(getEntity().getSimilarity());
        }
        getView().setCongruenceItems(congruenceI18nItems);
        getView().setSimilarityItems(similarityCongruenceItems);
        getView().setI18nComboItems(getService().findAllByLocaleNot(locale));
        getView().setCongruenceComboItems(getService().findAllByLocale(locale));
        getView().setSimilarityComboItems(getService().findAllByLocale(locale));
        setBeans();
    }

    @Override
    public void publishSelection(Optional<Signal> firstSelectedItem) {
        if(firstSelectedItem.isPresent()) {
            if(UI.getCurrent().getLocale().equals(getEntity().getLocale())) {
                refreshItems(getEntity().getLocale());
                return;
            }
            UiI18nUtil.setLocale(getEntity().getLocale());
        }
    }

    @Override
    public void afterSave() {
        setI18nCongruence();
        setI18nSimilarity();
        getView().setEditorFormVisibility(false);
        getView().setItems(getItems());
    }

    private void setI18nSimilarity() {
        if(getEntity().getSimilarityId() != null) {
            Optional<Signal> similaritySignal = getService().findById(getEntity().getSimilarityId());
            if(similaritySignal.isPresent()) {
                getService().getI18nBeans(getEntity()).stream()
                        .filter(signal -> !signal.getLocale().equals(getEntity().getLocale()))
                        .forEach(i18nSignal -> {
                            i18nSignal.setSimilarityId(similaritySignal.get().getI18nId());
                            getService().save(i18nSignal);
                        });
            }
        }
    }

    private void setI18nCongruence() {
        if(getEntity().getCongruenceId() != null) {
            Optional<Signal> congruenceSignal = getService().findById(getEntity().getCongruenceId());
            if(congruenceSignal.isPresent()) {
                getService().getI18nBeans(getEntity()).stream()
                        .filter(signal -> !signal.getLocale().equals(getEntity().getLocale()))
                        .forEach(i18nSignal -> {
                            i18nSignal.setCongruenceId(congruenceSignal.get().getI18nId());
                            getService().save(i18nSignal);
                        });
            }
        }
    }

    @Override
    public void createItem() {
        Signal signal = new Signal();
        signal.setInformationalStream(getInformationalStream().orElse(null));
        signal.setFeature(getFeature().orElse(null));
        signal.setLocale(getInformationalStream().isPresent() ?
                informationalStream.getLocale() : UI.getCurrent().getLocale());
        setEntity(signal);
    }

    public void setI8nBean(Optional<Signal> value) {
        getEntity().setI18nId(value.isPresent() ? value.get().getId() : getEntity().getI18nId());
    }

    private void setBeans() {
        if(getEntity() != null && getEntity().getId() != null) {
            getView().setI18nValue(getService().getI18nBeans(getEntity()).stream()
                    .filter(s -> !s.getLocale().equals(getEntity().getLocale())).findFirst());
            if(getEntity().getCongruenceId() != null) {
                getView().setCongruenceValue(getService().findById(getEntity().getCongruenceId()));
            }
            if(getEntity().getSimilarityId() != null) {
                getView().setSimilarityValue(getService().findById(getEntity().getSimilarityId()));
            }
        }
    }

    public void setCongruenceBean(Optional<Signal> value) {
        getEntity().setCongruenceId(value.isPresent() ? value.get().getId() : getEntity().getCongruenceId());
    }

    public void setSimilarityBean(Optional<Signal> value) {
        getEntity().setSimilarityId(value.isPresent() ? value.get().getId() : getEntity().getSimilarityId());
    }


}
