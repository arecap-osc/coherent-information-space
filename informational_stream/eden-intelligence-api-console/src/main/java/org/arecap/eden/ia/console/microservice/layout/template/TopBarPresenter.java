package org.arecap.eden.ia.console.microservice.layout.template;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.featureflag.FeatureFlags;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventObject;

@SpringComponent
@UIScope
public class TopBarPresenter extends DefaultFlowPresenter<TopBarView> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopBarPresenter.class);

    @Override
    public void afterPrepareModel(EventObject event) {
        super.afterPrepareModel(event);
        if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
            if (UI.getCurrent().getSession().getAttribute("selectedLanguage") == null) {
                UI.getCurrent().getSession().setAttribute("selectedLanguage", UI.getCurrent().getLocale().getLanguage().toUpperCase());
            }
            getView().setLanguageInCombo(UI.getCurrent().getLocale());
        }
    }

}
