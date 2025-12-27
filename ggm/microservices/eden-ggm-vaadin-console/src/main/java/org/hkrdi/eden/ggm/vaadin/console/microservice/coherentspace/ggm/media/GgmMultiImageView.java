package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media;

import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GgmMultiImageView extends MultiImageView{


	@Autowired
	private NodeInformationView nodeInformationView;

	@Override
	public void buildView() {
		super.buildView();
		add(nodeInformationView);
	}

	public NodeInformationView getNodeInformationView() {
		return nodeInformationView;
	}
}
