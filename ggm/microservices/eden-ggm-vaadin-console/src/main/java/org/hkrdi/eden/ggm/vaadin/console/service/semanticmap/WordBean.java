package org.hkrdi.eden.ggm.vaadin.console.service.semanticmap;

public class WordBean {

    private String network;

    private Long x;

    private Long y;

    public WordBean() {
    }

    public WordBean(String network, Long x, Long y) {
        this.network = network;
        this.x = x;
        this.y = y;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WordBean wordBean = (WordBean) o;

        if (network != null ? !network.equals(wordBean.network) : wordBean.network != null)
            return false;
        if (x != null ? !x.equals(wordBean.x) : wordBean.x != null)
            return false;
        return y != null ? y.equals(wordBean.y) : wordBean.y == null;
    }

    @Override
    public int hashCode() {
        int result = network != null ? network.hashCode() : 0;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
