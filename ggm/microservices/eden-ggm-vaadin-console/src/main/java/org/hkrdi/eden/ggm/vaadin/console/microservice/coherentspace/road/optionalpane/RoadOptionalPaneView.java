package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.roadmanager.RoadEditorDialogView;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.AttachedDocumentsDialogView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeSemanticBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RoadOptionalPaneView extends VerticalLayout implements FlowEntityView {

    @Autowired
    private RoadEditorDialogView roadEditorDialogView;

    @Autowired
    private AttachedDocumentsDialogView attachedDocumentsDialogView;

    private ComboBox<Road> groupNameRoadComboBox = new ComboBox<>();
    private TextField roadTextField = new TextField("Drum");
    
    private ComboBox<Road> roadComboBox = new ComboBox<>();
    private Button roadComputeButton = new Button(VaadinIcon.REFRESH.create());
    private Button roadClearRoadsButton = new Button(VaadinIcon.DEL.create());
    private Button roadSaveRoadsButton = new Button(VaadinIcon.DISC.create());
    private Button roadEditRoadsButton = new Button(VaadinIcon.MENU.create());
    
    private Grid<EdgeSemanticBean> grid = new Grid<>();//EdgeSemanticBean.class); !!!! Automatically adds header
    
    private Label node1SemanticLabel = new Label();
    private Label node1Node2SyntaxLabel = new Label();
    private Label node2SemanticLabel = new Label();
    
    private VerticalLayout attachedDocumentsLayout = new VerticalLayout();

    private Anchor downloadAnchor = new Anchor();

	@Override
    public void buildView() {
//		FlowEntityView.super.buildView();

        customizeView();
        buildEventListeners();
        buildViewWrapper();
    }

	private void customizeView() {
        setWidth("350px");
        getStyle().set("border-right", "1px solid black");
        setPadding(false);
        setSpacing(false);

//        getStyle().set("border-bottom", "1px solid black");
    }

    private void buildEventListeners() {
        roadComputeButton.addClickListener(e -> getPresenter().handleComputeRoad(e));
        roadClearRoadsButton.addClickListener(e -> getPresenter().handleClearAllRoads(e));
        roadEditRoadsButton.addClickListener(e -> roadEditorDialogView.open(
        												this.getGroupNameRoadComboBox().getValue(),
        												null,
        												getPresenter().getStateManager().getCurrentNetwork(),
        												false,
        												showFractolon()));

        groupNameRoadComboBox.addValueChangeListener(e-> {
        	if (e.getValue()!=null && e.isFromClient()) {
        		getPresenter().handleGroupNameRoadSelection(e.getValue());
        	}
        });
        
//        roadComboBox.addValueChangeListener(e-> {
//        	if (e.getValue()!=null) {
//        		getPresenter().handleRoadSelection(e.getValue());	
//        	}
//        });
        
        grid.addSelectionListener(edgeSemanticBean -> {
        	if (edgeSemanticBean.getFirstSelectedItem().isPresent()){
        		getPresenter().handleGridSelection(edgeSemanticBean);
    		}
        });

        roadEditorDialogView.setConfirmationFunctionAndPrepareView(this::onConfirmationSave);
        getSaveButton().addClickListener(e -> {
        	if (this.getGroupNameRoadComboBox().getValue()!=null) {//this.getPresenter().getEntity()!=null && this.getPresenter().getEntity().getId()!=null) {
        		roadEditorDialogView.open(this.getGroupNameRoadComboBox().getValue(),
        									this.getPresenter().getEntity(),
        									getPresenter().getStateManager().getCurrentNetwork(),
        									true,
        									showFractolon());
        	}else {
        		//new entity
        		roadEditorDialogView.open(null,
        									this.getPresenter().getEntity(),
        									getPresenter().getStateManager().getCurrentNetwork(),
        									true,
        									showFractolon());
        	}
//			new RoadNameSaveDialog(this::onConfirmationSave)
//										.open(this.getPresenter().getEntity().getName(),
//												this.getPresenter().getEntity().getGroupName());
		});

//		getSaveButton().addClickListener(e -> getPresenter().save());

    }

    private void onConfirmationSave(ComponentEvent<Dialog> event) {
    	getPresenter().onConfirmationSave(event);
    }

    public Anchor getDownloadAnchor() {
        return downloadAnchor;
    }
    
    protected abstract void buildViewWrapper();
    
	protected void initSemanticGrid() {
//    	grid.setHeightByRows(true);
    	grid.setHeight("500px");
    	grid.setSizeFull();
    	grid.setThemeName("wrap-cell-content");
//        grid.addColumn(edgeSemanticBean -> (edgeSemanticBean.getEdge().getToNode() != null ?
//                "(" + edgeSemanticBean.getEdge().getFromNode().getAddressIndex() + "-" + edgeSemanticBean.getEdge().getToNode().getAddressIndex() + ") " :
//                "(" + edgeSemanticBean.getEdge().getFromNode().getAddressIndex() + ") ") +
//                edgeSemanticBean.getSyntax());	//.setHeader("Semantica");

        grid.addColumn(new ComponentRenderer<>(
                edgeSemanticBean -> {
                    HorizontalLayout hl = new HorizontalLayout(
                            getPresenter().getEdgeSemanticBeanText(edgeSemanticBean),
                            getPresenter().getAttachmentsButton(edgeSemanticBean));
                    hl.setDefaultVerticalComponentAlignment(Alignment.CENTER);
                    return hl;
                }));
    	grid.setClassNameGenerator(edgeSemanticBean -> {
    	    if (edgeSemanticBean.getEdge().getToNode()!=null) {
    	       return "syntax";
    	    } else {
    	       return "semantic";
    	    }
    	});
    }

	public ComboBox<Road> getRoadComboBox() {
		return roadComboBox;
	}

    public Grid<EdgeSemanticBean> getSemanticGrid() {
		return grid;
	}

	public Label getNode1SemanticLabel() {
		return node1SemanticLabel;
	}

	public Label getNode1Node2SyntaxLabel() {
		return node1Node2SyntaxLabel;
	}

	public Label getNode2SemanticLabel() {
		return node2SemanticLabel;
	}
	
	public Button getRoadComputeButton() {
		return roadComputeButton;
	}
	
	public Button getRoadClearRoadsButton() {
		return roadClearRoadsButton;
	}
	
	public Button getRoadSaveRoadsButton() {
		return roadSaveRoadsButton;
	}
	
	public Button getRoadEditRoadsButton() {
		return roadEditRoadsButton;
	}

	public TextField getRoadTextField() {
		return roadTextField;
	}

	public ComboBox<Road> getGroupNameRoadComboBox() {
		return groupNameRoadComboBox;
	}

	public Grid<EdgeSemanticBean> getGrid() {
		return grid;
	}

    public AttachedDocumentsDialogView getAttachedDocumentsDialogView() {
        return attachedDocumentsDialogView;
    }

    public VerticalLayout getAttachedDocumentsLayout() {
        return attachedDocumentsLayout;
    }

	@Override
	public void onAttach(AttachEvent attachEvent) {
		FlowEntityView.super.onAttach(attachEvent);
	}

	@Override
	public Button getSaveButton() {
		return roadSaveRoadsButton;
	}

	public abstract RoadOptionalPanePresenter getPresenter();

	public abstract boolean showFractolon();
}
