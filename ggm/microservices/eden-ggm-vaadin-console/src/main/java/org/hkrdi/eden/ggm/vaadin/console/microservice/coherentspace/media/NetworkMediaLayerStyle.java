package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import org.hkrdi.eden.ggm.vaadin.console.media.ConfigurableGraphics2dStyle;

public interface NetworkMediaLayerStyle extends ConfigurableGraphics2dStyle {

	Boolean isApplicationDataVisible();

	void setApplicationDataVisible(Boolean applicationDataVisible);

	Boolean isAddressIndexVisible();

	void setAddressIndexVisible(Boolean addressIndexVisible);

	Boolean isClusterIndexVisible();

	void setClusterIndexVisible(Boolean clusterIndexVisible);

	Boolean isTrivalentLogicVisible();

	void setTrivalentLogicVisible(Boolean trivalentLogicVisible);

}
