package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

public class ClusterBean {

    private String network;

    private Long clusterIndex;

    public ClusterBean(String network, Long clusterIndex) {
        this.network = network;
        this.clusterIndex = clusterIndex;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(Long clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clusterIndex == null) ? 0 : clusterIndex.hashCode());
		result = prime * result + ((network == null) ? 0 : network.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClusterBean other = (ClusterBean) obj;
		if (clusterIndex == null) {
			if (other.clusterIndex != null)
				return false;
		} else if (!clusterIndex.equals(other.clusterIndex))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		return true;
	}
    
    
}
