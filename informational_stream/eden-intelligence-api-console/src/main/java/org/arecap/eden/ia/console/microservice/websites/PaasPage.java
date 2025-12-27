package org.arecap.eden.ia.console.microservice.websites;

import com.vaadin.flow.router.Route;

@Route(value = "paas")
public class PaasPage extends NoianLogoLandingPage {

    @Override
    protected void selectRoute() {
        selectRouteTab(getPaasRoute());
    }

    @Override
    protected void buildLayout() {

    }
}
