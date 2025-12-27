package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.route.user.RouteMenu;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.template.ViewMvc;
import com.vaadin.flow.spring.template.annotation.ViewMapping;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

//@GuiController
@Theme(value = Lumo.class)
@PWA(name = "Bookstore Starter", shortName = "Bookstore")
public class MainLayout extends HorizontalLayout implements ViewMvc, RouterLayout {
	
//	@Autowired
//	private RouteMenu menu;
	
	@Autowired
    public MainLayout(RouteMenu menu) {
        setSpacing(false);
        setSizeFull();
        add(menu);
	}
    
//    @ViewMapping
//    public void templateRoute(HasComponents route) {
//        route.add(this);
//        setSpacing(false);
//        setSizeFull();
//
////        this.menu = menu;
////        menu.addView(SampleCrudView.class, SampleCrudView.VIEW_NAME,
////                VaadinIcon.EDIT.create());
////        menu.addView(AboutView.class, AboutView.VIEW_NAME,
////                VaadinIcon.INFO_CIRCLE.create());
//        add(menu);
//    }
}
