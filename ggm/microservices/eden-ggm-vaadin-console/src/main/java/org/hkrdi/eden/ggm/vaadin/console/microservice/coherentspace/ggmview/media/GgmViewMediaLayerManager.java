package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.media.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmFirstNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmNeighborNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmPathLineMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.layer.GgmSecondNodeMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer.*;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmRouteApplicationDataIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.ie.GgmViewIe;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
public class GgmViewMediaLayerManager implements MediaRenderLayerFactory, MediaLayerFactory, StateSelectionManager {

    private MediaLayer ggmViewLayer;

    @Autowired
    private GgmViewIe viewApplicationDataIe;

    @Autowired
    private GgmRouteApplicationDataIe applicationDataIe;

    @Autowired
    private ApplicationRepositoryService applicationService;
    
    @PostConstruct
    private void initViewLayer() {
        List<Application> application =  applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        ggmViewLayer = new CoherentSpaceNetworkMediaLayer(viewApplicationDataIe.getNetwork());
        ((CoherentSpaceNetworkMediaLayer)ggmViewLayer).setAddressIndexVisible(true);
        ((CoherentSpaceNetworkMediaLayer)ggmViewLayer).setApplicationDataVisible(true);
        ((CoherentSpaceNetworkMediaLayer)ggmViewLayer).setPenStroke(6.6D);
        //TODO makea static util for load default ggm coherent space media renderer layers
        if(application.size() > 0) {
            applicationDataIe.setApplicationId(application.get(0).getId());
            ggmViewLayer.getMediaRendererLayers().add(new GgmSecondNodeMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#fca103"), ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new GgmNeighborNodeMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#00aa00"), ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new GgmFirstNodeMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#fca103"), ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new MarkedNodesMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#1471a3"),
                            ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new InformationNodesMediaLayer(ggmViewLayer.getName(),
                    (NetworkMediaLayerStyle) ggmViewLayer.getMediaRenderStyle(), ColorFactory.web("#ff0080"), application.get(0).getId()));
            ggmViewLayer.getMediaRendererLayers().add(new HexavalentOtherNonAnimatedMediaLayer(ggmViewLayer.getName()));
            ggmViewLayer.getMediaRendererLayers().add(new HexavalentAnimatedMediaLayer(ggmViewLayer.getName()));
            ggmViewLayer.getMediaRendererLayers().add(new GgmPathLineMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#fca103"),
                            ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new MarkedEdgesMediaLayer(ggmViewLayer.getName(),
                    new Graphics2dStyle(ColorFactory.web("#1471a3"),
                            ((ConfigurableGraphics2dStyle) ggmViewLayer.getMediaRenderStyle()).getPenStroke())));
            ggmViewLayer.getMediaRendererLayers().add(new InformationEdgesMediaLayer(ggmViewLayer.getName(),
                    (NetworkMediaLayerStyle) ggmViewLayer.getMediaRenderStyle(), ColorFactory.web("#ff0080"), application.get(0).getId()));
            ggmViewLayer.getMediaRendererLayers().add(new HexavalentEdgesNonAnimatedMediaLayer(ggmViewLayer.getName()));
        }
        getMediaRendererLayers().stream()
                .filter(mediaRendererLayer -> MediaRenderStyleAware.class.isAssignableFrom(mediaRendererLayer.getClass()))
                .map(mediaRendererLayer -> (MediaRenderStyleAware)mediaRendererLayer)
                .forEach(mediaRenderStyleAware -> mediaRenderStyleAware.setMediaRenderStyle(ggmViewLayer.getMediaRenderStyle()));
    }

    //TODO this code is duplicate it could be a default interface
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

    @Override
    public List<MediaLayer> getMediaLayers() {
        List<MediaLayer> mapList = new ArrayList<>();
        mapList.add(ggmViewLayer);
        return mapList;
    }

    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        return ggmViewLayer.getMediaRendererLayers();
    }

    @Override
    public String getCurrentNetwork() {
		return viewApplicationDataIe.getNetwork();
	}

    @Override
    public Long getCurrentApplicationId() {
        List<Application> applicationList = applicationService.getAllApplicationsByBriefLabelStartsWithIgnoreCase(viewApplicationDataIe.getApplicationName());
        if (applicationList.size() > 0) {
            return applicationList.get(0).getId();
        }
        return null;
    }
}
