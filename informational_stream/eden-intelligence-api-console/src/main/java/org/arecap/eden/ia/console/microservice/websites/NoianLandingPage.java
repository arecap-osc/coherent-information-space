package org.arecap.eden.ia.console.microservice.websites;

import com.vaadin.flow.component.tabs.Tab;
import org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.LandingRouteLayout;
import org.arecap.eden.ia.console.microservice.nl.signal.NlSignalRoute;

import javax.annotation.PostConstruct;

public abstract class NoianLandingPage extends LandingRouteLayout {

    private Tab homeRoute = new Tab("route.breadcrumb.homeroute");

    private Tab iaasRoute = new Tab("route.breadcrumb.iaasroute");

    private Tab paasRoute = new Tab("route.breadcrumb.paasroute");

    private Tab products = new Tab("route.breadcrumb.products");

    private Tab edenIaRoute = new Tab("route.breadcrumb.edeniaroute");

    private Tab aboutRoute = new Tab("route.breadcrumb.aboutroute");

    @PostConstruct
    protected void setupRoutes() {
        addRouteTab(homeRoute, NoianHomePage.class);
        addRouteTab(iaasRoute, IaasPage.class);
        addRouteTab(paasRoute, PaasPage.class);
        addRouteTab(products);
        addRouteTab(edenIaRoute, NlSignalRoute.class);
        addRouteTab(aboutRoute, AboutPage.class);
        setMaxWidth("83%");
        selectRoute();
    }

    @Override
    protected String getLogoPath() {
        return "frontend/img/Vraf_Action_Logo.png";
    }

    protected abstract void selectRoute();

    public Tab getHomeRoute() {
        return homeRoute;
    }

    public Tab getIaasRoute() {
        return iaasRoute;
    }

    public Tab getPaasRoute() {
        return paasRoute;
    }

    public Tab getEdenIaRoute() {
        return edenIaRoute;
    }

    public Tab getAboutRoute() {
        return aboutRoute;
    }
}
