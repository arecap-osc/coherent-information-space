package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.node;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.GgmNodeSelectionProcessor;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.GgmMultiImagePresenter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmNodeMultiImagePresenter extends GgmMultiImagePresenter {

    @Autowired
    private GgmNodeSelectionProcessor stateSelectionManager;


    @Override
    protected GgmNodeSelectionProcessor getStateSelectionProcessor() {
        return stateSelectionManager;
    }
}
