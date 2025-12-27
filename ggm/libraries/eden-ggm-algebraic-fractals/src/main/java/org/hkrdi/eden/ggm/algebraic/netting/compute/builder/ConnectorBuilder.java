package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.hkrdi.eden.ggm.algebraic.util.ConnectorUtil;
import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface ConnectorBuilder {



    default List<Connector> getOuterVerticesConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> links = new ArrayList<>();
        links.addAll(getSecondaryDiagonalsConnectors(hexavalentLogic));
        links.addAll(getMainDiagonalsConnectors(hexavalentLogic));
        links.addAll(getPlaneConnectors(hexavalentLogic));
        return links;
    }

    default List<Connector> getInnerVerticesConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> innerVerticesConnectors = new ArrayList<>(30);
        for(int i = 0; i < hexavalentLogic.getOuterVertices().size(); i++) {
            Connector outerVerticesConnector = ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                    hexavalentLogic.getOuterVertices().get(i),
                    hexavalentLogic.getOuterVertices().get((i + 2 < hexavalentLogic.getOuterVertices().size() ?
                            i + 2 :(i + 1 < hexavalentLogic.getOuterVertices().size() ? 0 : 1 ))));
            List<Vertex> collinearInnerVertices = hexavalentLogic.getInnerVertices()
                    .stream().filter(vertex -> GeometryUtil.isCollinear(outerVerticesConnector.getHead().getPoint(),
                            vertex.getPoint(), outerVerticesConnector.getTail().getPoint()))
                    .collect(Collectors.toList());
            double distance = 0;
            Vertex tmp = null;
            for(int j = 0; j < collinearInnerVertices.size(); j++) {
                if(tmp == null) {
                    tmp = collinearInnerVertices.get(j);
                    distance = Math.hypot(Math.abs(outerVerticesConnector.getHead().getPoint().getY() - collinearInnerVertices.get(j).getPoint().getY()) ,
                            Math.abs(outerVerticesConnector.getHead().getPoint().getX() - collinearInnerVertices.get(j).getPoint().getX()));
                    innerVerticesConnectors.add(new Connector(outerVerticesConnector.getHead(), tmp));
                    innerVerticesConnectors.add(new Connector(tmp, outerVerticesConnector.getTail()));
                    continue;
                }
                double d = Math.hypot(Math.abs(outerVerticesConnector.getHead().getPoint().getY() - collinearInnerVertices.get(j).getPoint().getY()) ,
                        Math.abs(outerVerticesConnector.getHead().getPoint().getX() - collinearInnerVertices.get(j).getPoint().getX()));
                innerVerticesConnectors.add(new Connector(outerVerticesConnector.getHead(), collinearInnerVertices.get(j)));
                innerVerticesConnectors.add(new Connector(collinearInnerVertices.get(j), outerVerticesConnector.getTail()));
                if (d < distance) {
                    innerVerticesConnectors.add(new Connector(collinearInnerVertices.get(j), tmp));
                } else {
                    innerVerticesConnectors.add(new Connector(tmp, collinearInnerVertices.get(j)));
                }
            }
        }
        Assert.isTrue(innerVerticesConnectors.size() == 30, "Inner Vertices Connectors size :\t"+ innerVerticesConnectors.size()+ " !  ");
        return innerVerticesConnectors;
    }

    default List<Connector> getPlaneConnectors(HexavalentLogic hexavalentLogic) {
        return isEven() ? getEvenPlaneConnectors(hexavalentLogic) : getOddPlaneConnectors(hexavalentLogic);
    }

    default List<Connector> getOddPlaneConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    default List<Connector> getEvenPlaneConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    default List<Connector> getMainDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return isEven() ? getEvenMainDiagonalsConnectors(hexavalentLogic) : getOddMainDiagonalsConnectors(hexavalentLogic);
    }

    default List<Connector> getOddMainDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    default List<Connector> getEvenMainDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    default List<Connector> getSecondaryDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return isEven() ? getEvenSecondaryDiagonalsConnectors(hexavalentLogic) : getOddSecondaryDiagonalsConnectors(hexavalentLogic);
    }

    default List<Connector> getOddSecondaryDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    default List<Connector> getEvenSecondaryDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        return new ArrayList<>();
    }

    boolean isEven();

}
