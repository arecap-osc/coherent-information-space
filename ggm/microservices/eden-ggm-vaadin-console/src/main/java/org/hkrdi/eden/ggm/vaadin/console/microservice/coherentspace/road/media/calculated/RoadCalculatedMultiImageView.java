package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.calculated;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImagePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.media.RoadMultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadCalculatedMultiImageView extends RoadMultiImageView{

	@Autowired
	private RoadCalculatedMultiImagePresenter presenter;
	
	@Override
	public RoadMultiImagePresenter getPresenter() {
		return presenter;
	}
}
