package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.edge;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmEdgeMultiImageView extends GgmMultiImageView {

	@Autowired
	private GgmEdgeMultiImagePresenter presenter;


	@Override
	public GgmEdgeMultiImagePresenter getPresenter() {
		return presenter;
	}
	
}
