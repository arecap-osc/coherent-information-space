package org.arecap.eden.ia.console.mvp.component.verticallayout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import org.arecap.eden.ia.console.mvp.FlowEntityPresenter;
import org.arecap.eden.ia.console.mvp.component.HasNew;
import org.arecap.eden.ia.console.mvp.component.HasSelection;

import javax.annotation.PostConstruct;
import java.util.Collection;

public abstract class VerticalLayoutGridCrudFlowEntityView<P extends FlowEntityPresenter,T> extends VerticalLayoutCrudFlowEntityView<P, T> {


    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());

    private HorizontalLayout gridActions = new HorizontalLayout(addNewBtn);

    private Grid<T> crudGrid;

    @PostConstruct
    public void init() {
        crudGrid = new Grid<>(getPresenter().getEntityType());
        super.init();
        crudGrid.setWidthFull();
        gridActions.setWidthFull();
        crudGrid.addSelectionListener(this::gridSelectionChangedFired);
        addNewBtn.addClickListener(this::addNewBtnFired);
        addComponentAsFirst(crudGrid);
        addComponentAsFirst(gridActions);
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

    private void addNewBtnFired(ClickEvent<Button> buttonClickEvent) {
        setEditorFormVisibility(true);
        if(HasNew.class.isAssignableFrom(getPresenter().getClass())) {
            ((HasNew)getPresenter()).createItem();
        }
    }

    public HorizontalLayout getGridActions() {
        return gridActions;
    }

    public Grid<T> getCrudGrid() {
        return crudGrid;
    }

    @Override
    public void setItems(Collection<T> collection) {
        crudGrid.setItems(collection);
    }
}
