package org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class MandelbrotSetUtil {


    private static Double escapeRadius = 10D;

    private static Double logBase = 1 / Math.log(2);

    private static Double logHalfBase = Math.log(0.5) * logBase;

    private static Color interiorColor = new Color(0,0,0, 255);


    public static Double getSmoothColor(Integer step, Double x, Double y) {
        return 5 + step - logHalfBase - Math.log(Math.log(x+y)) * logBase;
    }

    public static Color getColor(Integer steps, Integer step, Double x, Double y) {
        if(steps == step)
            return interiorColor;
        return Color.getHSBColor(360 * getSmoothColor(step, x, y).floatValue() / steps, 1, 1);
    }

    public static Color add(Color a, Color b) {
        return new Color(a.getRed() + b.getRed(), a.getGreen() + b.getGreen(), a.getBlue() + b.getBlue(), a.getAlpha() + b.getAlpha());
    }

    public static Color divide(Color a, Color b) {
        return new Color(a.getRed() / b.getRed(), a.getGreen() / b.getGreen(), a.getBlue() / b.getBlue(), a.getAlpha() / b.getAlpha());
    }

    public static ComplexPlane adjustAspectRatio(Double x, Double y, Double width, Double height) {

        return new ComplexPlane(width, height);
    }

    public static Map<Integer, ComplexPlane> iterateEquation(Double cx, Double cy, Double escapeRadius, Integer steps) {
        Map<Integer, ComplexPlane> result = new HashMap<>();
        int step = 0;
        Double zx = 0d;
        Double zy = 0d;
        Double x = 0d;
        Double y = 0d;
        for(;step < steps && new Double(x + y).compareTo(escapeRadius) <= 0; ++step) {
            zy = 2 * zx * zy + cy;
            zx = x - y + cx;
            x = zx * zx;
            y = zy * zy;
        }
        /*
        * Four more iterations to decrease error term;
        * see http://linas.org/art-gallery/escape/escape.html
        */
        for(int e = 0; e < 4; e++) {
            zy = 2 * zx * zy + cy;
            zx = x - y + cx;
            x = zx * zx;
            y = zy * zy;
        }
        result.put(step, new ComplexPlane(x, y));
        return result;
    }

}
