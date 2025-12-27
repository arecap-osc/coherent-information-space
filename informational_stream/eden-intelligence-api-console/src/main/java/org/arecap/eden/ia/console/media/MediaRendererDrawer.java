package org.arecap.eden.ia.console.media;

import java.io.InputStream;

public interface MediaRendererDrawer {
	InputStream draw(MediaRendererTransform mediaRendererTransform);
}
