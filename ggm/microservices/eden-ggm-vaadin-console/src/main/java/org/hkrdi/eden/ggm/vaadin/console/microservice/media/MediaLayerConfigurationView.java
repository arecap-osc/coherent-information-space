package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;

/**
 * Component used to configure layers order, color, etc... 
 * MediaRendererLayer Manager.
 * Sort Layers. Cache static layers (for background)
 * 
 * @author abo
 *
 */
public abstract class MediaLayerConfigurationView extends HorizontalLayout implements FlowView {
	
	@Override
	public void onAttach(AttachEvent attachEvent) {
		FlowView.super.onAttach(attachEvent);
	}
}
