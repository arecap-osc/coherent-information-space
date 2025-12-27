package org.arecap.eden.ia.console.microservice.layout.template;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.featureflag.FeatureFlags;
import org.arecap.eden.ia.console.microservice.layout.UiI18nUtil;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

@SpringComponent
@UIScope
public class TopBarView extends HorizontalLayout implements FlowView<TopBarPresenter> {

    @Autowired
    private TopBarPresenter presenter;

    private Image edenLogo = new Image("/frontend/img/Noian_Logo_Color.png", "Enunțuri, Demonstrați, Erori și Natura - Intelligence Api");

    private ComboBox<Locale> languageCombo = new ComboBox<Locale>("", Locale.ENGLISH, new Locale("RO"));

    @Override
    public void buildView() {
        customizeView();

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.getStyle().set("position", "absolute");
        wrapper.getStyle().set("right", "100px");
        if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
        	languageCombo.setAllowCustomValue(false);
        	languageCombo.setItemLabelGenerator(s->s.getLanguage());
            languageCombo.addValueChangeListener(event-> UiI18nUtil.setLocale(event.getValue()));
        	wrapper.add(languageCombo);
        }
        add(edenLogo, wrapper);
    }

    private void customizeView() {
        setWidthFull();
        setSpacing(false);
        setHeight("50px");
        getStyle().set("z-index", "200");
        getStyle().set("position", "relative");
        getStyle().set("background-color", "#F5F5F5");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        edenLogo.setTitle("Enunțuri, Demonstrați, Erori și Natura - Intelligence Api");
        edenLogo.setHeight("50px");
        edenLogo.getStyle().set("position", "absolute");
        edenLogo.getStyle().set("right", "0px");

        languageCombo.setWidth("90px");

    }

    public void setLanguageInCombo(Locale locale) {
    	languageCombo.setValue(locale);
    }


    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public TopBarPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        FlowView.super.localeChange(event);
        setLanguageInCombo(event.getLocale());
    }
}
