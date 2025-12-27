package org.arecap.eden.ia.console.common.i18n.pagetitle;

import java.util.Locale;

import static org.arecap.eden.ia.console.common.i18n.I18NProviderStatic.getTranslation;


public class TitleFormatter {
 public String format(String key, Locale locale){
 return getTranslation("global.app.name" , locale)
 + " | "
 + getTranslation(key , locale);
 }
}