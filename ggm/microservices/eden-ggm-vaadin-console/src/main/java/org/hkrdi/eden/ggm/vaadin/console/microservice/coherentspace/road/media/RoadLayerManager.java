package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.NeedRefreshUpdatedLayerEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.InformationEdgesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.InformationNodesMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.RoadApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

public abstract class RoadLayerManager implements MediaRenderLayerFactory, MediaLayerFactory {
    @Autowired
    private CoherentSpaceNetworkMediaLayerManager coherentSpaceNetworkMediaLayerManager;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private UIEventBus uIEventBus;

    @PostConstruct
    private void initEventBus() {
        uIEventBus.subscribe(this);
    }

    @PreDestroy
    private void predestroyEventBus() {
        uIEventBus.unsubscribe(this);
    }

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        List<MediaRendererLayer> mediaRendererLayers = new ArrayList<>();
        coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                .filter(MediaLayer::isVisible)
                .forEach(mediaLayer -> mediaRendererLayers.addAll(mediaLayer.getMediaRendererLayers()));
        return mediaRendererLayers;
    }

    @Override
    public List<MediaLayer> getMediaLayers() {
        //TODO add **
        return coherentSpaceNetworkMediaLayerManager.getMediaLayers();
    }


    @EventBusListenerMethod
    public void onApplicationChangedEvent(RoadApplicationChangeEvent event) {
        List<MediaRendererLayer> mediaRendererLayers = getMediaLayers().stream()
                .flatMap(mediaLayer -> mediaLayer.getMediaRendererLayers().stream())
                .collect(Collectors.toList());
        refreshInformationForLayers(mediaRendererLayers);
    }

    @EventBusListenerMethod
    public void onInformationSavedEvent(InformationSavedEvent event) {
        refreshInformationForLayers(getMediaRendererLayers());
    }

    public void refreshInformationForLayers(List<MediaRendererLayer> layers) {
        layers.stream()
                .filter(layer -> layer instanceof InformationEdgesMediaLayer)
                .map(layer -> ((InformationEdgesMediaLayer) layer))
                .forEach(layer -> layer.setApplicationId(applicationDataIe.getApplicationId()));
        layers.stream()
                .filter(layer -> layer instanceof InformationNodesMediaLayer)
                .map(layer -> ((InformationNodesMediaLayer) layer))
                .forEach(layer -> layer.setApplicationId(applicationDataIe.getApplicationId()));
        uIEventBus.publish(this, new NeedRefreshUpdatedLayerEvent());
    }
}

