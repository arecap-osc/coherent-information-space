package org.hkrdi.eden.ggm.vaadin.console.media;

import java.io.InputStream;

public interface MediaRendererDrawer {
	InputStream draw(MediaRendererTransform mediaRendererTransform);
}
