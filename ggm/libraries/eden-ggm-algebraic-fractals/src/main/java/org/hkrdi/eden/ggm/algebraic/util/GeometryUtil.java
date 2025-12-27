package org.hkrdi.eden.ggm.algebraic.util;

import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.MathContext;

public final class GeometryUtil {

    public static boolean isOnSegment(Point p1, Point p2, Point p3) {
        p1 = new Point( round(p1.getX(),2), round(p1.getY(),2));
        p2 = new Point( round(p2.getX(), 2), round(p2.getY(), 2));
        p3 = new Point( round(p3.getX(), 2), round(p3.getY(), 2));
        double sideA = getDistance(p1, p2);
        double sideB = getDistance(p2, p3);
        double sideC = getDistance(p3, p1);
        double s = round( (sideA + sideB + sideC) / 2, 2);
        double sa = (s - sideA);
        double sb = (s - sideC);
        double sc = (s - sideB);
        Double area = Math.sqrt(s*sa*sb*sc);
        return (area.compareTo(0d) == 0 || area.equals(Double.NaN)) && sideA >= sideB && sideA >= sideC;
    }

    public static Double getDistance(Point fromPoint, Point toPoint) {
        return round(Math.round(Math.hypot(Math.abs(fromPoint.getY() - toPoint.getY()),
                Math.abs(fromPoint.getX() - toPoint.getX()))), 2);
    }
    
    public static boolean isOnSegmentBigDecimal(Point p1, Point p2, Point p3, double radius) {
        p1 = new Point( round(p1.getX(),2), round(p1.getY(),2));
        p2 = new Point( round(p2.getX(), 2), round(p2.getY(), 2));
        p3 = new Point( round(p3.getX(), 2), round(p3.getY(), 2));
        BigDecimal sideA = getDistanceBigDecimal(p1, p2);
        BigDecimal sideB = getDistanceBigDecimal(p2, p3);
        BigDecimal sideC = getDistanceBigDecimal(p3, p1);
        BigDecimal s = (sideA.add(sideB).add(sideC)).divide(new BigDecimal(2), MathContext.DECIMAL128);
        BigDecimal sa = s.subtract(sideA);
        BigDecimal sb = s.subtract(sideC);
        BigDecimal sc = s.subtract(sideB);
        BigDecimal area = BigDecimalUtil.sqrt((s.multiply(sa, MathContext.DECIMAL128).multiply(sb, MathContext.DECIMAL128).multiply(sc, MathContext.DECIMAL128)).abs());
        return (area.compareTo(BigDecimal.ZERO) <= radius || area.equals(Double.NaN)) && sideA.compareTo(sideB) >= 0 && sideA.compareTo(sideC) >= 0;
    }

    public static BigDecimal getDistanceBigDecimal(Point fromPoint, Point toPoint) {
    	BigDecimal r1 = new BigDecimal(Math.abs(fromPoint.getY() - toPoint.getY()),MathContext.DECIMAL128);
    	r1 = r1.pow(2);
    	BigDecimal r2 = new BigDecimal(Math.abs(fromPoint.getX() - toPoint.getX()),MathContext.DECIMAL128);
    	r2 = r2.pow(2);
    	return BigDecimalUtil.sqrt((r1.add(r2)));   	
    }
    
    public static boolean isCollinear(Point p1, Point p2, Point p3) {
        p1 = new Point( round(p1.getX(),2), round(p1.getY(),2));
        p2 = new Point( round(p2.getX(), 2), round(p2.getY(), 2));
        p3 = new Point( round(p3.getX(), 2), round(p3.getY(), 2));
        double sideA = getDistance(p1, p2);
        double sideB = getDistance(p2, p3);
        double sideC = getDistance(p3, p1);
        double s = round( (sideA + sideB + sideC) / 2, 2);
        double sa = (s - sideA);
        double sb = (s - sideC);
        double sc = (s - sideB);
        Double area = Math.sqrt(s*sa*sb*sc);
        return  (area.compareTo(0d) <= 0 || area.equals(Double.NaN));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Point getPerpendicularPoint(Point p1, Point p2, Point p3) {
        // first convert line to normalized unit vector
        double dx = round(p2.getX() - p1.getX(), 2);
        double dy = round(p2.getY() - p1.getY(), 2);
        double mag = Math.sqrt(dx*dx + dy*dy);
        dx /= mag; //= GeometryUtil.round(dx / mag , 2);
        dy /= mag; //= GeometryUtil.round(dy / mag, 2);

        // translate the point and get the dot product
        double lambda = round((dx * (p3.getX() - p1.getX())) + (dy * (p3.getY() - p1.getY())), 2);

        double x4 = round((dx * lambda) + p1.getX(),  2);
        double y4 = round((dy * lambda) + p1.getY(), 2);

        return new Point(x4, y4);
        ///new Point(p3.getX() - k * (p2.getY() - p1.getY()) ,p3.getY() - k * (p2.getX() - p1.getX()));
    }

}
