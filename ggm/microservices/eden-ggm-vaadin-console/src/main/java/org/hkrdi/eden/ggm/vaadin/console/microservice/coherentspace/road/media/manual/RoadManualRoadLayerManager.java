package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class RoadManualRoadLayerManager extends RoadLayerManager {
    @Autowired
    private CoherentSpaceNetworkMediaLayerManager coherentSpaceNetworkMediaLayerManager;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @PostConstruct
    public void initLayers() {
        coherentSpaceNetworkMediaLayerManager.getMediaLayers().stream()
                .forEach(mediaLayer -> {
                    List<MediaRendererLayer> networkMediaRendererLayers = new ArrayList<>();
                    networkMediaRendererLayers.add(new RoadSelectedNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"), 4.0)));
                    networkMediaRendererLayers.add(new RoadSelectedNeighbourNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#00aa00"), 4.0)));
                    networkMediaRendererLayers.add(new RoadStartNodeMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#aa0000"), 4.0)));
                    networkMediaRendererLayers.add(new RoadEndNodeMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#aa0000"), 4.0)));
                    networkMediaRendererLayers.add(new RoadIncludeNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#aa0000"), 4.0)));
                    networkMediaRendererLayers.add(new MarkedNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#1471a3"), ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new InformationNodesMediaLayer(mediaLayer.getName(), (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                            ColorFactory.web("#ff0080"), applicationDataIe.getApplicationId()));
                    networkMediaRendererLayers.add(new HexavalentOtherNonAnimatedMediaLayer(mediaLayer.getName()));
                    networkMediaRendererLayers.add(new HexavalentAnimatedMediaLayer(mediaLayer.getName()));
                    networkMediaRendererLayers.add(new RoadSelectedEdgesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"), 4.0)));
                    networkMediaRendererLayers.add(new RoadPathLinesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#aa0000"), 4.0)));
                    networkMediaRendererLayers.add(new MarkedEdgesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#1471a3"), ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
                    networkMediaRendererLayers.add(new InformationEdgesMediaLayer(mediaLayer.getName(), (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                            ColorFactory.web("#ff0080"), applicationDataIe.getApplicationId()));
                    networkMediaRendererLayers.add(new HexavalentEdgesNonAnimatedMediaLayer(mediaLayer.getName()));
                    coherentSpaceNetworkMediaLayerManager.setMediaRendererLayers(mediaLayer.getName(), networkMediaRendererLayers);
                });
    }
}