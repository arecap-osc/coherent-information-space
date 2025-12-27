package org.hkrdi.eden.ggm.vaadin.console.media;

import org.hkrdi.eden.ggm.vaadin.console.media.util.AnimatedGIFWriter;
import org.hkrdi.eden.ggm.vaadin.console.media.util.Graphics2dUtils;
import org.hkrdi.eden.ggm.vaadin.console.media.util.encoder.PngEncoder;

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
//
		Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage,
																		mediaRendererTransform.getWidth().intValue(),
                														mediaRendererTransform.getHeight().intValue());
//
		drawContent(graphics2D, mediaRendererTransform);
		long t = System.currentTimeMillis();
//		BufferedImage bi = drawFrame(mediaRendererTransform, 0);

//		InputStream is = drawGif(mediaRendererTransform);

//        PngEncoder encoder = new PngEncoder(bufferedImage, true);
//        encoder.setCompressionLevel(9);
//        InputStream is = new ByteArrayInputStream(encoder.pngEncode());

        InputStream is = null;
		try {
			is = new ByteArrayInputStream(toByteArrayAutoClosable(bufferedImage, "PNG"));
		}catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("Write image processing :\t" + (System.currentTimeMillis() - t) + "\tms");
		return is;

	}

	default InputStream drawGif(MediaRendererTransform mediaRendererTransform) {
		AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			writer.prepareForWrite(os, mediaRendererTransform.getWidth().intValue(),
					mediaRendererTransform.getHeight().intValue());

			writer.writeFrame(os, drawFrame(mediaRendererTransform, 0), 0);
			writer.finishWrite(os);
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	default BufferedImage drawFrame(MediaRendererTransform mediaRendererTransform, Integer frameNo) {
		BufferedImage bufferedImage = new BufferedImage(mediaRendererTransform.getWidth().intValue(),
				mediaRendererTransform.getHeight().intValue(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage,
				mediaRendererTransform.getWidth().intValue(), mediaRendererTransform.getHeight().intValue());

		drawContent(graphics2D, mediaRendererTransform);

		return bufferedImage;
	}

	default byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
			new PngEncoder().write(image, out);
//			ImageIO.write(image, type, out);
			return out.toByteArray();
		} catch (Exception e) {
		}
		return null;
	}
}
