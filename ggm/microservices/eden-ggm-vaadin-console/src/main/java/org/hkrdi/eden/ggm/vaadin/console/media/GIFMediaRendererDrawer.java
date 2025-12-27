package org.hkrdi.eden.ggm.vaadin.console.media;

import org.hkrdi.eden.ggm.vaadin.console.media.util.AnimatedGIFWriter;
import org.hkrdi.eden.ggm.vaadin.console.media.util.Graphics2dUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface GIFMediaRendererDrawer extends MediaRendererDrawer {
	default Integer getTotalFramesNo() {
		return 4;
	}

	default Integer getDelayMs() {
		return 300;
	}

	void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform, Integer frameNo);

	default InputStream draw(MediaRendererTransform mediaRendererTransform) {
		AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			writer.prepareForWrite(os, mediaRendererTransform.getWidth().intValue(),
					mediaRendererTransform.getHeight().intValue());
			for (int frameNo = 1; frameNo <= getTotalFramesNo(); frameNo++) {
				writer.writeFrame(os, drawFrame(mediaRendererTransform, frameNo), getDelayMs());
			}
			writer.finishWrite(os);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(os.toByteArray());
	}

	default BufferedImage drawFrame(MediaRendererTransform mediaRendererTransform, Integer frameNo) {
		BufferedImage bufferedImage = new BufferedImage(mediaRendererTransform.getWidth().intValue(),
				mediaRendererTransform.getHeight().intValue(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage,
				mediaRendererTransform.getWidth().intValue(), mediaRendererTransform.getHeight().intValue());

		drawContent(graphics2D, mediaRendererTransform, frameNo);

		return bufferedImage;
	}

	default int[] frameParts(int frame) {
		if (frame == 1) {
			return new int[] { 0, 1, 4, 5, 8, 9 };
		} else if (frame == 2) {
			return new int[] { 1, 2, 5, 6, 9, 10 };
		} else if (frame == 3) {
			return new int[] { 2, 3, 6, 7, 10, 11 };
		} else if (frame == 4) {
			return new int[] { 3, 4, 7, 8, 11, 12 };
		}
		return new int[] { 0, 12 };
	}

}
