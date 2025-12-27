package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.EventObject;

public class NodeRoadSelectionClickEvent extends EventObject {

	public NodeRoadSelectionClickEvent(NodeBean node) {
		super(node);
	}

}
