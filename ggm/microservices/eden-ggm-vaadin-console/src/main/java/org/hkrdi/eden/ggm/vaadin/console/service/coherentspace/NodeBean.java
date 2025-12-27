package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;

public class NodeBean {

    private String network;

    private Long addressIndex;
    
    public NodeBean(String network, Long addressIndex) {
        this.network = network;
        this.addressIndex = addressIndex;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getAddressIndex() {
        return addressIndex;
    }

    public void setAddressIndex(Long addressIndex) {
        this.addressIndex = addressIndex;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressIndex == null) ? 0 : addressIndex.hashCode());
		result = prime * result + ((network == null) ? 0 : network.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		NodeBean other = (NodeBean) obj;
		if (addressIndex == null) {
			if (other.addressIndex != null)
				return false;
		} else if (!addressIndex.equals(other.addressIndex))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		return true;
	}
    
    
}
