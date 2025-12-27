package org.arecap.eden.ia.console.media.util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public final class Graphics2dUtils {

    public static Graphics2D constructGraphics2D(BufferedImage bufferedImage, int width, int height) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setBackground(new Color(0, 0, 0, 0));
        graphics2D.clearRect(0, 0, width, height);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2D;
    }

    public static void drawPoint(Graphics2D graphics2D, Double x, Double y, Double penStroke, double resolution) {
        double radius = resolution * penStroke;
        graphics2D.fill(new Ellipse2D.Double(x - radius, y - radius, 2.0 * radius, 2.0 * radius));
    }

    public static void drawLine(Graphics2D graphics2D, Double fromX, Double fromY, Double toX, Double toY) {
        graphics2D.draw(new Line2D.Double(fromX, fromY, toX, toY));
    }

    public static void drawStringInCenter(Graphics2D graphics2D, String s, Double x, Double y) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        graphics2D.drawString(s, x.floatValue() - fontMetrics.stringWidth(s) / 2,
                y.floatValue() - fontMetrics.getHeight() / 2 + fontMetrics.getAscent());
    }

    public static void drawString(Graphics2D graphics2D, String s, Double x, Double y) {
        graphics2D.drawString(s, x.floatValue(), y.floatValue() );
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
