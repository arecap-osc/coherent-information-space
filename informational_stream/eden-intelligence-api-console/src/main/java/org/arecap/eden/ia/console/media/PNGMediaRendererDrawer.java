package org.arecap.eden.ia.console.media;

import org.arecap.eden.ia.console.media.util.Graphics2dUtils;
import org.arecap.eden.ia.console.media.util.encoder.PngEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface PNGMediaRendererDrawer extends MediaRendererDrawer{

	void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform);

	default InputStream draw(MediaRendererTransform mediaRendererTransform) {

		BufferedImage bufferedImage = new BufferedImage(mediaRendererTransform.getWidth().intValue(),
															mediaRendererTransform.getHeight().intValue(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage,
																		mediaRendererTransform.getWidth().intValue(),
                														mediaRendererTransform.getHeight().intValue());
		drawContent(graphics2D, mediaRendererTransform);
		long t = System.currentTimeMillis();


        InputStream is = null;
		try {
			is = new ByteArrayInputStream(toByteArrayAutoClosable(bufferedImage, "PNG"));
		}catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Write image processing :\t" + (System.currentTimeMillis() - t) + "\tms");
		return is;

	}

	default byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
			new PngEncoder().write(image, out);
			return out.toByteArray();
		} catch (Exception e) {
		}
		return null;
	}

}
