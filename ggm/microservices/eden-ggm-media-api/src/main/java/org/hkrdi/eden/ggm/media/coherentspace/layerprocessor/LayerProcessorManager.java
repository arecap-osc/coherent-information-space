package org.hkrdi.eden.ggm.media.coherentspace.layerprocessor;

import java.awt.Graphics2D;

import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;

public class LayerProcessorManager {
	private LayerProcessorI[] layers = {new CoherentSpaceLayerProcessor(), new RouteLayerProcessor()};
	
	public void paint(Graphics2D graphics2D, DrawingOption drawingOption, String network, ApplicationDataParameter applicationDataParameter) {
		for (LayerProcessorI layerProcessor: layers) {
			if (layerProcessor.accept(applicationDataParameter.getDataMapParameter().getLayerType())) {
				layerProcessor.process(graphics2D, drawingOption, network, applicationDataParameter);
				return;
			}
		}
	}
}
