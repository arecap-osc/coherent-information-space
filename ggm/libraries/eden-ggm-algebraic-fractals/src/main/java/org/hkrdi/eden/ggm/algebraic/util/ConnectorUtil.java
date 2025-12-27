package org.hkrdi.eden.ggm.algebraic.util;

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.springframework.data.geo.Point;

import java.util.Optional;
import java.util.stream.Collectors;

public final class ConnectorUtil {

    public static Connector getConnector(HexavalentLogic hexavalentLogic, Vertex vertex1, Vertex vertex2) {
        return hexavalentLogic.getConnectors().stream()
                .filter(connector ->(connector.getHead() == vertex1 && connector.getTail() == vertex2) ||
                        (connector.getTail() == vertex1 && connector.getHead() == vertex2))
                .collect(Collectors.toList()).get(0);
    }

    public static Connector getOuterVerticesConnector(HexavalentLogic hexavalentLogic, Vertex vertex1, Vertex vertex2) {
        return hexavalentLogic.getOuterVerticesConnectors().stream()
                .filter(connector ->(connector.getHead() == vertex1 && connector.getTail() == vertex2) ||
                        (connector.getTail() == vertex1 && connector.getHead() == vertex2))
                .collect(Collectors.toList()).get(0);
    }

    public static Connector getInnerVerticesConnector(HexavalentLogic hexavalentLogic, Vertex vertex1, Vertex vertex2) {
        return hexavalentLogic.getInnerVerticesConnectors().stream()
                .filter(connector ->(connector.getHead() == vertex1 && connector.getTail() == vertex2) ||
                        (connector.getTail() == vertex1 && connector.getHead() == vertex2))
                .collect(Collectors.toList()).get(0);
    }


    public static Optional<Point> calculateIntersectionPoint(Connector connector1, Connector connector2) {
        double a1 = connector1.getTail().getPoint().getX() == connector1.getHead().getPoint().getX() ? 1 :
                (connector1.getTail().getPoint().getY() - connector1.getHead().getPoint().getY()) /
                        (connector1.getTail().getPoint().getX() - connector1.getHead().getPoint().getX());
        double b1 = a1 == 1 ? 0 : -1;
        double c1 = a1 == 1 ? - connector1.getTail().getPoint().getX() :
                connector1.getTail().getPoint().getY() - a1 * connector1.getTail().getPoint().getX();
        double a2 = connector2.getTail().getPoint().getX() == connector2.getHead().getPoint().getX() ? 1 :
                (connector2.getTail().getPoint().getY() - connector2.getHead().getPoint().getY()) /
                        (connector2.getTail().getPoint().getX() - connector2.getHead().getPoint().getX());
        double b2 = a2 == 1 ? 0 : -1;
        double c2 = a2 == 1 ?  - connector2.getTail().getPoint().getX() :
                connector2.getTail().getPoint().getY() - a2 * connector2.getTail().getPoint().getX();
        if (a1 == a2) {
            return Optional.empty();
        }
        double a =  b1 * c2 - b2 * c1 ;
        double b = a2 * c1 - a1 * c2;
        double c = a1 * b2  - a2 * b1;
        double x = a/c ;//(b2 - b1) / (m1 - m2);
        double y = b/c ; //m1 * x + b1;
        Point point = new Point(x , y);
        return Optional.of(point);
    }


}
