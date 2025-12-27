package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.ip;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.GgmSystemRouteLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.GgmSystemPwaLayout;
import org.hkrdi.eden.ggm.vaadin.console.microservice.util.NetIdentity;

@Route(value = "ipauthorized", layout = GgmSystemPwaLayout.class)
public class IpRoute extends GgmSystemRouteLayout {


    @Override
    protected void buildRouteLayout() {
        TextArea test = new TextArea();
        test.setWidthFull();
        test.setValue("AUTHORIZED-------------------------------\n"+
                new NetIdentity().getDescription());
        add(test);
    }

    @Override
    protected String getRouteIconPath() {
        return "/frontend/img/road_logo.png";
    }

    @Override
    protected String getRouteNameForBredCrumbAndTooltip() {
        return "ipauthorized";
    }
}
