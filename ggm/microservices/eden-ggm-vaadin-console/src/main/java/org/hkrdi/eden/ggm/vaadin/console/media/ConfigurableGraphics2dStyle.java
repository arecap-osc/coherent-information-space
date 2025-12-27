package org.hkrdi.eden.ggm.vaadin.console.media;

import org.beryx.awt.color.ColorFactory;

import java.awt.*;

public interface ConfigurableGraphics2dStyle extends MediaRenderStyle {

    Color getColor();

    void setColor(Color color);

    Double getOpacity();

    void setOpacity(Double opacity);

    Double getPenStroke();

    void setPenStroke(Double penStroke);

    Font getFont();

    void setFont(Font font);

    default Color getAlphaColor() {
        return ColorFactory.web(String.format("#%02x%02x%02x",
                getColor().getRed(), getColor().getGreen(), getColor().getBlue()), getOpacity());
    }
    
    default String getAlphaColorAsStr() {
        return String.format("#%02x%02x%02x",
                getColor().getRed(), getColor().getGreen(), getColor().getBlue());
    }
    

}
