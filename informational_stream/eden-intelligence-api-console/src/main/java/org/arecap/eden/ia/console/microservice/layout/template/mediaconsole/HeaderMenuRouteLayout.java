package org.arecap.eden.ia.console.microservice.layout.template.mediaconsole;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouteConfiguration;
import org.arecap.eden.ia.console.microservice.layout.JsUtil;
import org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.mvp.HeaderView;
import org.arecap.eden.ia.console.microservice.layout.template.mediaconsole.mvp.MenuBarView;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Optional;

public abstract  class HeaderMenuRouteLayout extends MediaConsoleRouteLayout {

    @Autowired
    private HeaderView headerView;

    @Autowired
    private MenuBarView menuBarView;

    @PostConstruct
    protected void setupHeaderMenu() {
        menuBarView.setLogoFeature(Optional.ofNullable(getRouteLogoPath()).isPresent(), getRouteLogoPath());
        menuBarView.setActionFeature(Optional.ofNullable(getActionImagePath()).isPresent(), getActionImagePath());
        addHeaderView(menuBarView);
        headerView.setLogoFeature(Optional.ofNullable(getLogoPath()).isPresent(), getLogoPath());
        headerView.setStoryTitleFeature(Optional.ofNullable(getI18nStoryTitle()).isPresent(), getI18nStoryTitle());
        addHeaderView(headerView);
    }

    protected void addMenuBarView(FlowView flowView) {
        addMenuBar( (Component) flowView);
    }

    protected void addMenuBar(Component component) {
        menuBarView.addComponentAsFirst( component);
    }

    protected void addRouteTab(Tab routeTab) {
        headerView.addRouteTab(routeTab);
    }

    protected void addRouteTab(Tab routeTab, Class<? extends Component> route) {
        routeTab.getElement().addEventListener("click", e -> JsUtil.execute("window.location = $0;", RouteConfiguration.forApplicationScope().getUrl(route)));
        addRouteTab(routeTab);
    }

    protected void selectRouteTab(Tab routeTab) {
        headerView.selectRouteTab(routeTab);
    }

    protected void setMenuBarJustifyContentMode(JustifyContentMode justifyContentMode){
        menuBarView.setJustifyContentMode(justifyContentMode);
    }

    protected abstract String getLogoPath();

    protected abstract String getI18nStoryTitle();

    protected abstract String getActionImagePath();

    protected abstract String getRouteLogoPath();

}
