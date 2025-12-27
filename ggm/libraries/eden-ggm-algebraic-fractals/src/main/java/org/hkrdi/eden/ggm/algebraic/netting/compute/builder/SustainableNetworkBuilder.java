package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.*;
import org.hkrdi.eden.ggm.algebraic.util.ConnectorUtil;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class SustainableNetworkBuilder extends ConfigurableNetworkBuilder {


    protected TrivalentLogic constructCognitiveOuter(Point center){
        return new TrivalentLogic(TrivalentLogicType.COGNITIVE_OUTER,
                constructCognitiveOuterVertex(VertexType.SOURCE, new Point(center.getX(),
                        center.getY() - getHeight() / 2)),
                constructCognitiveOuterVertex(VertexType.SENSOR, new Point(center.getX() + getWidth() / 2,
                        center.getY() + getHeight() / 4)),
                constructCognitiveOuterVertex(VertexType.DECIDER, new Point(center.getX() - getWidth() / 2,
                        center.getY() + getHeight() / 4)));
    }

    @Override
    protected TrivalentLogic constructSocialOuter(Point center) {
        return new TrivalentLogic(TrivalentLogicType.SOCIAL_OUTER,
                constructSocialOuterVertex(VertexType.SOURCE, new Point(center.getX(),
                        center.getY() + getHeight() / 2)),
                constructSocialOuterVertex(VertexType.SENSOR, new Point(center.getX() - getWidth() / 2,
                        center.getY() - getHeight() / 4)),
                constructSocialOuterVertex(VertexType.DECIDER, new Point(center.getX() + getWidth() / 2,
                        center.getY() - getHeight() / 4)));
    }

    @Override
    protected TrivalentLogic constructCognitiveInner(HexavalentLogic hexavalentLogic, Point center) {
        return new TrivalentLogic(TrivalentLogicType.COGNITIVE_INNER,
                constructCognitiveInnerVertex(VertexType.SOURCE,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getCognitiveOuter().getDecider()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getSensor()))
                                .get()),
                constructCognitiveInnerVertex(VertexType.SENSOR,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getCognitiveOuter().getSensor()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getDecider(), hexavalentLogic.getSocialOuter().getSensor()))
                                .get()),
                constructCognitiveInnerVertex(VertexType.DECIDER,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSensor(), hexavalentLogic.getCognitiveOuter().getDecider()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getDecider()))
                                .get()));
    }

    @Override
    protected TrivalentLogic constructSocialInner(HexavalentLogic hexavalentLogic, Point center) {
        return new TrivalentLogic(TrivalentLogicType.SOCIAL_INNER,
                constructSocialInnerVertex(VertexType.SOURCE,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getCognitiveOuter().getSensor()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getDecider()))
                                .get()),
                constructSocialInnerVertex(VertexType.SENSOR,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSensor(), hexavalentLogic.getCognitiveOuter().getDecider()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getSensor()))
                                .get()),
                constructSocialInnerVertex(VertexType.DECIDER,
                        ConnectorUtil.calculateIntersectionPoint(
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getCognitiveOuter().getDecider()),
                                ConnectorUtil.getOuterVerticesConnector(hexavalentLogic,
                                        hexavalentLogic.getSocialOuter().getDecider(), hexavalentLogic.getSocialOuter().getSensor()))
                                .get()));
    }

    @Override
    public List<Connector> getPlaneConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> links = new ArrayList<>();
        links.add(new Connector(hexavalentLogic.getSocialOuter().getSensor(), hexavalentLogic.getCognitiveOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getSensor(), hexavalentLogic.getSocialOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getDecider(), hexavalentLogic.getCognitiveOuter().getSensor()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getDecider(), hexavalentLogic.getCognitiveOuter().getSensor()));
        return links;
    }

    @Override
    public List<Connector> getMainDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> links = new ArrayList<>();
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getSensor(), hexavalentLogic.getCognitiveOuter().getSource()));
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getSensor(), hexavalentLogic.getSocialOuter().getSource()));
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getSocialOuter().getSensor()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getSensor()));
        return links;
    }

    @Override
    public List<Connector> getOddSecondaryDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> links = new ArrayList<>();
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getCognitiveOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getDecider(), hexavalentLogic.getSocialOuter().getSource()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getSocialOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getDecider(), hexavalentLogic.getCognitiveOuter().getSource()));
        return links;
    }

    @Override
    public List<Connector> getEvenSecondaryDiagonalsConnectors(HexavalentLogic hexavalentLogic) {
        List<Connector> links = new ArrayList<>();
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getSource(), hexavalentLogic.getSocialOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getDecider(), hexavalentLogic.getSocialOuter().getSource()));
        links.add(new Connector(hexavalentLogic.getSocialOuter().getSource(), hexavalentLogic.getCognitiveOuter().getDecider()));
        links.add(new Connector(hexavalentLogic.getCognitiveOuter().getDecider(), hexavalentLogic.getCognitiveOuter().getSource()));
        return links;
    }

    @Override
    public boolean isEven() {
        return getRow() >= 0 ?
                ((Math.abs(getRow()) % 4 == 0 || Math.abs(getRow()) % 4 == 1) ?
                        (getDeep() % 2 == 0 ? getColumn() % 2 == 0 : getColumn() % 2 != 0) :
                        (getDeep() % 2 == 0 ? getColumn() % 2 != 0 : getColumn() % 2 == 0) ) :
                ((Math.abs(getRow()) % 4 == 0 || Math.abs(getRow()) % 4 == 3) ?
                        (getDeep() % 2 == 0 ? getColumn() % 2 == 0 : getColumn() % 2 != 0) :
                        (getDeep() % 2 == 0 ? getColumn() % 2 != 0 : getColumn() % 2 == 0) );
    }
}
