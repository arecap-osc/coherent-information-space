package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.RoadFractoloniViewMultiImageView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScalePresenter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScaleView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class RoadFractoloniViewMultiImageScaleView extends MultiImageScaleView {

    @Autowired
    private RoadFractoloniViewMultiImageScalePresenter presenter;

    @Autowired
    private RoadFractoloniViewMultiImageView multiImageView;

    @Override
    public MultiImageScalePresenter getPresenter() {
        return presenter;
    }

    @Override
    public RoadFractoloniViewMultiImageView getMultiImageView() {
        return multiImageView;
    }
}
