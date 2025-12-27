package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.Graphics2dStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class RoadFractoloniViewLayerManager extends RoadLayerManager {
    @Autowired
    private CoherentSpaceNetworkMediaLayerManager coherentSpaceNetworkMediaLayerManager;

    @Autowired
    private GgmViewIe applicationDataIe;

    @Autowired
    private ApplicationRepositoryService applicationService;

    @PostConstruct
    public void initLayers() {
        Optional<Application> application = applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(
                applicationDataIe.getApplicationName()).stream().findAny();
        if (application.isPresent() == false) {
            return;
        }
        Long applicationId = application.get().getId();
        MediaLayer mediaLayer = coherentSpaceNetworkMediaLayerManager.getMediaLayerByName("SUSTAINABLE_VERTICES::1").get();
        List<MediaRendererLayer> networkMediaRendererLayers = new ArrayList<>();
        networkMediaRendererLayers.add(new GgmSecondNodeMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
        networkMediaRendererLayers.add(new GgmNeighborNodeMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#00aa00"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
        networkMediaRendererLayers.add(new GgmFirstNodeMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));

        networkMediaRendererLayers.add(new RoadSelectedNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"), 4.0)));
        networkMediaRendererLayers.add(new RoadSelectedEdgesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"), 4.0)));
        networkMediaRendererLayers.add(new RoadFractolonNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#33ffcc"), 4.0)));
        networkMediaRendererLayers.add(new MarkedNodesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#1471a3"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
        networkMediaRendererLayers.add(new InformationNodesMediaLayer(mediaLayer.getName(), (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                ColorFactory.web("#ff0080"), applicationId));
        networkMediaRendererLayers.add(new HexavalentOtherNonAnimatedMediaLayer(mediaLayer.getName()));
        networkMediaRendererLayers.add(new HexavalentAnimatedMediaLayer(mediaLayer.getName()));

        networkMediaRendererLayers.add(new GgmPathLineMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#fca103"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));

        networkMediaRendererLayers.add(new RoadPathLinesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#aa0000"), 4.0)));
        networkMediaRendererLayers.add(new RoadFractolonEdgesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#33ffcc"), 4.0)));
        networkMediaRendererLayers.add(new MarkedEdgesMediaLayer(mediaLayer.getName(), new Graphics2dStyle(ColorFactory.web("#1471a3"),
                ((ConfigurableGraphics2dStyle) mediaLayer.getMediaRenderStyle()).getPenStroke())));
        networkMediaRendererLayers.add(new InformationEdgesMediaLayer(mediaLayer.getName(), (NetworkMediaLayerStyle) mediaLayer.getMediaRenderStyle(),
                ColorFactory.web("#ff0080"), applicationId));
        networkMediaRendererLayers.add(new HexavalentEdgesNonAnimatedMediaLayer(mediaLayer.getName()));
        coherentSpaceNetworkMediaLayerManager.setMediaRendererLayers(mediaLayer.getName(), networkMediaRendererLayers);
    }

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        return coherentSpaceNetworkMediaLayerManager.getMediaLayerByName("SUSTAINABLE_VERTICES::1").get().getMediaRendererLayers();
    }
}
