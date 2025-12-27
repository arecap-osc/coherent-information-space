package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap;

import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapService;
import org.springframework.cop.support.BeanUtil;

public interface SemanticMapServiceFactory {

    default SemanticMapService getSemanticMapService() {
        return BeanUtil.getBean(SemanticMapService.class);
    }
}
