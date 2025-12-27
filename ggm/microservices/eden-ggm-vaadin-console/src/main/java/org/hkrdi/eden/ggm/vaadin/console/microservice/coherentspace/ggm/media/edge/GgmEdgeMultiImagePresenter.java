package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.edge;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmEdgeSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMultiImagePresenter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmEdgeMultiImagePresenter extends GgmMultiImagePresenter {

    @Autowired
    private GgmEdgeSelectionProcessor stateSelectionManager;

    @Override
    protected GgmEdgeSelectionProcessor getStateSelectionProcessor() {
        return stateSelectionManager;
    }
}
