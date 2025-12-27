package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;


import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.springframework.data.geo.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeometricRoute {

	public static List<HexavalentLogic> draw(HexavalentLogic start, HexavalentLogic end, List<HexavalentLogic> hexavalentLogic) {
		return hexavalentLogic.stream().filter(
				hl->intersectsHexagon(start.getCenter(), end.getCenter(), hl)
			).collect(Collectors.toList());
	}
	
	public static List<HexavalentLogic> draw(Point start, Point end, List<HexavalentLogic> hexavalentLogic) {
		List<HexavalentLogic> result = new ArrayList<HexavalentLogic>();
		result.addAll(hexavalentLogic.stream().filter(
				hl->intersectsHexagon(start, end, hl)
			).
//				sorted(
//				(hl, hl2)->(new Integer(hl2.getRow()).compareTo(new Integer(hl.getRow())))
//					).
				collect(Collectors.toList()));
		Map<Double, HexavalentLogic> map = result.stream().collect(Collectors.toMap(hl->segmentLength(start, hl.getCenter()), hl->hl));
		Double[] distance = map.keySet().toArray(new Double[] {});
		Arrays.sort(distance);
		return Arrays.asList(distance).stream().map(dist->map.get(dist)).collect(Collectors.toList());
	}
	
	public static double segmentLength(Point start, Point end) {
		return Math.sqrt( ( ( end.getX() - start.getX() ) * ( end.getX() 
				- start.getX() ) ) + ( ( end.getY() - start.getY() ) * ( end.getY() - start.getY() ) ) );
	}
	
	private static boolean intersectsHexagon(Point startPoint, Point endPoint, HexavalentLogic hl) {
		return hl.getOuterVerticesConnectors().stream().filter(
				hlLine-> calculateIntersectionPoint(
						startPoint, endPoint, hlLine.getHead().getPoint(), hlLine.getTail().getPoint()
					)
			).findAny().isPresent();
	}
	
	private static boolean calculateIntersectionPoint(Point startPoint, Point endPoint, 
			Point hlStartPoint, Point hlEndPoint) {
		
		return Line2D.Double.linesIntersect(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 
				hlStartPoint.getX(), hlStartPoint.getY(), hlEndPoint.getX(), hlEndPoint.getY()) ||
			Line2D.Double.ptSegDist(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 
						hlStartPoint.getX(), hlStartPoint.getY()) == 0 ||
			Line2D.Double.ptSegDist(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 
					hlEndPoint.getX(), hlEndPoint.getY()) == 0;
	}
//	private static Optional<Point> calculateIntersectionPoint(Point startPoint, Point endPoint, 
//			Point hlStartPoint, Point hlEndPoint) {
//        double a1 = endPoint.getX() == startPoint.getX() ? 1 :
//                (endPoint.getY() - startPoint.getY()) /
//                        (endPoint.getX() - startPoint.getX());
//        double b1 = a1 == 1 ? 0 : -1;
//        double c1 = a1 == 1 ? - endPoint.getX() :
//        	endPoint.getY() - a1 * endPoint.getX();
//        double a2 = hlEndPoint.getX() == hlStartPoint.getX() ? 1 :
//                (hlEndPoint.getY() - hlStartPoint.getY()) /
//                        (hlEndPoint.getX() - hlStartPoint.getX());
//        double b2 = a2 == 1 ? 0 : -1;
//        double c2 = a2 == 1 ?  - hlEndPoint.getX() :
//        	hlEndPoint.getY() - a2 * hlEndPoint.getX();
//        if (a1 == a2) {
//            return Optional.empty();
//        }
//        double a =  b1 * c2 - b2 * c1 ;
//        double b = a2 * c1 - a1 * c2;
//        double c = a1 * b2  - a2 * b1;
//        double x = a/c ;//(b2 - b1) / (m1 - m2);
//        double y = b/c ; //m1 * x + b1;
//        Point point = new Point(x , y);
//        return Optional.of(point);
//    }
}
