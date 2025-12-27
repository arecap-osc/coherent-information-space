package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

public class EdgeBean {

    private NodeBean fromNode;

    private NodeBean toNode;

    public EdgeBean(NodeBean fromNode, NodeBean toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public NodeBean getFromNode() {
        return fromNode;
    }

    public void setFromNode(NodeBean fromNode) {
        this.fromNode = fromNode;
    }

    public NodeBean getToNode() {
        return toNode;
    }

    public void setToNode(NodeBean toNode) {
        this.toNode = toNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EdgeBean edgeBean = (EdgeBean) o;

        if (fromNode != null ? !fromNode.equals(edgeBean.fromNode) : edgeBean.fromNode != null)
            return false;
        return toNode != null ? toNode.equals(edgeBean.toNode) : edgeBean.toNode == null;
    }

    @Override
    public int hashCode() {
        int result = fromNode != null ? fromNode.hashCode() : 0;
        result = 31 * result + (toNode != null ? toNode.hashCode() : 0);
        return result;
    }
}
