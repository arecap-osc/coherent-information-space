package org.hkrdi.eden.ggm.vaadin.console.microservice.media;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;

public abstract class ConfigurableMediaRendererLayerStyleView extends VerticalLayout implements FlowView {

    private ConfigurableMediaLayerStyleView configurableMediaLayerStyleView;

    private Input colorInput = constructColorPickerInput("#6b89d8");
    private Input penInput = constructRangeInput("4.4", "10", "0.1");
    private Input opacityInput = constructRangeInput("1", "1", "0.01");

    @Override
    public void buildView() {
        buildViewWrappers();
        buildEventListeners();
        setEnabled(false);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public abstract ConfigurableMediaRendererLayerStylePresenter getPresenter();

    public ConfigurableMediaLayerStyleView getConfigurableMediaLayerStyleView() {
        return configurableMediaLayerStyleView;
    }

    public void setConfigurableMediaLayerStyleView(ConfigurableMediaLayerStyleView configurableMediaLayerStyleView) {
        this.configurableMediaLayerStyleView = configurableMediaLayerStyleView;
    }

    private void buildViewWrappers() {
        getStyle().set("background-color", "white");
        penInput.setPlaceholder("Pen");
        opacityInput.setPlaceholder("Alpha");
        HorizontalLayout inputs = new HorizontalLayout(colorInput, penInput, opacityInput);
        add(inputs);
    }

    private void buildEventListeners() {
        colorInput.addValueChangeListener(getPresenter()::handleColorValueChanged);
        penInput.addValueChangeListener(getPresenter()::handlePenValueChanged);
        opacityInput.addValueChangeListener(getPresenter()::handleOpacityValueChanged);
    }


    private Input constructRangeInput(String value, String max, String step) {
        return constructRangeInput(value, "0", max, step);
    }

    private Input constructRangeInput(String value, String min, String max, String step) {
        Input rangeInput = new Input();
        rangeInput.setType("range");
        rangeInput.setValue(value);
        rangeInput.getElement().setAttribute("value", value);
        rangeInput.getElement().setAttribute("min", min);
        rangeInput.getElement().setAttribute("max", max);
        rangeInput.getElement().setAttribute("step", step);
        return rangeInput;
    }

    private Input constructColorPickerInput(String color) {
        Input colorPickerInput = new Input();
        colorPickerInput.setType("color");
        colorPickerInput.setValue(color);
        colorPickerInput.getElement().setAttribute("value", color);
        return colorPickerInput;
    }

    public Input getColorInput() {
        return colorInput;
    }

    public Input getPenInput() {
        return penInput;
    }

    public Input getOpacityInput() {
        return opacityInput;
    }
}
