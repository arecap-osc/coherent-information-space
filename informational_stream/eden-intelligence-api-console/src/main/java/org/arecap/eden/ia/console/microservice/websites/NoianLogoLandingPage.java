package org.arecap.eden.ia.console.microservice.websites;

import javax.annotation.PostConstruct;

public abstract class NoianLogoLandingPage extends NoianLandingPage {

    @PostConstruct
    protected void setupBackgroundImage() {
        setBackgroundImage("linear-gradient(rgba(222, 83, 31, 0.83), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1))");
    }

    @Override
    protected String getRouteLogoPath() {
        return "frontend/img/Noian_Coop_Logo.png";
    }

}
