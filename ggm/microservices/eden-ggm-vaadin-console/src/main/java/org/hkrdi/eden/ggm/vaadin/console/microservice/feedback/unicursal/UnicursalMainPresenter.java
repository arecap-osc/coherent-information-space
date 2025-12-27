package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.springframework.stereotype.Component;

import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UnicursalMainPresenter extends DefaultFlowPresenter<FeedbackDataMap, Long>{
}
