package org.arecap.eden.ia.console.microservice.layout;

import org.arecap.eden.ia.console.microservice.layout.mvp.InformationalStreamView;
import org.arecap.eden.ia.console.microservice.layout.template.ConsoleRouteLayout;
import org.arecap.eden.ia.console.microservice.layout.template.TopBarView;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class InformationalStreamConsoleRoute extends ConsoleRouteLayout {

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private InformationalStreamView informationalStreamView;

    @Override
    protected void buildRouteLayout() {
        topBarView.add(informationalStreamView);
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/Dahlia_Eden.jpg";
    }
}
