package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.application.node;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.edge.EdgeView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node.FirstNodeView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application.node.SecondNodeView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NodeSelectionView extends LeftSideContentView {

    private HorizontalLayout nodesLayout = new HorizontalLayout();

    private HorizontalLayout routeLayout = new HorizontalLayout();

    @Autowired
    private NodeSelectionPresenter presenter;

    @Autowired
    private FirstNodeView firstNodeView;

    @Autowired
    private SecondNodeView secondNodeView;

    @Autowired
    private EdgeView edgeView;

    @Override
    public NodeSelectionPresenter getPresenter() {
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
        nodesLayout.getStyle().set("margin-top", "50px");
        nodesLayout.setHeight("35%");
        nodesLayout.setWidthFull();
        nodesLayout.getStyle().set("position", "relative");

        nodesLayout.getStyle().set("border-bottom", "1px solid black");

        routeLayout.setHeight("65%");
        routeLayout.setWidthFull();
        routeLayout.getStyle().set("position", "relative");
        routeLayout.getStyle().set("display", "block");
        routeLayout.getStyle().set("border-bottom", "1px solid black");
        routeLayout.add(edgeView);
        nodesLayout.add(firstNodeView, secondNodeView);

        wrapper.add(nodesLayout, routeLayout);
        add(wrapper);
    }

    public FirstNodeView getFirstNodeView() {
        return firstNodeView;
    }

    public SecondNodeView getSecondNodeView() {
        return secondNodeView;
    }

    public EdgeView getEdgeView() {
        return edgeView;
    }

}
