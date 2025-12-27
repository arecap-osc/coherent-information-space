package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScalePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScaleView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class GgmViewMultiImageScaleView extends MultiImageScaleView {

    @Autowired
    private GgmViewMultiImageScalePresenter presenter;

    @Autowired
    private GgmViewMultiImageView multiImageView;


    @Override
    public MultiImageScalePresenter getPresenter() {
        return presenter;
    }

    @Override
    public GgmViewMultiImageView getMultiImageView() {
        return multiImageView;
    }
}
