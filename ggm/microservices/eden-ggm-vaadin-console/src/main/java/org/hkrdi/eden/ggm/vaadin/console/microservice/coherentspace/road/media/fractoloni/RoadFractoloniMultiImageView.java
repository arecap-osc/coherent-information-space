package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.fractoloni;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual.RoadManualMultiImagePresenter;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadFractoloniMultiImageView extends RoadMultiImageView{

	@Autowired
	private RoadFractoloniMultiImagePresenter presenter;
	
	@Override
	public RoadMultiImagePresenter getPresenter() {
		return presenter;
	}
}
