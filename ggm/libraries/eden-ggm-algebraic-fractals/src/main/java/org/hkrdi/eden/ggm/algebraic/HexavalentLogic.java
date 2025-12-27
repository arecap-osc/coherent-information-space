package org.hkrdi.eden.ggm.algebraic;

import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

public class HexavalentLogic {

    private ClusterType clusterType;

    private int index;

    private int beltIndex;

    private Point center;

    private int row;

    private int column;

    private TrivalentLogic cognitiveOuter;

    private TrivalentLogic socialOuter;

    private TrivalentLogic cognitiveInner;

    private TrivalentLogic socialInner;

    private List<Connector> outerVerticesConnectors;

    private List<Connector> innerVerticesConnectors;

    public ClusterType getClusterType() {
        return clusterType;
    }

    public void setClusterType(ClusterType clusterType) {
        this.clusterType = clusterType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBeltIndex() {
        return beltIndex;
    }

    public void setBeltIndex(int beltIndex) {
        this.beltIndex = beltIndex;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public TrivalentLogic getCognitiveOuter() {
        return cognitiveOuter;
    }

    public void setCognitiveOuter(TrivalentLogic cognitiveOuter) {
        this.cognitiveOuter = cognitiveOuter;
    }

    public TrivalentLogic getSocialOuter() {
        return socialOuter;
    }

    public void setSocialOuter(TrivalentLogic socialOuter) {
        this.socialOuter = socialOuter;
    }

    public TrivalentLogic getCognitiveInner() {
        return cognitiveInner;
    }

    public void setCognitiveInner(TrivalentLogic cognitiveInner) {
        this.cognitiveInner = cognitiveInner;
    }

    public TrivalentLogic getSocialInner() {
        return socialInner;
    }

    public void setSocialInner(TrivalentLogic socialInner) {
        this.socialInner = socialInner;
    }

    public List<Connector> getOuterVerticesConnectors() {
        return outerVerticesConnectors;
    }

    public void setOuterVerticesConnectors(List<Connector> outerVerticesConnectors) {
        this.outerVerticesConnectors = outerVerticesConnectors;
    }

    public List<Connector> getInnerVerticesConnectors() {
        return innerVerticesConnectors;
    }

    public void setInnerVerticesConnectors(List<Connector> innerVerticesConnectors) {
        this.innerVerticesConnectors = innerVerticesConnectors;
    }

    public List<Vertex> getVertices() {
        List result = new ArrayList<Vertex>(12);
        result.addAll(getOuterVertices());
        result.addAll(getInnerVertices());
        return result;
    }

    //TODO move the logic of inner connectors to ConnectorBuilder interface
    public List<Vertex> getOuterVertices(){
        List result = new ArrayList<Vertex>(6);
        result.add(cognitiveOuter.getSource());
        result.add(socialOuter.getDecider());
        result.add(cognitiveOuter.getSensor());
        result.add(socialOuter.getSource());
        result.add(cognitiveOuter.getDecider());
        result.add(socialOuter.getSensor());
        return result;
    }

    public List<Vertex> getInnerVertices() {
        List result = new ArrayList<Vertex>(6);
        result.add(cognitiveInner.getSource());
        result.add(socialInner.getDecider());
        result.add(cognitiveInner.getSensor());
        result.add(socialInner.getSource());
        result.add(cognitiveInner.getDecider());
        result.add(socialInner.getSensor());
        return result;
    }

    public List<Vertex> getSocialVertices() {
        List result = new ArrayList<Vertex>(6);
        result.addAll(getSocialOuterVertices());
        result.addAll(getSocialInnerVertices());
        return result;
    }

    public List<Vertex> getCognitiveVertices() {
        List result = new ArrayList<Vertex>(6);
        result.addAll(getCognitiveOuterVertices());
        result.addAll(getCognitiveInnerVertices());
        return result;
    }

    public List<Vertex> getSocialOuterVertices() {
        List result = new ArrayList<Vertex>(3);
        result.add(socialOuter.getSource());
        result.add(socialOuter.getSensor());
        result.add(socialOuter.getDecider());
        return result;
    }

    public List<Vertex> getCognitiveOuterVertices() {
        List result = new ArrayList<Vertex>(3);
        result.add(cognitiveOuter.getSource());
        result.add(cognitiveOuter.getSensor());
        result.add(cognitiveOuter.getDecider());
        return result;
    }

    public List<Vertex> getSocialInnerVertices() {
        List result = new ArrayList<Vertex>(3);
        result.add(socialInner.getSource());
        result.add(socialInner.getSensor());
        result.add(socialInner.getDecider());
        return result;
    }

    public List<Vertex> getCognitiveInnerVertices() {
        List result = new ArrayList<Vertex>(3);
        result.add(cognitiveInner.getSource());
        result.add(cognitiveInner.getSensor());
        result.add(cognitiveInner.getDecider());
        return result;
    }

    public List<Connector> getConnectors() {
        List<Connector> verticesConnectors = new ArrayList<>(42);
        verticesConnectors.addAll(getOuterVerticesConnectors());
        verticesConnectors.addAll(getInnerVerticesConnectors());
        return verticesConnectors;
    }

}
