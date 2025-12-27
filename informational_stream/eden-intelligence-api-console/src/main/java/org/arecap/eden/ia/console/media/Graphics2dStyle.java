package org.arecap.eden.ia.console.media;

import java.awt.*;

public class Graphics2dStyle implements ConfigurableGraphics2dStyle {

    private Color color;
    private Double opacity;
    private Double penStroke;
    private Font font;

    public Graphics2dStyle(Color color, Double opacity, Double penStroke, Font font) {
        super();
        this.color = color;
        this.opacity = opacity;
        this.penStroke = penStroke;
        this.font = font;
    }

    public Graphics2dStyle(Color color, Double penStroke) {
        this(color, 1.0d, penStroke, null);
    }
    
    public Graphics2dStyle(Color color, Double opacity, Double penStroke) {
    	 this(color, opacity, penStroke, null);
    }

    public Graphics2dStyle() {

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Double getOpacity() {
        return opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    public Double getPenStroke() {
        return penStroke;
    }

    public void setPenStroke(Double penStroke) {
        this.penStroke = penStroke;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

}
