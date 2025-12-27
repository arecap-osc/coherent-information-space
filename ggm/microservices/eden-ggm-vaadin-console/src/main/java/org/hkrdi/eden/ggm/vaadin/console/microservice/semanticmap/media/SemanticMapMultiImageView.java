package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SemanticMapMultiImageView extends MultiImageView {

    @Autowired
    private SemanticMapMultiImagePresenter presenter;

    @Override
    public SemanticMapMultiImagePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        super.buildView();
    }

}
