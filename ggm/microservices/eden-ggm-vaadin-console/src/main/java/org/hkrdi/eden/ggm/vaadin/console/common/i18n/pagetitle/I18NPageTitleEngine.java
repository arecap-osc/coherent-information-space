package org.hkrdi.eden.ggm.vaadin.console.common.i18n.pagetitle;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

@Component
public class I18NPageTitleEngine
       implements VaadinServiceInitListener,
                  UIInitListener, BeforeEnterListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(I18NPageTitleEngine.class);
  public static final String ERROR_MSG_NO_LOCALE = "no locale provided and i18nProvider #getProvidedLocales()# list is empty !! ";
  public static final String ERROR_MSG_NO_ANNOTATION = "no annotation found at class ";

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Class<?> navigationTarget = event.getNavigationTarget();
    I18NPageTitle annotation = navigationTarget.getAnnotation(I18NPageTitle.class);
       if(annotation == null) {
    	   LOGGER.info(ERROR_MSG_NO_ANNOTATION + navigationTarget.getName());
    } else {
      final String messageKey = (annotation.messageKey().isEmpty())
                          ? annotation.defaultValue()
                          : annotation.messageKey();

      final I18NProvider i18NProvider = VaadinService
          .getCurrent()
          .getInstantiator()
          .getI18NProvider();
      final Locale locale = event.getUI().getLocale();
      final List<Locale> providedLocales = i18NProvider.getProvidedLocales();

      Locale providedLocale = null;

      if(locale == null && providedLocales.isEmpty()){
    	  LOGGER.info(ERROR_MSG_NO_LOCALE + i18NProvider.getClass().getName());
      } else if(locale == null){
        providedLocale = providedLocales.get(0);
      } else if(providedLocales.contains(locale)) {
        providedLocale = locale;
      } else {
        providedLocale = providedLocales.get(0);
      }

      final Class<? extends TitleFormatter> formatterCls = annotation.formatter();

      try {
        final TitleFormatter formatter = formatterCls.getDeclaredConstructor().newInstance();
        String title = formatter.format(messageKey, providedLocale);
        UI.getCurrent().getPage().setTitle(title);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void uiInit(UIInitEvent event) {
    final UI ui = event.getUI();
    ui.addBeforeEnterListener(this);
  }

  @Override
  public void serviceInit(ServiceInitEvent event) {
    event
        .getSource()
        .addUIInitListener(this);
  }
}