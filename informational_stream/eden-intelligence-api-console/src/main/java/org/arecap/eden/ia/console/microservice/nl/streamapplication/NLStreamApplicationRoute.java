package org.arecap.eden.ia.console.microservice.nl.streamapplication;


import com.vaadin.flow.router.Route;
import org.arecap.eden.ia.console.microservice.nl.InformationalStreamRouteLayout;

@Route(value = "nl/stream-application")
public class NLStreamApplicationRoute extends InformationalStreamRouteLayout {

    @Override
    protected void buildLayout() {
    }

    @Override
    protected String getI18nStoryTitle() {
        return "route.nlstreamapplicationroute";
    }

    @Override
    protected void selectRoute() {
        selectRouteTab(getStreamApplicationRoute());
    }

}
