package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.application;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media.RoadFractoloniViewOptionalPaneView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class ReadEdgeSelectionView extends LeftSideContentView {

    @Autowired
    private ReadNodeView readNodeView;

    @Autowired
    private ReadEdgeView readEdgeView;

    private Label phraseIdFromNode = new Label();

    private Label phraseIdRoute = new Label();

    private Label phraseIdToNode = new Label();

    private HorizontalLayout nodesWrapper = new HorizontalLayout();

    @Autowired
    private ReadEdgeSelectionPresenter presenter;

    @Override
    public void buildView() {
        super.buildView();
        getStyle().set("padding-right", "10px");
        getStyle().set("padding-top", "50px");

        phraseIdFromNode.getStyle().set("max-width", "400px");
        phraseIdFromNode.getStyle().set("color", "red");
        phraseIdRoute.getStyle().set("max-width", "400px");
        phraseIdRoute.getStyle().set("color", "green");
        phraseIdToNode.getStyle().set("max-width", "400px");
        phraseIdToNode.getStyle().set("color", "blue");
        VerticalLayout verticalWrapper = new VerticalLayout();
        verticalWrapper.getStyle().set("overflow", "hidden");

        nodesWrapper.setWidthFull();
        nodesWrapper.setHeight("50%");
        nodesWrapper.add(readEdgeView);
        nodesWrapper.getStyle().set("border-bottom", "1px solid black");
        VerticalLayout phraseWrapper = new VerticalLayout();
        phraseWrapper.setWidthFull();
        phraseWrapper.setHeight("50%");
        phraseWrapper.add(phraseIdFromNode, phraseIdRoute, phraseIdToNode);

        verticalWrapper.setSizeFull();
        verticalWrapper.add(nodesWrapper, phraseWrapper);
        add(verticalWrapper);
    }

    public HorizontalLayout getNodesWrapper() {
        return nodesWrapper;
    }

    @Override
    public ReadEdgeSelectionPresenter getPresenter() {
        return presenter;
    }

    public ReadNodeView getReadNodeView() {
        return readNodeView;
    }

    public ReadEdgeView getReadEdgeView() {
        return readEdgeView;
    }

    public Label getPhraseIdFromNode() {
        return phraseIdFromNode;
    }

    public Label getPhraseIdRoute() {
        return phraseIdRoute;
    }

    public Label getPhraseIdToNode() {
        return phraseIdToNode;
    }

    public void setStyleForRoadFractoloniView(RoadFractoloniViewOptionalPaneView paneView) {
        getStyle().set("margin-left", (new Integer(paneView.getWidth().replace("px", "")) - 5) + "px");
    }
}
