package org.hkrdi.eden.ggm.media.coherentspace.layerprocessor;

import java.awt.Graphics2D;

import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;

public interface LayerProcessorI {
	public boolean accept(String layerType);
	
	public void process(Graphics2D graphics2D, DrawingOption drawingOption, String network, ApplicationDataParameter applicationDataParameter);
}
