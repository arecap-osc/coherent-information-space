package org.arecap.eden.ia.console.microservice.graph.mvp;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.microservice.graph.event.GraphSelectionChangedEvent;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;

import java.util.Optional;

@SpringComponent
@UIScope
public class GraphSelectorPresenter extends DefaultFlowPresenter<GraphSelectorView> {

    public void publishGraphSelection(Optional<String> value) {
        getUIEventBus().publish(this, new GraphSelectionChangedEvent(value));
    }

}
