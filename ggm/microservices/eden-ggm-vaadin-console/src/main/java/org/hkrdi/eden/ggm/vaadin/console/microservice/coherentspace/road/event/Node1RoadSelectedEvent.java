package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.event;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.EventObject;

public class Node1RoadSelectedEvent extends EventObject {

	public Node1RoadSelectedEvent(NodeBean node) {
		super(node);
	}

}
