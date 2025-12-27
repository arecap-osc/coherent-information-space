package org.hkrdi.eden.ggm.vaadin.console.microservice.layout;

import javax.annotation.PostConstruct;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.flow.InternationalizeViewEngine;
import org.hkrdi.eden.ggm.vaadin.console.featureflag.FeatureFlags;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.RoadFractoloniViewRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmEdgeRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmNodeRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.GgmViewRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.RoadFractoloniRoadRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.RoadManualRoadRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.RoadRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.DimenssionUnicursalFeedbackRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.NavigationModalOptionView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.SemanticMapRoute;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RouterNavigationView extends VerticalLayout implements LocaleChangeObserver{

    private H4 title = new H4("msg.route.breadcrumb.choose.h4");

    @Autowired
    private NavigationModalOptionView leftNavigationModalOptionView;

    @PostConstruct
    public void setup() {
        setSizeFull();
        getStyle().set("color", "#ccc");
        getStyle().set("position", "relative");
        leftNavigationModalOptionView.getStyle().set("margin", "0px");
        Anchor ggmNodesRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(GgmNodeRoute.class), "msg.route.breadcrumb.noderoute");
        Anchor ggmEdgesRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(GgmEdgeRoute.class), "msg.route.breadcrumb.edgeroute");
        Anchor semanticRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(SemanticMapRoute.class), "msg.route.breadcrumb.semanticmap");
        Anchor roadRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(RoadRoute.class), "msg.route.breadcrumb.automaticroad");
        Anchor roadManualRoadRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(RoadManualRoadRoute.class), "msg.route.breadcrumb.manualroad");
        Anchor roadFractoloniRoadRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(RoadFractoloniRoadRoute.class), "msg.route.breadcrumb.fractoloniroad");
        Anchor firstDegreeFeedbackRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(DimenssionUnicursalFeedbackRoute.class), "msg.route.breadcrumb.firstdegreefeedback");
        Anchor roadViewFractoloniRoute = new Anchor(RouteConfiguration.forApplicationScope().getUrl(RoadFractoloniViewRoute.class), "msg.route.breadcrumb.ggmview");

        Anchor ggmView = new Anchor(RouteConfiguration.forApplicationScope().getUrl(GgmViewRoute.class), "msg.route.breadcrumb.book");
        ggmView.getStyle().set("position", "absolute");
        ggmView.getStyle().set("bottom", "30px");


        add(title, ggmView);
        addIfFeatureIsEnabled(roadViewFractoloniRoute, FeatureFlags.ROAD_VIEW_NAVIGATOR);
        addIfFeatureIsEnabled(ggmNodesRoute, FeatureFlags.COHERENT_SPACE_GGM_NAVIGATOR);
        addIfFeatureIsEnabled(ggmEdgesRoute, FeatureFlags.COHERENT_SPACE_GGM_NAVIGATOR);
        addIfFeatureIsEnabled(roadRoute, FeatureFlags.ROAD_NAVIGATOR);
        addIfFeatureIsEnabled(roadManualRoadRoute, FeatureFlags.ROAD_NAVIGATOR);
        addIfFeatureIsEnabled(roadFractoloniRoadRoute, FeatureFlags.ROAD_NAVIGATOR);
        addIfFeatureIsEnabled(semanticRoute, FeatureFlags.SEMANTIC_MAP_NAVIGATOR);
        addIfFeatureIsEnabled(firstDegreeFeedbackRoute, FeatureFlags.FIRST_DEGREE_FEEDBACK_NAVIGATOR); 
    }

    public void open() {
        leftNavigationModalOptionView.open(this);
    }

    private void addIfFeatureIsEnabled(Anchor anchor, FeatureFlags featureFlags) {
    	if (featureFlags.check()) {add(anchor);}
    }

	@Override
	public void localeChange(LocaleChangeEvent event) {
		InternationalizeViewEngine.internationalize(this, event.getLocale());
	}
}
