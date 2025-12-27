package org.hkrdi.eden.ggm.vaadin.console.common.i18n.pagetitle;

import static org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic.getTranslation;
import java.util.Locale;

public class TitleFormatter {
 public String format(String key, Locale locale){
 return getTranslation("global.app.name" , locale)
 + " | "
 + getTranslation(key , locale);
 }
}