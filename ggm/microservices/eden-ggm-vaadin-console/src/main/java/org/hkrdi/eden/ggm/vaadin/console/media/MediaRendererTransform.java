package org.hkrdi.eden.ggm.vaadin.console.media;

public interface MediaRendererTransform{
	
	Double getScale();

	Double getCenterX();

	Double getCenterY();

	Double getWidth();

	Double getHeight();

	void setScale(Double scale);

	void setCenterX(Double centerX);

	void setCenterY(Double centerY);

	void setWidth(Double width);

	void setHeight(Double height);

	default boolean equalz(MediaRendererTransform obj) {
		if (getCenterX() == null) {
			if (obj.getCenterX() != null)
				return false;
		} else if (!getCenterX().equals(obj.getCenterX()))
			return false;
		if (getCenterY() == null) {
			if (obj.getCenterY() != null)
				return false;
		} else if (!getCenterY().equals(obj.getCenterY()))
			return false;
		if (getHeight() == null) {
			if (obj.getHeight() != null)
				return false;
		} else if (!getHeight().equals(obj.getHeight()))
			return false;
		if (getScale() == null) {
			if (obj.getScale() != null)
				return false;
		} else if (!getScale().equals(obj.getScale()))
			return false;
		if (getWidth() == null) {
			if (obj.getWidth() != null)
				return false;
		} else if (!getWidth().equals(obj.getWidth()))
			return false;
		return true;
	}

}
