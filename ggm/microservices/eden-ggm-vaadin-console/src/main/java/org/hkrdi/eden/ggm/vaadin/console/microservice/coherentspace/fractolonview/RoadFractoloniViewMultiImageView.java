package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class RoadFractoloniViewMultiImageView extends RoadMultiImageView{

	@Autowired
	private RoadFractoloniViewMultiImagePresenter presenter;
	
	@Override
	public RoadFractoloniViewMultiImagePresenter getPresenter() {
		return presenter;
	}

	@Override
	public void afterPrepareView() {
		getPresenter().getMediaRendererLayerFactory().initLayers();
	}
}
