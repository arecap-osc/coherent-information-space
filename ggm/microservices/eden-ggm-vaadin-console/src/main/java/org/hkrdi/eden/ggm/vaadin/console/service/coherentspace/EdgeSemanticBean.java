package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

public class EdgeSemanticBean {

    private EdgeBean edge;

    private String syntax;

    private String details;

    public EdgeSemanticBean(EdgeBean edge, String syntax, String details) {
        this.edge = edge;
        this.syntax = syntax;
        this.details = details;
    }

    public EdgeBean getEdge() {
        return edge;
    }

    public void setEdge(EdgeBean edge) {
        this.edge = edge;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
