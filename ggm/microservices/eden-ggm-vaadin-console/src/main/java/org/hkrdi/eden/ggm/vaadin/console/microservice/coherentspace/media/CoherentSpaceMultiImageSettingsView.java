package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ggm.media.NodeInformationView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageScaleView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.media.MultiImageView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class CoherentSpaceMultiImageSettingsView extends MultiImageScaleView {

    private Button networkStyleToggleBtn = new Button(VaadinIcon.MENU.create());

    private Button nodeInformationSwitchBtn = new Button(VaadinIcon.EYE_SLASH.create());

    private MultiImageView multiImageView;

    @Autowired
    private CoherentSpaceMultiImageSettingsPresenter presenter;

    @Autowired
    private NodeInformationView nodeInformationView;

    private NetworkMediaLayerStyleView networkMediaLayerStyleView;

    @Override
    public void buildView() {
        super.buildView();
        networkStyleToggleBtn.getStyle().set("border-radius", "100%").set("background-color", "white");
        networkStyleToggleBtn.getStyle().set("color", "red");
        networkStyleToggleBtn.addClickListener(getPresenter()::handleNetworkStyleToggle);
        nodeInformationSwitchBtn.getStyle().set("border-radius","50%").set("background-color", "white");
        nodeInformationSwitchBtn.addClickListener(getPresenter()::switchNodeInformationView);
        nodeInformationSwitchBtn.getStyle().set("color", "red");
        add(nodeInformationSwitchBtn, networkStyleToggleBtn);
    }

    public void setMultiImageView(MultiImageView multiImageView) {
        this.multiImageView = multiImageView;
    }

    public void setNetworkMediaLayerStyleView(NetworkMediaLayerStyleView networkMediaLayerStyleView) {
        this.networkMediaLayerStyleView = networkMediaLayerStyleView;
    }

    @Override
    public CoherentSpaceMultiImageSettingsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public MultiImageView getMultiImageView() {
        return multiImageView;
    }

    public NetworkMediaLayerStyleView getNetworkMediaLayerStyleView() {
        return networkMediaLayerStyleView;
    }

    public NodeInformationView getNodeInformationView() {
        return nodeInformationView;
    }

    public Button getNodeInformationSwitchBtn() {
        return nodeInformationSwitchBtn;
    }
}
