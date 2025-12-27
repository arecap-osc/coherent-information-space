package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScalePresenter;

@SpringComponent
@UIScope
public class CoherentSpaceMultiImageSettingsPresenter extends MultiImageScalePresenter {

    @Override
    public CoherentSpaceMultiImageSettingsView getView() {
        return (CoherentSpaceMultiImageSettingsView) super.getView();
    }

    public void handleNetworkStyleToggle(ClickEvent<Button> buttonClickEvent) {
        getView().getNetworkMediaLayerStyleView().setVisible(!getView().getNetworkMediaLayerStyleView().isVisible());
    }

    public void switchNodeInformationView(ClickEvent<Button> buttonClickEvent) {
        getView().getNodeInformationView().setEnabled(!getView().getNodeInformationView().isEnabled());
        getView().getNodeInformationSwitchBtn().setIcon(getView().getNodeInformationView().isEnabled() ?
                VaadinIcon.EYE.create() : VaadinIcon.EYE_SLASH.create());
    }
}
