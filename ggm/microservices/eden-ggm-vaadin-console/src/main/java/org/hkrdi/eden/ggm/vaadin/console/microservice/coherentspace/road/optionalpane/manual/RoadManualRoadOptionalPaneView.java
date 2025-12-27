package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.manual;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.RoadOptionalPaneView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@HtmlImport("frontend://styles/shared-styles.html")
public class RoadManualRoadOptionalPaneView extends RoadOptionalPaneView {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadManualRoadOptionalPaneView.class);

    @Autowired
    private RoadManualRoadOptionalPanePresenter presenter;
    
//    private TextField manualNodesTextField = new TextField("Drum");//"Through");

    protected void buildViewWrapper() {
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
        
        HorizontalLayout hlR1 = new HorizontalLayout();
        hlR1.getStyle().set("border-top", "1px solid black");
        hlR1.setWidthFull();
        hlR1.setPadding(false);
        hlR1.setMargin(false);
        hlR1.setDefaultVerticalComponentAlignment(Alignment.END);
        getRoadTextField().setWidth("190px");
        getRoadTextField().setPlaceholder("Ex: 6,3,24");
        getRoadTextField().setEnabled(false);
        
        getDownloadAnchor().getElement().setAttribute("download", true);
        Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        getDownloadAnchor().add(downloadButton);
        
        hlR1.add(getRoadTextField(), getRoadClearRoadsButton(), getDownloadAnchor(), getRoadSaveRoadsButton());
        
        layoutFormRoad.add(hlR0, hlR1);
        vertical.add(layoutFormRoad);;
        //
        
        //road
//        HorizontalLayout hlRoad = new HorizontalLayout();
//        getRoadComboBox().setWidth("250px");
//        getRoadComboBox().setAllowCustomValue(false);
//        getRoadComboBox().setItemLabelGenerator(r->(r.getName()!=null && !"".equals(r.getName().trim()))?
//        		r.getName():
//        			r.getRoad());
//
//        getDownload().getElement().setAttribute("download", true);
//        Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());
//        getDownload().add(downloadButton);
//        downloadButton.addClickListener(l->presenter.handleExport(roadComboBox.getValue()));
        
//        hlRoad.add(getRoadComboBox(), getRoadClearRoadsButton(), getDownload());
//        hlRoad.setWidthFull();
        //
        
//        vertical.add(hlRoad);
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

//	public TextField getManualNodesTextField() {
//		return manualNodesTextField;
//	}


    @Override
    public RoadManualRoadOptionalPanePresenter getPresenter() {
        return presenter;
    }

	@Override
	public boolean showFractolon() {
		return false;
	}
}
