package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class DimenssionUnicursalPresenter extends DefaultFlowPresenter<FeedbackDataMap, Long>{
}
