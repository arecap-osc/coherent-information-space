package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScalePresenter;

@SpringComponent
@UIScope
public class RoadFractoloniViewMultiImageScalePresenter extends MultiImageScalePresenter {

    @Override
    public RoadFractoloniViewMultiImageScaleView getView() {
        return (RoadFractoloniViewMultiImageScaleView) super.getView();
    }
}
