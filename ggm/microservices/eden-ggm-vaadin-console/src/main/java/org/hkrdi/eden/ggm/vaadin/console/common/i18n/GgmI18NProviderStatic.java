package org.hkrdi.eden.ggm.vaadin.console.common.i18n;

import java.util.Locale;

import org.springframework.cop.support.BeanUtil;

import com.vaadin.flow.component.UI;

public class GgmI18NProviderStatic {

	public static String getTranslation(String key, Object... params) {
		Locale locale = UI.getCurrent().getLocale();
		return getTranslation(key, locale, params);
	}
	
	public static String getTranslation(String key, Locale locale, Object... params) {
		return BeanUtil.getBean(GgmI18NProvider.class).getTranslation(key, locale, params);
	}
}
