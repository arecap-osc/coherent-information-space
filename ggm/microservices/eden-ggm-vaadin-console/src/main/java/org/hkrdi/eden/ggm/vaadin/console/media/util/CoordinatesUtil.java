package org.hkrdi.eden.ggm.vaadin.console.media.util;

import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.springframework.data.geo.Point;

public final class CoordinatesUtil {
	//resize+scale+translate from top/left corner to graphics center+translate to request center  
	public static Point getAlgebraicToRequestGraphics(Point algebraic, MediaRendererTransform mrt) {
		Point algebraicScaled = getAlgebraicToScaledResizedGraphics(algebraic, mrt.getScale());
		return new Point(GeometryUtil.round(algebraicScaled.getX() + mrt.getCenterX() + mrt.getWidth() / 2, 5),
				GeometryUtil.round(algebraicScaled.getY() + mrt.getCenterY() + mrt.getHeight() / 2,5));
	}
	
	public static Point getAlgebraicToResizedGraphics(Point algebraic) {
		return new Point( GeometryUtil.round(algebraic.getX() / 150_000L, 7), GeometryUtil.round(algebraic.getY() / 150_000L, 7));
	}
	
	private static Point getAlgebraicToScaledResizedGraphics(Point algebraic, Double scale) {
		Point graphicsNotScaledCoordinates = getAlgebraicToResizedGraphics(algebraic);
		return new Point(graphicsNotScaledCoordinates.getX() * scale, graphicsNotScaledCoordinates.getY() * scale);
	}
}
