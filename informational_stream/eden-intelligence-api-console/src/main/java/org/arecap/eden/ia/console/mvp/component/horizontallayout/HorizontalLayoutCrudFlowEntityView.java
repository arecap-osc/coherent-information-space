package org.arecap.eden.ia.console.mvp.component.horizontallayout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.HasItems;
import org.arecap.eden.ia.console.mvp.FlowEntityPresenter;

import javax.annotation.PostConstruct;

public abstract class HorizontalLayoutCrudFlowEntityView<P extends FlowEntityPresenter,T> extends HorizontalLayoutFlowEntityView<P> implements HasItems<T> {

    private Button cancel = new Button("button.cancel.label");
    private Button delete = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(getSaveButton(), cancel, delete);

    @PostConstruct
    public void init() {
        getEditorForm().add(actions);
        super.init();
        actions.setWidthFull();
        cancel.addClickListener(this::cancelBtnFired);
        delete.addClickListener(this::deleteBtnFired);
    }

    private void deleteBtnFired(ClickEvent<Button> buttonClickEvent) {
        getPresenter().delete();
        setEditorFormVisibility(false);
        setItems(getPresenter().getItems());
    }

    private void cancelBtnFired(ClickEvent<Button> buttonClickEvent) {
        setEditorFormVisibility(false);
    }

}
