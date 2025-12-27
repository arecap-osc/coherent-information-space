package org.arecap.eden.ia.console.microservice.nl.signal;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.arecap.eden.ia.console.microservice.nl.InformationalStreamRouteLayout;
import org.arecap.eden.ia.console.microservice.nl.feature.mvp.FeatureStreamView;
import org.arecap.eden.ia.console.microservice.nl.signal.mvp.SignalView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "nl/signal")
public class NlSignalRoute extends InformationalStreamRouteLayout {

    @Autowired
    private SignalView signalView;

    @Autowired
    private FeatureStreamView featureStreamView;

    @Override
    protected void buildLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        Accordion accordion = new Accordion();
        accordion.add("Signals", signalView);
        accordion.add("Feature Streams", featureStreamView);
        accordion.setSizeFull();
        wrapper.setSizeFull();
        wrapper.getStyle().set("overflow", "auto");
        wrapper.add(accordion);
        addContent(wrapper);
    }

    @Override
    protected String getI18nStoryTitle() {
        return "route.nlsignalroute";
    }


    @Override
    protected void selectRoute() {
        selectRouteTab(getSignalRoute());
    }
}
