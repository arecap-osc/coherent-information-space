package org.arecap.eden.ia.console.microservice.layout.template.mediaconsole;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.arecap.eden.ia.console.mvp.FlowView;

import javax.annotation.PostConstruct;

@HtmlImport("frontend://bower_components/vaadin-lumo-styles/presets/compact.html")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@Viewport(value = Viewport.DEVICE_DIMENSIONS)
public abstract class MediaConsoleRouteLayout extends HorizontalLayout {

    private VerticalLayout contentWrapper = new VerticalLayout();

    @PostConstruct
    protected void setup() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("overflow", "hidden");
        contentWrapper.setSizeFull();
        contentWrapper.getStyle().set("overflow", "hidden");
        contentWrapper.setPadding(false);
        contentWrapper.setSpacing(false);
        add(contentWrapper);
        buildLayout();
    }

    protected abstract void buildLayout();


    public void setMaxWidth(String maxWidth) {
        contentWrapper.getStyle().set("max-width", maxWidth);
    }

    protected void setBackgroundImage(String backgroundImage) {
        getStyle().set("background-image", backgroundImage);
    }

    protected void addHeaderView(FlowView flowView) {
        contentWrapper.addComponentAsFirst((Component) flowView);
    }


    protected void addContentView(FlowView flowView) {
        addContent((Component) flowView);
    }

    protected void addContent(Component component) {
        contentWrapper.add(component);
    }

}
