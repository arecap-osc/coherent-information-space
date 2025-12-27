package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;

public class SimpleMediaRendererTransform implements MediaRendererTransform {
	private Double scale = 1D;
	private Double width = 1D;
	private Double height = 1D;
	private Double centerX = 0D;
	private Double centerY = 0D;
	
	public void config(Double scale, Double width, Double height, Double centerX, Double centerY) {
		this.scale = scale;
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public void config(MediaRendererTransform mrt) {
		config(mrt.getScale(), mrt.getWidth(), mrt.getHeight(), mrt.getCenterX(), mrt.getCenterY());
	}

	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getCenterX() {
		return centerX;
	}
	public void setCenterX(Double centerX) {
		this.centerX = centerX;
	}
	public Double getCenterY() {
		return centerY;
	}
	public void setCenterY(Double centerY) {
		this.centerY = centerY;
	}	
}
