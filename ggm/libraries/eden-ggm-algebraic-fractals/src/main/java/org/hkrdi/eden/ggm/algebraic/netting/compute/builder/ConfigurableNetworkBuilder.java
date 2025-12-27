package org.hkrdi.eden.ggm.algebraic.netting.compute.builder;

import org.hkrdi.eden.ggm.algebraic.*;
import org.hkrdi.eden.ggm.algebraic.netting.Network;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigurableNetworkBuilder implements NetworkBuilder, Network {

    private Point spaceCenter;

    private Point center;

    private double spaceDistance;

    private int deep;

    private int row;

    private int column;

//    private Point partitionCenter;
//
//    private Point partitionTopLeft;

    @Override
    public void setGrid(int col, int row) {
        this.row = row;
        this.column = col;
    }

    private List<HexavalentLogic> hexavalentNetting = new ArrayList<HexavalentLogic>();

    @Override
    public void buildHexavalentLogic(Point center) {
        HexavalentLogic hexavalentLogic = new HexavalentLogic();
        hexavalentLogic.setClusterType(isEven() ? ClusterType.EVEN : ClusterType.ODD);
        hexavalentLogic.setCenter(center);
        hexavalentLogic.setIndex(getIndex());
        hexavalentLogic.setBeltIndex(getBeltIndex(hexavalentLogic.getIndex()));
        hexavalentLogic.setRow(getRow());
        hexavalentLogic.setColumn(getColumn());
        hexavalentLogic.setCognitiveOuter(constructCognitiveOuter(center));
        hexavalentLogic.setSocialOuter(constructSocialOuter(center));
        hexavalentLogic.setOuterVerticesConnectors(getOuterVerticesConnectors(hexavalentLogic));
        hexavalentLogic.setCognitiveInner(constructCognitiveInner(hexavalentLogic, center));
        hexavalentLogic.setSocialInner(constructSocialInner(hexavalentLogic, center));
        hexavalentLogic.setInnerVerticesConnectors(getInnerVerticesConnectors(hexavalentLogic));
        getHexavalentNetting().add(hexavalentLogic);
    }

    protected abstract TrivalentLogic constructCognitiveOuter(Point center);

    protected abstract TrivalentLogic constructSocialOuter(Point center);

    protected abstract TrivalentLogic constructCognitiveInner(HexavalentLogic hexavalentLogic, Point center);

    protected abstract TrivalentLogic constructSocialInner(HexavalentLogic hexavalentLogic, Point center);

    protected Vertex constructCognitiveOuterVertex(VertexType vertexType, Point point) {
        switch (vertexType) {
            case SOURCE:
                return new Vertex(getCognitiveOuterSourceIndex(getIndex()), vertexType, point);
            case SENSOR:
                return new Vertex(getCognitiveOuterSensorIndex(getIndex()), vertexType, point);
            case DECIDER:
                return new Vertex(getCognitiveOuterDeciderIndex(getIndex()), vertexType, point);
            default:
                throw new IllegalArgumentException();
        }
    }

    protected Vertex constructSocialOuterVertex(VertexType vertexType, Point point) {
        switch (vertexType) {
            case SOURCE:
                return new Vertex(getSocialOuterSourceIndex(getIndex()), vertexType, point);
            case SENSOR:
                return new Vertex(getSocialOuterSensorIndex(getIndex()), vertexType, point);
            case DECIDER:
                return new Vertex(getSocialOuterDeciderIndex(getIndex()), vertexType, point);
            default:
                throw new IllegalArgumentException();
        }
    }

    protected Vertex constructCognitiveInnerVertex(VertexType vertexType, Point point) {
        switch (vertexType) {
            case SOURCE:
                return new Vertex(getLastIndex(getIndex()) - 5, vertexType, point);
            case SENSOR:
                return new Vertex(getLastIndex(getIndex()) - 3, vertexType, point);
            case DECIDER:
                return new Vertex(getLastIndex(getIndex()) - 1, vertexType, point);
            default:
                throw new IllegalArgumentException();
        }
    }

    protected Vertex constructSocialInnerVertex(VertexType vertexType, Point point) {
        switch (vertexType) {
            case SOURCE:
                return new Vertex(getLastIndex(getIndex()) - 2, vertexType, point);
            case SENSOR:
                return new Vertex(getLastIndex(getIndex()), vertexType, point);
            case DECIDER:
                return new Vertex(getLastIndex(getIndex()) - 4, vertexType, point);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Point getSpaceCenter() {
        return spaceCenter;
    }

    public void setSpaceCenter(Point spaceCenter) {
        this.spaceCenter = spaceCenter;
    }

    @Override
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public double getSpaceDistance() {
        return spaceDistance;
    }

    public void setSpaceDistance(double spaceDistance) {
        this.spaceDistance = spaceDistance;
    }

    @Override
    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

//    @Override
//    public Point getPartitionCenter() {
//        return partitionCenter;
//    }
//
//    public void setPartitionCenter(Point partitionCenter) {
//        this.partitionCenter = partitionCenter;
//    }
//
//    @Override
//    public Point getPartitionTopLeft() {
//        return partitionTopLeft;
//    }
//
//    public void setPartitionTopLeft(Point partitionTopLeft) {
//        this.partitionTopLeft = partitionTopLeft;
//    }

    @Override
    public List<HexavalentLogic> getHexavalentNetting() {
        return hexavalentNetting;
    }

}
