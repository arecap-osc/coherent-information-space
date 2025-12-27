package org.arecap.eden.ia.console.microservice.layout.template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@HtmlImport("frontend://bower_components/vaadin-lumo-styles/presets/compact.html")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class ConsolePwaLayout extends HorizontalLayout implements RouterLayout {

    @Autowired
    private TopBarView topBarView;

    @Autowired
    private ContentWrapperView contentWrapperView;

    @Autowired
    private LeftBarView leftBarView;

    @PostConstruct
    private void buildLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
		getStyle().set("padding", "0px 0px 0px 0px");
        contentWrapperView.add(topBarView);
        add(leftBarView, contentWrapperView);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            contentWrapperView.add((Component) content);
        }
    }

}
