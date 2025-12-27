package org.hkrdi.eden.ggm.media;

import org.springframework.data.geo.Point;

public class DrawingOption {

    private DrawingStyle drawingStyle;

    private Point center;

    private Point bottomRight;

    private double scale;

    public DrawingOption(DrawingStyle drawingStyle, Point center, Point bottomRight, double scale) {
        this.drawingStyle = drawingStyle;
        this.center = center;
        this.bottomRight = bottomRight;
        this.scale = scale;
    }

    public DrawingStyle getDrawingStyle() {
        return drawingStyle;
    }

    public void setDrawingStyle(DrawingStyle drawingStyle) {
        this.drawingStyle = drawingStyle;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Point bottomRight) {
        this.bottomRight = bottomRight;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
