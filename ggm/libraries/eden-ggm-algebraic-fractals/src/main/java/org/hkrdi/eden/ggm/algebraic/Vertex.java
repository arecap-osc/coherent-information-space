package org.hkrdi.eden.ggm.algebraic;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.geo.Point;

import java.io.Serializable;

public class Vertex implements Serializable {

    @JsonProperty
    private int index;

    @JsonProperty
    private VertexType type;

    @JsonProperty
    private Point point;

    public Vertex() {
    }

    public Vertex(int index, VertexType type, Point point) {
        this.index = index;
        this.type = type;
        this.point = point;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public VertexType getType() {
        return type;
    }

    public void setType(VertexType type) {
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}
