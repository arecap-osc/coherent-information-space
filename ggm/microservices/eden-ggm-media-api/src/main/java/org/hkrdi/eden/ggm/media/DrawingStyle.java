package org.hkrdi.eden.ggm.media;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DrawingStyle implements Serializable {

    @JsonProperty
    private double pen;

    @JsonProperty
    private String color;

    @JsonProperty
    private double alpha;

    public DrawingStyle() {
    }

    public DrawingStyle(double pen, String color, double alpha) {
        this.pen = pen;
        this.color = color;
        this.alpha = alpha;
    }

    public double getPen() {
        return pen;
    }

    public void setPen(double pen) {
        this.pen = pen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
