package org.hkrdi.eden.ggm.vaadin.console.microservice.layout;

import org.hkrdi.eden.ggm.vaadin.console.mvp.DefaultFlowPresenter;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class UserOptionPresenter extends DefaultFlowPresenter {
    @Autowired
    private RouterNavigationView routerNavigationView;

    public void handleOpenRouteNavigationButtonClick(ClickEvent<Button> buttonClickEvent) {
        routerNavigationView.open();
    }

}
