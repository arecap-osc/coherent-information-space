package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggmview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScalePresenter;

@SpringComponent
@UIScope
public class GgmViewMultiImageScalePresenter extends MultiImageScalePresenter {

    @Override
    public GgmViewMultiImageScaleView getView() {
        return (GgmViewMultiImageScaleView) super.getView();
    }
}
