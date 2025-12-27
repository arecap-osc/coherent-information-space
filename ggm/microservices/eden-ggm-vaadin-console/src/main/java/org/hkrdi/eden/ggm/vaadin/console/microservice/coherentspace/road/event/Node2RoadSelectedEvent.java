package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.EventObject;

public class Node2RoadSelectedEvent extends EventObject {

	public Node2RoadSelectedEvent(NodeBean node) {
		super(node);
	}

}
