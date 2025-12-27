package org.arecap.eden.ia.console.microservice.layout.template;

import com.vaadin.flow.component.tabs.Tab;
import org.arecap.eden.ia.console.microservice.graph.GraphRoute;
import org.arecap.eden.ia.console.microservice.nl.feature.NlFeatureRoute;
import org.arecap.eden.ia.console.microservice.nl.signal.NlSignalRoute;
import org.arecap.eden.ia.console.microservice.nl.streamapplication.NLStreamApplicationRoute;

import javax.annotation.PostConstruct;

public abstract class VrafEdenIaRouteLayout extends VrafRouteLayout {

    private Tab graphRoute = new Tab("route.breadcrumb.graphroute");

    private Tab signalRoute = new Tab("route.breadcrumb.nlsignalroute");

    private Tab featureRoute = new Tab("route.breadcrumb.nlfeatureroute");

    private Tab streamApplicationRoute = new Tab("route.breadcrumb.nlstreamapplicationroute");

    @PostConstruct
    protected void setupRoutes() {
        addRouteTab(graphRoute, GraphRoute.class);
        addRouteTab(signalRoute, NlSignalRoute.class);
        addRouteTab(featureRoute, NlFeatureRoute.class);
        addRouteTab(streamApplicationRoute, NLStreamApplicationRoute.class);
        setMaxWidth("83%");
        setBackgroundImage("linear-gradient(#1FAADE, rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1), rgba(255, 255, 255, 1))");
        selectRoute();
    }

    protected abstract void selectRoute();

    public Tab getGraphRoute() {
        return graphRoute;
    }

    public Tab getSignalRoute() {
        return signalRoute;
    }

    public Tab getFeatureRoute() {
        return featureRoute;
    }

    public Tab getStreamApplicationRoute() {
        return streamApplicationRoute;
    }

    @Override
    protected String getRouteLogoPath() {
        return "/frontend/img/eden_logo.png";
    }

}
