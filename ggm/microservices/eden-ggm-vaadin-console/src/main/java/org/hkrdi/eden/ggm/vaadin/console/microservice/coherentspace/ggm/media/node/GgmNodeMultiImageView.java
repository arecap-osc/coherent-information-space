package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.node;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmNodeMultiImageView extends GgmMultiImageView {

	@Autowired
	private GgmNodeMultiImagePresenter presenter;


	@Override
	public GgmNodeMultiImagePresenter getPresenter() {
		return presenter;
	}
	
}
