package org.hkrdi.eden.ggm.vaadin.console.common.i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

@Component
public class GgmI18NProvider implements I18NProvider {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GgmI18NProvider.class);

	public GgmI18NProvider() {
		LOGGER.info(GgmI18NProvider.class.getSimpleName() + " was found..");
	}

	public static final String RESOURCE_BUNDLE_NAME = "edenggm";
	private static final Map<String, ResourceBundle> bundles;
	private static Locale defaultLocale = Locale.ENGLISH;

	private static final List<Locale> providedLocales = Collections
			.unmodifiableList(Arrays.asList( 
					defaultLocale,
					new Locale("ro", "RO") 
			));

	static {
		bundles = providedLocales.stream()
				.collect(Collectors.toMap(l -> l.toString(), l -> ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, l)));
	}

	@Override
	public List<Locale> getProvidedLocales() {
		LOGGER.info("GGMI18NProvider getProvidedLocales..");
		return providedLocales;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		if (key==null || key.isEmpty()) {
			return key;
		}
		String localeKey = locale.toString();
		if (locale.getCountry() == null || locale.getCountry().isEmpty()) {
			localeKey = localeKey+"_"+localeKey.toUpperCase();
		}
		ResourceBundle rb = bundles.get(localeKey);
		if (rb == null) {
			rb = bundles.get(defaultLocale.toString());
		}

		String msg = null;
		try {
			msg = rb.getString(key);
		} catch (MissingResourceException e) {
			LOGGER.error("missing ressource key (i18n) " + key);
			return "!" + locale.getLanguage() + "." + key;
		}
		if (msg.contains("{")) {
			return new MessageFormat(msg, locale).format(params);
		}
		return msg;
	}

}