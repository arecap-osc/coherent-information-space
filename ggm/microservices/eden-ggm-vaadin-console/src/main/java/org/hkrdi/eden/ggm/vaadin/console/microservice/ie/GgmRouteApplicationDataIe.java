package org.hkrdi.eden.ggm.vaadin.console.microservice.ie;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.web.exchange.annotation.InformationExchange;

@InformationExchange
@UIScope
@JsonIgnoreProperties(ignoreUnknown = true)
public class GgmRouteApplicationDataIe {

    @JsonProperty
    private Long applicationId;

    @JsonProperty
    private Long firstNodeId;

    @Deprecated
    private Long firstNodeDataMapId;

    @JsonProperty
    private Long secondNodeId;

    @JsonProperty
    private Long syntaxId;

    @JsonProperty
    private String network;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getFirstNodeId() {
        return firstNodeId;
    }

    public void setFirstNodeId(Long firstNodeId) {
        this.firstNodeId = firstNodeId;
    }

    public Long getSecondNodeId() {
        return secondNodeId;
    }

    public void setSecondNodeId(Long secondNodeId) {
        this.secondNodeId = secondNodeId;
    }

    public Long getSyntaxId() {
        return syntaxId;
    }

    public void setSyntaxId(Long syntaxId) {
        this.syntaxId = syntaxId;
    }

    @Deprecated
	public Long getFirstNodeDataMapId() {
		return firstNodeDataMapId;
	}

    @Deprecated
	public void setFirstNodeDataMapId(Long firstNodeDataMapId) {
		this.firstNodeDataMapId = firstNodeDataMapId;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}
}

