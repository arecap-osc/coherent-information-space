package org.arecap.eden.ia.console.microservice.graph.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.microservice.graph.event.GraphSelectionChangedEvent;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.Optional;

@SpringComponent
@UIScope
public class GraphSelectionPresenter extends DefaultFlowPresenter<GraphSelectionView> {


    @EventBusListenerMethod
    public void onGraphSelectionChangedEvent(GraphSelectionChangedEvent graphSelectionChangedEvent) {
        Optional<String> selectedGraph = (Optional<String>) graphSelectionChangedEvent.getSource();
        if(selectedGraph.isPresent()) {
            if(selectedGraph.get().equalsIgnoreCase(GraphSelectorView.Mandelbrot_Set_Graph)) {
                getView().displayMandelbrotSetGraphView();
            }
            if(selectedGraph.get().equalsIgnoreCase(GraphSelectorView.Informational_Stream_Graph)) {
                getView().displayInformationalStreamGraphView();
            }
            return;
        }
        getView().displayNone();
    }
}
