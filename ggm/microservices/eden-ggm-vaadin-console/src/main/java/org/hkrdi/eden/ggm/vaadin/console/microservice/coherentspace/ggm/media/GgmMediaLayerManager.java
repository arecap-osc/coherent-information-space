package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.event.GgmApplicationChangeEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.InformationSavedEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.event.NeedRefreshUpdatedLayerEvent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class GgmMediaLayerManager implements MediaRenderLayerFactory, MediaLayerFactory, StateSelectionManager {

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

    @PostConstruct
    private void initLayers() {
        coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                .forEach(mediaLayer -> {
                    List<MediaRendererLayer> networkMediaRendererLayers = new ArrayList<>();
                    networkMediaRendererLayers.add(new GgmSecondNodeMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#fca103"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new GgmNeighborNodeMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#00aa00"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new GgmFirstNodeMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#fca103"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new MarkedNodesMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#1471a3"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new InformationNodesMediaLayer(mediaLayer.getName(),
                            (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                            ColorFactory.web("#ff0080"), applicationDataIe.getApplicationId()));
                    networkMediaRendererLayers.add(new HexavalentOtherNonAnimatedMediaLayer(mediaLayer.getName()));
                    networkMediaRendererLayers.add(new HexavalentAnimatedMediaLayer(mediaLayer.getName()));
                    networkMediaRendererLayers.add(new GgmPathLineMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#fca103"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new MarkedEdgesMediaLayer(mediaLayer.getName(),
                            new Graphics2dStyle(ColorFactory.web("#1471a3"),
                                    ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new InformationEdgesMediaLayer(mediaLayer.getName(),
                            (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                            ColorFactory.web("#ff0080"), applicationDataIe.getApplicationId()));
                    networkMediaRendererLayers.add(new HexavalentEdgesNonAnimatedMediaLayer(mediaLayer.getName()));
                    coherentSpaceNetworkMediaLayerManager.setMediaRendererLayers(mediaLayer.getName(), networkMediaRendererLayers);
                });
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

    public MarkedNodesMediaLayer getMarkedNodesMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, MarkedNodesMediaLayer.class).get();
    }

    public MarkedEdgesMediaLayer getMarkedEdgesMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, MarkedEdgesMediaLayer.class).get();
    }

    public GgmFirstNodeMediaLayer getFirstNodeMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, GgmFirstNodeMediaLayer.class).get();
    }

    public GgmSecondNodeMediaLayer getSecondNodeMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, GgmSecondNodeMediaLayer.class).get();
    }

    public GgmNeighborNodeMediaLayer getNeighborNodeMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, GgmNeighborNodeMediaLayer.class).get();
    }

    public GgmPathLineMediaLayer getPathLineMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, GgmPathLineMediaLayer.class).get();
    }

    public Optional<InformationNodesMediaLayer> getInformationNodesMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, InformationNodesMediaLayer.class);
    }

    public Optional<InformationEdgesMediaLayer> getInformationEdgesMediaLayer(String network) {
        return getLayerByNetworkAndType(this, network, InformationEdgesMediaLayer.class);
    }

    @EventBusListenerMethod
    public void onGgmApplicationChangedEvent(GgmApplicationChangeEvent event) {
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
    
    @Override
    public String getCurrentNetwork() {
		return applicationDataIe.getNetwork();
	}

    @Override
    public Long getCurrentApplicationId() {
        return applicationDataIe.getApplicationId();
    }
}