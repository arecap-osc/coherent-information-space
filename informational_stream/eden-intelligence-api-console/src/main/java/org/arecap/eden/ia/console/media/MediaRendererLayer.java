package org.arecap.eden.ia.console.media;

public interface MediaRendererLayer extends MediaRendererDrawer {

	Boolean isNeedRefresh();

	void setNeedRefresh(Boolean needRefresh);

	default String getName() {
		return "IMG_" + this;
	}

	/**
	 * used to reset the layer to the initial state (for ex to remove selections)
	 */
	default void reset() {
	}
}
