package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.calculated;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.RoadOptionalPaneView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@HtmlImport("frontend://styles/shared-styles.html")
public class RoadCalcualtedRoadOptionalPaneView extends RoadOptionalPaneView {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadCalcualtedRoadOptionalPaneView.class);

    @Autowired
    private RoadCalcualtedRoadOptionalPanePresenter presenter;

    private TextField fromNodeTextField = new TextField("road.textfield.fromnode");//"From");
    private TextField toNodeTextField = new TextField("road.textfield.tonode");//"To");
    private TextField includeNodesTextField = new TextField("road.textfield.includenodes");//"Through");
    private TextField excludeClustersTextField = new TextField("road.textfield.excludeclusters");//"No clusters");
    
    private TextField searchSyntax = new TextField("road.textfield.withsynta");//"Syntax");
    
    @Override
    public void buildViewWrapper() {
//    	getStyle().set("overflow", "auto");
    	
        VerticalLayout vertical = new VerticalLayout();
        vertical.getStyle().set("border-bottom", "1px solid black");
        
        //road form
        VerticalLayout layoutFormRoad = new VerticalLayout();
        layoutFormRoad.setSpacing(false);
        layoutFormRoad.setPadding(false);
        layoutFormRoad.setMargin(false);
        layoutFormRoad.setWidthFull();
        
        HorizontalLayout hlR0 = new HorizontalLayout();
        hlR0.setWidthFull();
        hlR0.setDefaultVerticalComponentAlignment(Alignment.END);
//        hlR0.setPadding(false);
//        hlR0.setMargin(false);
		
        getGroupNameRoadComboBox().setWidth("300px");
        hlR0.add(getGroupNameRoadComboBox(), getRoadTextField(), getRoadEditRoadsButton());
        
        getRoadTextField().setVisible(false);
        
        HorizontalLayout hlR1 = new HorizontalLayout();
        hlR1.getStyle().set("border-top", "1px solid black");
        hlR1.setWidthFull();
        hlR1.setPadding(false);
        hlR1.setMargin(false);
        fromNodeTextField.setWidth("60px");
        toNodeTextField.setWidth("60px");
        getIncludeNodesTextField().setWidth("90px");getIncludeNodesTextField().setPlaceholder("Ex: 6,3,24");
        excludeClustersTextField.setWidth("90px");excludeClustersTextField.setPlaceholder("Ex: 14,5,4");
//        searchSyntax.setWidth("60px");
        hlR1.add(fromNodeTextField, toNodeTextField, getIncludeNodesTextField(), excludeClustersTextField);//, searchSyntax);
        
        layoutFormRoad.add(hlR0, hlR1);
        vertical.add(layoutFormRoad);;
        //
        
        //road
        HorizontalLayout hlRoad = new HorizontalLayout();
        getRoadComboBox().setWidth("150px");
        getRoadComboBox().setAllowCustomValue(false);
        getRoadComboBox().setItemLabelGenerator(r->//r.getName()!=null && !"".equals(r.getName().trim())?r.getName():r.getRoad());
        											r.getRoad());//!=null? r.getRoad():"");

        getDownloadAnchor().getElement().setAttribute("download", true);
        Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        getDownloadAnchor().add(downloadButton);
//        downloadButton.addClickListener(l->presenter.handleExport(roadComboBox.getValue()));
//        add(getDownload());
        
        hlRoad.add(getRoadComboBox(), getRoadComputeButton(), getRoadClearRoadsButton(), getDownloadAnchor(), getRoadSaveRoadsButton());
        hlRoad.setWidthFull();
        //
        
        vertical.add(hlRoad);
        add(vertical);
        
        //add grid
        initSemanticGrid();
        VerticalLayout verticalGrid = new VerticalLayout();
        verticalGrid.add(getGrid());
        verticalGrid.setHeight("500px");
        verticalGrid.getStyle().set("border-bottom", "1px solid black");
        add(verticalGrid);
        
        //add phrase
        VerticalLayout verticalPhrase = new VerticalLayout();
        verticalPhrase.setHeight("250px");
        getNode1SemanticLabel().getElement().getStyle().set("color", "red");
        getNode1Node2SyntaxLabel().getElement().getStyle().set("color", "green");
        getNode2SemanticLabel().getElement().getStyle().set("color", "blue");
        getAttachedDocumentsLayout().getStyle().set("overflow", "auto");
        verticalPhrase.add(getNode1SemanticLabel(), getNode1Node2SyntaxLabel(), getNode2SemanticLabel(), getAttachedDocumentsLayout());
//        verticalPhrase.setHeight("250px");
//        verticalPhrase.setSizeUndefined();
//        verticalPhrase.getStyle().set("display","block");
        add(verticalPhrase);
        
//        setHeight("100%");
    }
    
	public TextField getFromNodeTextField() {
		return fromNodeTextField;
	}

	public TextField getToNodeTextField() {
		return toNodeTextField;
	}
	
	public TextField getIncludeNodesTextField() {
		return includeNodesTextField;
	}

	public TextField getExcludeClustersTextField() {
		return excludeClustersTextField;
	}

	public TextField getSearchSyntax() {
		return searchSyntax;
	}

    @Override
    public RoadCalcualtedRoadOptionalPanePresenter getPresenter() {
        return presenter;
    }

	@Override
	public boolean showFractolon() {
		return false;
	}
}
