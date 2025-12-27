package org.arecap.eden.ia.console.microservice.graph;

import com.vaadin.flow.router.Route;
import org.arecap.eden.ia.console.microservice.graph.mvp.GraphSelectionView;
import org.arecap.eden.ia.console.microservice.graph.mvp.GraphSelectorView;
import org.arecap.eden.ia.console.microservice.layout.template.VrafEdenIaRouteLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "graphs")
public class GraphRoute extends VrafEdenIaRouteLayout {

    @Autowired
    private GraphSelectorView graphSelectorView;

    @Autowired
    private GraphSelectionView graphSelectionView;

    @Override
    protected void selectRoute() {
        selectRouteTab(getGraphRoute());
    }

    @Override
    protected String getI18nStoryTitle() {
        return "route.graphroute";
    }

    @Override
    protected void buildLayout() {
        addMenuBarView(graphSelectorView);
        addContentView(graphSelectionView);
    }

}
