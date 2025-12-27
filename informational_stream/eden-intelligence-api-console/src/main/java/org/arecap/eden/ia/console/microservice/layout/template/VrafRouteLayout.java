package org.arecap.eden.ia.console.microservice.layout.template;

import org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.ApplicationRouteLayout;

public abstract class VrafRouteLayout extends ApplicationRouteLayout {

    @Override
    protected String getLogoPath() {
        return "/frontend/img/Vraf_logo.png";
    }

    @Override
    protected String getActionImagePath() {
        return "/frontend/img/Vraf_Action_Logo.png";
    }
}
