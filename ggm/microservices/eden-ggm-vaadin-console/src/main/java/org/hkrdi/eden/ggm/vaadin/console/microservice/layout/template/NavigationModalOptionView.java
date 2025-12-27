package org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NavigationModalOptionView extends HorizontalLayout implements FlowView {

    @Autowired
    private NavigationModalOptionPresenter presenter;

    private HorizontalLayout modalContent = new HorizontalLayout();

    private VerticalLayout menuBar = new VerticalLayout();

    private HorizontalLayout menuContent = new HorizontalLayout();

    @Override
    public void buildView() {
        setPadding(false);
        setSizeFull();
        getStyle().set("position", "absolute");
        getStyle().set("z-index", "1000");
        getStyle().set("left", "0px");
        getStyle().set("top", "0px");
        setupModalContent();
        setupModalFrame();
    }

    public void open(Component component) {
        UI.getCurrent().add(this);
        menuContent.add(component);
    }

    public void close() {
        menuContent.removeAll();
        UI.getCurrent().remove(this);
    }

    private void setupModalContent() {
        modalContent.setPadding(false);
        modalContent.getStyle().set("left", "0px");
        modalContent.getStyle().set("top", "0px");
        modalContent.getStyle().set("background-color", "white");
        modalContent.setHeightFull();
        modalContent.setWidth("400px");
        add(modalContent);
        setupMenuBar();
        setupMenuContent();
    }

    private void setupMenuBar() {
        menuBar.setPadding(false);
        menuBar.setHeightFull();
        menuBar.setWidth("65px");
        Button closeBtn = new Button(VaadinIcon.ARROW_LONG_LEFT.create());
        closeBtn.setWidthFull();
        closeBtn.addClickListener(c -> close());
        menuBar.add(closeBtn);
        modalContent.add(menuBar);

    }

    private void setupMenuContent() {
        menuContent.setSizeFull();
        menuContent.setPadding(false);
        modalContent.add(menuContent);
    }

    private void setupModalFrame() {
        HorizontalLayout modalFrame = new HorizontalLayout();
        modalFrame.setPadding(false);
        modalFrame.setSizeFull();
        modalFrame.getStyle().set("background-color", "#ccc");
        modalFrame.getStyle().set("opacity", "0.5");
        modalFrame.getStyle().set("margin", "0px");
        modalFrame.getElement().addEventListener("click", click -> close());
        add(modalFrame);
    }

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public NavigationModalOptionPresenter getPresenter() {
        return presenter;
    }
}
