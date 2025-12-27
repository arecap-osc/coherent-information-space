package org.arecap.eden.ia.console.microservice.nl.feature;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import org.arecap.eden.ia.console.microservice.nl.InformationalStreamRouteLayout;
import org.arecap.eden.ia.console.microservice.nl.feature.mvp.FeatureStreamView;
import org.arecap.eden.ia.console.microservice.nl.feature.mvp.FeatureView;
import org.arecap.eden.ia.console.microservice.nl.signal.mvp.SignalView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "nl/feature")
public class NlFeatureRoute extends InformationalStreamRouteLayout {

    @Autowired
    private FeatureView featureView;

    @Autowired
    private SignalView signalView;

    @Autowired
    private FeatureStreamView featureStreamView;

    private HorizontalLayout routeContent = new HorizontalLayout();

    @Override
    protected void buildLayout() {
        Accordion accordion = new Accordion();
        accordion.add("Signals", signalView);
        accordion.add("Feature Streams", featureStreamView);
        accordion.setSizeFull();
        signalView.getStyle().set("border-left", "1px solid black");
        featureStreamView.getStyle().set("border-left", "1px solid black");
        routeContent.add(featureView, accordion);
        routeContent.setSizeFull();
        routeContent.getStyle().set("overflow", "auto");
        addContent(routeContent);
    }

    @Override
    protected String getI18nStoryTitle() {
        return "route.nlfeatureroute";
    }

    @Override
    protected void selectRoute() {
        selectRouteTab(getFeatureRoute());
    }

}
