package org.arecap.eden.ia.console.microservice.layout;

import com.vaadin.flow.component.UI;

import java.util.Locale;

public final class UiI18nUtil {

    public static void setLocale(Locale locale) {
        if(locale != null) {
            UI.getCurrent().access(() -> {
                UI.getCurrent().setLocale(locale);
                UI.getCurrent().getSession().setAttribute("selectedLanguage", locale.getLanguage().toUpperCase());
            });
        }
    }


}
