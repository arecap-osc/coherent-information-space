package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.roadmanager;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.CustomSaveDialog;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class RoadEditorDialogView extends CustomSaveDialog implements FlowEntityView {
	@Autowired
    private RoadEditorDialogPresenter presenter;

    private Grid<Road> roadGrid = new Grid<>(Road.class);

    private TextField filter = new TextField();

    private Button addNewBtn = new Button("button.create.label", VaadinIcon.PLUS.create());
    private Button confirmBtn = new Button("button.confirm.label", VaadinIcon.CHECK.create());
    private Button closeBtn = new Button("button.close.label", VaadinIcon.CLOSE.create());

    private TextField networkTextField = new TextField("Retea");
    private TextField nameTextField = new TextField("Nume");
    private TextField groupNameTextField = new TextField("Denumire grup");
    private TextField roadTextField = new TextField("Drum");
    private TextField excludeClusterTextField = new TextField("Exclude cluster");
    private TextField fractolonTextField = new TextField("Fractal algebric");
    private TextField orderTextField = new TextField("Ordine");

    private Button save = new Button("button.save.label", VaadinIcon.CHECK.create());
    private Button cancel = new Button("button.cancel.label");
    private Button delete = new Button("button.delete.label", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private VerticalLayout roadEditorForm = new VerticalLayout(new VerticalLayout( new HorizontalLayout(networkTextField, groupNameTextField, nameTextField, orderTextField),
    		 																		new HorizontalLayout(roadTextField, excludeClusterTextField, fractolonTextField)), 
    																			actions);
    
    private Road roadFromParent = null;
    private Road groupNameRoadComboboxParent = null;
    private boolean showEditEntityForm = false;
    private boolean editFractoloni = false;

    public void open(final Road groupNameRoadCombobox, final Road valuesFromParentForm, final String currentNetwork, boolean showEditEntityForm, boolean editFractoloni) {
    	this.open();

    	this.showEditEntityForm = showEditEntityForm;
    	this.editFractoloni = editFractoloni;
    	
    	if (groupNameRoadCombobox!=null) {
    		this.groupNameRoadComboboxParent = groupNameRoadCombobox.duplicate();
    		this.roadFromParent = groupNameRoadCombobox.duplicate();
    		
    		if (valuesFromParentForm!=null) {
	    		//update form fields
	//    		this.roadFromParent.setFromNode(valuesFromParentForm.getFromNode());
	//    		this.roadFromParent.setToNode(valuesFromParentForm.getToNode());
	//    		this.roadFromParent.setIncludeNodes(valuesFromParentForm.getIncludeNodes());
	    		this.roadFromParent.setExcludeClusters(valuesFromParentForm.getExcludeClusters());
	    		this.roadFromParent.setRoad(valuesFromParentForm.getRoad());
    		}
    	}else {
    		if (valuesFromParentForm!=null) {
    			this.roadFromParent = valuesFromParentForm.duplicate();
    			this.roadFromParent.setNetwork(currentNetwork);
    		}
    	}
    	
    	//continue in afterPrepareView()
    }
    
    @Override
    public void buildView() {
        FlowEntityView.super.buildView();
        setWidth("1300px");
        buildViewWrapper();
        buildEventListeners();
    }

    private void buildViewWrapper() {
        roadEditorForm.setVisible(false);

        filter.setPlaceholder("Filter by label");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        roadGrid.setColumns("id", "network", "groupName", "name", "road", "excludeClusters", "fractolon", "orderPosition");
        roadGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        roadGrid.setItems(getPresenter().getService().getAllRoads());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        if (this.editFractoloni) {
        	actions.add(confirmBtn);
        }
        actions.add(closeBtn);
        add(actions, roadGrid, roadEditorForm);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
    }

    private void buildEventListeners() {
        addNewBtn.addClickListener(getPresenter()::handleAddNewEvent);
        filter.addValueChangeListener(getPresenter()::handleFilterValueChangeEvent);
        delete.addClickListener(getPresenter()::handleDeleteEvent);
        cancel.addClickListener(getPresenter()::handleCancelEvent);
        roadGrid.addSelectionListener(getPresenter()::handleRoadSelectionEvent);
    }


    @Override
    public void afterPrepareView() {
    	if (showEditEntityForm) {
    		if (groupNameRoadComboboxParent != null) {
    			//select value in grid
    			getRoadGrid().select(groupNameRoadComboboxParent);
    		}
    		getPresenter().handleClickSaveNewEntityFromParent();
    	}
    	
    	if (editFractoloni) {
    		getFractolonTextField().setVisible(true);
    		getRoadGrid().getColumnByKey("fractolon").setVisible(true);
    	} else {
    		getFractolonTextField().setVisible(false);
    		getRoadGrid().getColumnByKey("fractolon").setVisible(false);
    	}
    }
    
    @Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }

    @Override
    public RoadEditorDialogPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    public VerticalLayout getRoadEditorForm() {
        return roadEditorForm;
    }

    public Grid<Road> getRoadGrid() {
        return roadGrid;
    }

	public TextField getNetworkTextField() {
		return networkTextField;
	}

	public TextField getNameTextField() {
		return nameTextField;
	}

	public TextField getGroupNameTextField() {
		return groupNameTextField;
	}

	public TextField getRoadTextField() {
		return roadTextField;
	}

	public TextField getExcludeClusterTextField() {
		return excludeClusterTextField;
	}

	public TextField getFractolonTextField() {
		return fractolonTextField;
	}
    
	public TextField getOrderTextField() {
		return orderTextField;
	}
	
	public Road getRoadFromParent() {
		return roadFromParent;
	}
	
//	public boolean getShowEditEntityForm() {
//		return showEditEntityForm;
//	}
//	
//	public Road getGroupNameRoadComboboxParent() {
//		return groupNameRoadComboboxParent;
//	}

	@Override
	protected boolean checkFields() {
		return true;
	}

	@Override
	public Button getConfirmButton() {
		return confirmBtn;
	}
	
	@Override
	public Button getCloseButton() {
		return closeBtn;
	}
    
	@Override
    public ComponentEvent<Dialog> getSaveEvent() {
		if (getRoadGrid().getSelectionModel().getFirstSelectedItem().isPresent()) {
			return new RoadEditorDialogViewEvent(this, getRoadGrid().getSelectionModel().getFirstSelectedItem().get());
		}
		return null;
    }
    
    public static class RoadEditorDialogViewEvent extends ComponentEvent<Dialog> {
    	private Road selectedRoad;
    	
        public RoadEditorDialogViewEvent(Dialog source, Road selectedRoad) {
			super(source, true);
			this.selectedRoad = selectedRoad;
		}

		public Road getSelectedRoad() {
			return selectedRoad;
		}
    }
}
