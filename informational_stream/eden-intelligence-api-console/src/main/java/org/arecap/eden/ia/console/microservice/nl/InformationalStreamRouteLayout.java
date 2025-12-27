package org.arecap.eden.ia.console.microservice.nl;

import org.arecap.eden.ia.console.microservice.layout.mvp.InformationalStreamView;
import org.arecap.eden.ia.console.microservice.layout.template.VrafEdenIaRouteLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class InformationalStreamRouteLayout extends VrafEdenIaRouteLayout {

    @Autowired
    private InformationalStreamView informationalStreamView;


    @PostConstruct
    protected void setupInformationalStreamView() {
        addMenuBarView(informationalStreamView);
    }


}
