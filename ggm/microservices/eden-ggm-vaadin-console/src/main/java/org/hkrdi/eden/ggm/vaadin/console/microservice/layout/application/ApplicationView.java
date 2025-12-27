package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.application;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cop.support.BeanUtil;

public abstract class ApplicationView extends HorizontalLayout implements FlowView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationView.class);

    @Autowired
    private ApplicationDialogView applicationDialogView;

    private ComboBox<Application> applicationComboBox = new ComboBox<>();

    private Button applicationDialogButton = new Button(VaadinIcon.MENU.create());

    private HorizontalLayout wrapperLayout = new HorizontalLayout();

    @Override
    public void buildView() {
        customizeView();
        buildViewWrapper();
        buildEventListeners();
    }

    @Override
    public void afterPrepareView() {
        if (applicationComboBox.getOptionalValue().isPresent() == false) {
            getPresenter().applicationComboBoxSetItemsAndSelectValue();
        }
    }

    private void customizeView() {
        //        getStyle().set("padding-left", "20px");
//        setMargin(false);
//        setPadding(false);
//        setDefaultVerticalComponentAlignment(Alignment.CENTER);
//        getStyle().set("background-color", "#f2f2f2");
    }

    private void buildEventListeners() {
        applicationDialogButton.addClickListener(e -> applicationDialogView.open(this));
        applicationComboBox.addValueChangeListener(getPresenter()::handleApplicationComboBoxValueChange);
    }

    private void buildViewWrapper() {
        applicationDialogButton.setWidth("40px");
        applicationDialogButton.getElement().setAttribute("title", "tooltip.editapp.button");

        applicationComboBox.setItemLabelGenerator(sg -> sg.getBriefLabelTmp());
        applicationComboBox.setAllowCustomValue(false);
        applicationComboBox.getElement().setAttribute("title", "tooltip.currentapp.combobox");
        applicationComboBox.setWidth("300px");

        wrapperLayout.setHeight("40px");
        wrapperLayout.getStyle().set("border-radius", "5%");
        wrapperLayout.add(applicationComboBox, applicationDialogButton);
        wrapperLayout.setId("ffff");

        add(wrapperLayout);
    }

    public ComboBox<Application> getApplicationComboBox() {
        return applicationComboBox;
    }

    public HorizontalLayout getWrapperLayout() {
        return wrapperLayout;
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public abstract ApplicationPresenter getPresenter();

    @Override
    public void localeChange(LocaleChangeEvent event) {
    	FlowView.super.localeChange(event);
    	try {
    		getPresenter().applicationComboBoxSetItemsAndSelectValue(event.getLocale().getLanguage().toLowerCase());
    	}catch(Throwable t) {
    		//silent
	}
    }
}
