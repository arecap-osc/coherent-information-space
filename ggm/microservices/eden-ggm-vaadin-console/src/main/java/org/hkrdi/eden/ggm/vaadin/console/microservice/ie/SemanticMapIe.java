package org.hkrdi.eden.ggm.vaadin.console.microservice.ie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.web.exchange.annotation.InformationExchange;

@InformationExchange
@UIScope
@JsonIgnoreProperties(ignoreUnknown = true)
public class SemanticMapIe {

    @JsonProperty
    private Long semanticMapId;

    @JsonProperty
    private Long firstMapWordId;

    @JsonProperty
    private Long secondMapWordId;

    @JsonProperty
    private Long firstMapLinkId;

    @JsonProperty
    private Long secondMapLinkId;

    public Long getSemanticMapId() {
        return semanticMapId;
    }

    public void setSemanticMapId(Long semanticMapId) {
        this.semanticMapId = semanticMapId;
    }

    public Long getFirstMapWordId() {
        return firstMapWordId;
    }

    public void setFirstMapWordId(Long firstMapWordId) {
        this.firstMapWordId = firstMapWordId;
    }

    public Long getSecondMapWordId() {
        return secondMapWordId;
    }

    public void setSecondMapWordId(Long secondMapWordId) {
        this.secondMapWordId = secondMapWordId;
    }

    public Long getFirstMapLinkId() {
        return firstMapLinkId;
    }

    public void setFirstMapLinkId(Long firstMapLinkId) {
        this.firstMapLinkId = firstMapLinkId;
    }

    public Long getSecondMapLinkId() {
        return secondMapLinkId;
    }

    public void setSecondMapLinkId(Long secondMapLinkId) {
        this.secondMapLinkId = secondMapLinkId;
    }
}
