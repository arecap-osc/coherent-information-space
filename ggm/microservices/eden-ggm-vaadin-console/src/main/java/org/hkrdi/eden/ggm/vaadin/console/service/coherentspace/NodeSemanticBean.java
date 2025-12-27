package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

public class NodeSemanticBean {

    private NodeBean node;

    private String note;

    private String details;

    public NodeSemanticBean(NodeBean node, String note, String details) {
        this.node = node;
        this.note = note;
        this.details = details;
    }

    public NodeBean getNode() {
        return node;
    }

    public void setNode(NodeBean node) {
        this.node = node;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
