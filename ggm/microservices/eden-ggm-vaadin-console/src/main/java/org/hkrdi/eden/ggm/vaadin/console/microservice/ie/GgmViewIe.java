package org.hkrdi.eden.ggm.vaadin.console.microservice.ie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.web.exchange.annotation.InformationExchange;

@InformationExchange
@UIScope
@JsonIgnoreProperties(ignoreUnknown = true)
public class GgmViewIe {

    private String applicationName = "Arta de a gandi";

    private String network = "SUSTAINABLE_VERTICES::1";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
