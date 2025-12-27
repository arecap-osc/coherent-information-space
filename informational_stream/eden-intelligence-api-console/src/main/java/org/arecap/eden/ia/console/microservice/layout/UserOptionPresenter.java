package org.arecap.eden.ia.console.microservice.layout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.mvp.DefaultFlowPresenter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserOptionPresenter extends DefaultFlowPresenter {
    @Autowired
    private RouterNavigationView routerNavigationView;

    public void handleOpenRouteNavigationButtonClick(ClickEvent<Button> buttonClickEvent) {
        routerNavigationView.open();
    }

}
