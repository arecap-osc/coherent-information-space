package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.edge;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.edge.EdgeView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.phrase.PhraseView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EdgeSelectionView extends LeftSideContentView {

    private HorizontalLayout routeLayout = new HorizontalLayout();

    private HorizontalLayout phraseLayout = new HorizontalLayout();

    @Autowired
    private EdgeSelectionPresenter presenter;

    @Autowired
    private EdgeView edgeView;

    @Autowired
    private PhraseView phraseView;

    @Override
    public EdgeSelectionPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
        getStyle().set("padding-right","10px");
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.getStyle().set("overflow", "hidden");
        wrapper.setHeightFull();
        wrapper.setWidthFull();
        routeLayout.getStyle().set("margin-top", "50px");
        routeLayout.setHeight("50%");
        routeLayout.setWidthFull();
        routeLayout.getStyle().set("position", "relative");

        routeLayout.getStyle().set("border-bottom", "1px solid black");

        phraseLayout.setHeight("50%");
        phraseLayout.setWidthFull();
        phraseLayout.getStyle().set("position", "relative");
        phraseLayout.getStyle().set("display", "block");
        phraseLayout.getStyle().set("border-bottom", "1px solid black");
        routeLayout.add(edgeView);
        phraseLayout.add(phraseView);
        wrapper.add(routeLayout, phraseLayout);
        add(wrapper);
    }

    public EdgeView getEdgeView() {
        return edgeView;
    }

    public PhraseView getPhraseView() {
        return phraseView;
    }


}
