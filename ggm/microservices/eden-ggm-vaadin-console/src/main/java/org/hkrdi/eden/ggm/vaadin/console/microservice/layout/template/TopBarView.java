package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;


import org.hkrdi.eden.ggm.vaadin.console.featureflag.FeatureFlags;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class TopBarView extends HorizontalLayout implements FlowView {

    @Autowired
    private TopBarPresenter presenter;

    private Image hkrdiLogo = new Image("/frontend/img/hkrdilogo_white.jpg", "Human Knowledge Research and Development Institute");

    private TextField markMultipleTextField = new TextField();

    private Button markButton = new Button(VaadinIcon.MAP_MARKER.create());

    private Image dedemanLogo = new Image("/frontend/img/dedeman-ie-logo.png", "Dedeman dedicat planurilor tale");

    private Button clearMarkingsButton = new Button(VaadinIcon.CLOSE.create());
    
    private ComboBox<String> languageCombo = new ComboBox<String>();

    @Override
    public void buildView() {
        customizeView();
        markButton.addClickListener(getPresenter()::markProvidedNodesAndEdges);
        clearMarkingsButton.addClickListener(getPresenter()::resetMarkingSelection);

        if (FeatureFlags.DEDEMAN_LOGO.check()) {
            add(dedemanLogo);
        }
        final int buttonPositionRightWidth = FeatureFlags.DEDEMAN_LOGO.check() ? 205 : 65;

        HorizontalLayout wrapper = new HorizontalLayout();
        wrapper.getStyle().set("position", "absolute");
        wrapper.getStyle().set("right", buttonPositionRightWidth + "px");
        wrapper.add(markMultipleTextField, markButton, clearMarkingsButton);
        if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
        	languageCombo.setItems("EN", "RO");
        	languageCombo.setAllowCustomValue(false);
        	languageCombo.setItemLabelGenerator(s->s);
            languageCombo.addValueChangeListener(event-> getPresenter().languageSelected(event.getValue()));
        	wrapper.add(languageCombo);
        }
        add(hkrdiLogo, wrapper);
    }

    private void customizeView() {
        setWidthFull();
        setSpacing(false);
        setHeight("50px");
        getStyle().set("z-index", "200");
        getStyle().set("position", "relative");
        getStyle().set("background-color", "#F5F5F5");
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        hkrdiLogo.setTitle("Human Knowledge Research and Development Institute");
        hkrdiLogo.setHeight("50px");
        hkrdiLogo.getStyle().set("position", "absolute");
        hkrdiLogo.getStyle().set("right", "0px");

        dedemanLogo.setHeight("70px");
        dedemanLogo.setTitle("Dedeman dedicat planurilor tale");
        dedemanLogo.getStyle().set("position", "absolute");
        dedemanLogo.getStyle().set("right", "50px");

        markMultipleTextField.setVisible(false);
        markMultipleTextField.setPlaceholder("ex: 24, 13, pondereaza, 19, ORGANIZEAZA, 64...");
        markMultipleTextField.setWidth("200px");

        languageCombo.setWidth("90px");

        clearMarkingsButton.getElement().setAttribute("title", "tooltip.delete.button");
        clearMarkingsButton.setVisible(false);

        markButton.getElement().setAttribute("title", "tooltip.mark.button");
        markButton.setVisible(false);

//        if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
//        	languageCombo.setVisible(false);
//        }
    }

    public void setLanguageInCombo(String language) {
    	languageCombo.setValue(language.toUpperCase());
    }

    public String getNodesAndEdgesToMark() {
        return markMultipleTextField.getValue();
    }

    public void showMarkFunctionality() {
        markMultipleTextField.setVisible(true);
        markButton.setVisible(true);
        clearMarkingsButton.setVisible(true);
//        if (FeatureFlags.LANGUAGE_CHOOSER_COMBOBOX.check()) {
//        	languageCombo.setVisible(true);
//        }
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public TopBarPresenter getPresenter() {
        return presenter;
    }

    public void clearMarkMultipleTextField() {
        markMultipleTextField.clear();
    }
}
