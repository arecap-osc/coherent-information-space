package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.web.exchange.annotation.InformationExchange;

@InformationExchange
@UIScope
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoadRouteIe {
	 @JsonProperty
	 private Long applicationId;
	 
	 @JsonProperty
	 private String network;
}
