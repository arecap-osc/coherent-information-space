package org.hkrdi.eden.ggm.algebraic;

import java.util.Arrays;
import java.util.List;

public class TrivalentLogic {

    private TrivalentLogicType type;

    private Vertex source;

    private Vertex sensor;

    private Vertex decider;

    public TrivalentLogic() {
    }

    public TrivalentLogic(TrivalentLogicType type, Vertex source, Vertex sensor, Vertex decider) {
        this.type = type;
        this.source = source;
        this.sensor = sensor;
        this.decider = decider;
    }

    public TrivalentLogicType getType() {
        return type;
    }

    public void setType(TrivalentLogicType type) {
        this.type = type;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getSensor() {
        return sensor;
    }

    public void setSensor(Vertex sensor) {
        this.sensor = sensor;
    }

    public Vertex getDecider() {
        return decider;
    }

    public void setDecider(Vertex decider) {
        this.decider = decider;
    }

    public List<Vertex> getVertices() {
        return Arrays.asList(new Vertex[]{getSource(), getSensor(), getDecider()});
    }

}
