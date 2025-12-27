package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderLayerFactory;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.StateSelectionManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadCalculatedMultiImagePresenter extends RoadMultiImagePresenter{
	@Autowired
	private RoadCalculatedRoadStateSelectionManager stateSelectionManager;

	@Override
	public StateSelectionManager getStateSelectionManager() {
		return stateSelectionManager;
	}
	
	@Override
	public MediaRenderLayerFactory getMediaRendererLayerFactory() {
		return stateSelectionManager.getLayerManager();
	}

	@Override
	public MediaLayerFactory getMediaLayerFactory() {
		return stateSelectionManager.getLayerManager();
	}
}
