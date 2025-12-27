package org.arecap.eden.ia.console.microservice.websites;

import com.vaadin.flow.router.Route;

@Route(value = "about")
public class AboutPage extends NoianLogoLandingPage {


    @Override
    protected void buildLayout() {
    }

    @Override
    protected void selectRoute() {
        selectRouteTab(getAboutRoute());
    }

}
