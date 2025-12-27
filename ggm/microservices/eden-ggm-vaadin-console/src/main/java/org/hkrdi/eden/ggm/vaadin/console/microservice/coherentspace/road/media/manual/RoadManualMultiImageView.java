package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.manual;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadManualMultiImageView extends RoadMultiImageView{

	@Autowired
	private RoadManualMultiImagePresenter presenter;
	
	@Override
	public RoadMultiImagePresenter getPresenter() {
		return presenter;
	}
}
