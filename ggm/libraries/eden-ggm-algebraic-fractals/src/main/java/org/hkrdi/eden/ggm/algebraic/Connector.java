package org.hkrdi.eden.ggm.algebraic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Connector implements Serializable {

    @JsonProperty
    private Vertex head;

    @JsonProperty
    private Vertex tail;

    public Connector() {
    }

    public Connector(Vertex head, Vertex tail) {
        this.head = head;
        this.tail = tail;
    }

    public Vertex getHead() {
        return head;
    }

    public void setHead(Vertex head) {
        this.head = head;
    }

    public Vertex getTail() {
        return tail;
    }

    public void setTail(Vertex tail) {
        this.tail = tail;
    }

}
