package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.NodeInformationView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmViewMultiImageView extends MultiImageView {

    @Autowired
    private GgmViewMultiImagePresenter presenter;

    @Autowired
    private NodeInformationView nodeInformationView;

    @Override
    public GgmViewMultiImagePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
        add(nodeInformationView);
    }

    public NodeInformationView getNodeInformationView() {
        return nodeInformationView;
    }
}
