package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererLayer;

import java.util.List;

public interface IndexSelectionMediaRendererLayer extends MediaRendererLayer, NetworkMediaLayer{
	List<Long> getSelectedIndex();
	
}
