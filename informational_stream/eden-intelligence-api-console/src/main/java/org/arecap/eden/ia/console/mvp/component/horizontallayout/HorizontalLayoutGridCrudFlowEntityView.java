package org.arecap.eden.ia.console.mvp.component.horizontallayout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import org.arecap.eden.ia.console.mvp.FlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.component.HasNew;
import org.arecap.eden.ia.console.mvp.component.HasSelection;

import javax.annotation.PostConstruct;
import java.util.Collection;

public abstract class HorizontalLayoutGridCrudFlowEntityView<P extends FlowEntityPresenter,T> extends HorizontalLayoutCrudFlowEntityView<P, T> {


    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private HorizontalLayout gridActions = new HorizontalLayout(addNewBtn);

    private VerticalLayout gridLayout = new VerticalLayout();

    private Grid<T> crudGrid;

    @PostConstruct
    public void init() {
        crudGrid = new Grid<>(getPresenter().getEntityType());
        gridLayout.setHeightFull();
        gridLayout.getStyle().set("overflow","auto");
        gridLayout.add(gridActions, crudGrid);
        super.init();
        gridActions.setWidthFull();
        crudGrid.addSelectionListener(this::gridSelectionChangedFired);
        addNewBtn.addClickListener(this::addNewBtnFired);
        addComponentAsFirst(gridLayout);
    }

    private void addNewBtnFired(ClickEvent<Button> buttonClickEvent) {
        setEditorFormVisibility(true);
        if(HasNew.class.isAssignableFrom(getPresenter().getClass())) {
            ((HasNew)getPresenter()).createItem();
        }
    }

    private void gridSelectionChangedFired(SelectionEvent<Grid<T>, T> gridTSelectionEvent) {
        setEditorFormVisibility(gridTSelectionEvent.getFirstSelectedItem().isPresent());
        if(gridTSelectionEvent.getFirstSelectedItem().isPresent()) {
            getPresenter().setEntity(gridTSelectionEvent.getFirstSelectedItem().get());
        }
        if(HasSelection.class.isAssignableFrom(getPresenter().getClass())) {
            ((HasSelection)getPresenter()).publishSelection(gridTSelectionEvent.getFirstSelectedItem());
        }
    }

    public HorizontalLayout getGridActions() {
        return gridActions;
    }

    public VerticalLayout getGridLayout() {
        return gridLayout;
    }

    public Grid<T> getCrudGrid() {
        return crudGrid;
    }

    @Override
    public void setItems(Collection<T> collection) {
        crudGrid.setItems(collection);
    }
}
