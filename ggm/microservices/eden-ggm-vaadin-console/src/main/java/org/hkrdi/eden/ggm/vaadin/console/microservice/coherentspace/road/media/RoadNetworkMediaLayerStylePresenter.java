package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media;


import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.CoherentSpaceNetworkMediaLayerManager;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStylePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event.ForceRefreshRoadMultiImageEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadNetworkMediaLayerStylePresenter extends NetworkMediaLayerStylePresenter {
    @Autowired
    private CoherentSpaceNetworkMediaLayerManager mediaLayerFactory;

    @Override
    protected MediaLayerFactory getMediaLayerFactory() {
        return mediaLayerFactory;
    }

    @Override
    protected void notifyMultiImageView() {
    	getUIEventBus().publish(this, new ForceRefreshRoadMultiImageEvent());
    }

}
