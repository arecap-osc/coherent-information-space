package org.arecap.eden.ia.console.microservice.layout.template;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.arecap.eden.ia.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class LeftBarView extends VerticalLayout implements FlowView<LeftBarPresenter> {

    @Autowired
    private LeftBarPresenter presenter;

    @Autowired
    private LeftBarUpperSideView upperSideView;

    @Autowired
    private LeftBarDownSideView downSideView;

    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

    @Override
    public LeftBarPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void buildView() {
        setSpacing(false);
        setPadding(false);
        setHeightFull();
        setWidth("65px");
        getStyle().set("border","0px 0px 0px 0px");
        getStyle().set("border-right", "1px solid black");
        getStyle().set("background-color", "#264c6c");//"#919191");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//        add(getImageLogo());
        add(upperSideView, downSideView);
    }

//    private Image getImageLogo() {
//        Image logo = new Image("/frontend/img/hkrdilogo_white.jpg","Human Knowledge Research and Development Institute");
////        logo.getStyle().set("margin-top", "5px");
////        logo.setHeight("50px");
////        logo.getStyle().set("position", "absolute");
////        logo.getStyle().set("right", "10px");
//        
////        logo.setWidth("40px");
////        logo.setHeight("40px");
////        logo.getStyle().set("margin-top", "5px");
////        logo.getStyle().set("margin-left", "10px");
////        logo.getStyle().set("border-radius", "50%");
//        
//        
//        logo.getStyle().set("margin", "6px");
//        logo.getStyle().set("border-radius", "50%");
//        logo.setWidth("50px");
//        logo.setHeight("50px");
//        logo.getElement().setAttribute("title", "Human Knowledge Research and Development Institute");
//        return logo;
//    }

}
