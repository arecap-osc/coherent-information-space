package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.fractolonview.media;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.featureflag.FeatureFlags;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.RoadOptionalPaneView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@HtmlImport("frontend://styles/shared-styles.html")
public class RoadFractoloniViewOptionalPaneView extends RoadOptionalPaneView {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadFractoloniViewOptionalPaneView.class);

    @Autowired
    private RoadFractoloniViewOptionalPanePresenter presenter;

    private ComboBox<String> groupComboBox = new ComboBox<>("Fractolon");
    private TextField fractolon = new TextField("Nume drum");

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
		
        getGroupNameRoadComboBox().setWidth("270px");
		getGroupComboBox().setWidth("135px"); 
		getRoadComboBox().setWidth("135px");
		getGroupNameRoadComboBox().setVisible(false);
		getFractolonTextField().setVisible(false);
		getRoadComboBox().setLabel("Nume drum");
        hlR0.add(getGroupComboBox(), getRoadComboBox(), getGroupNameRoadComboBox(), getFractolonTextField());
        //TODO should have edit permision
        if (FeatureFlags.ROAD_NAVIGATOR.check()) {
        	hlR0.add(getRoadEditRoadsButton());
        }

        HorizontalLayout hlR1 = new HorizontalLayout();
        hlR1.getStyle().set("border-top", "1px solid black");
        hlR1.setWidthFull();
        hlR1.setPadding(false);
        hlR1.setMargin(false);
        hlR1.setDefaultVerticalComponentAlignment(Alignment.END);
        getRoadTextField().setWidthFull();
        getRoadTextField().setPlaceholder("Ex: 6,3,24");
        getRoadTextField().setEnabled(false);
        
        getDownloadAnchor().getElement().setAttribute("download", true);
        Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());
        getDownloadAnchor().add(downloadButton);
        
        hlR1.add(getRoadTextField(), getDownloadAnchor());
        
        layoutFormRoad.add(hlR0, hlR1);
        vertical.add(layoutFormRoad);;
        //
        
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
        getNode1SemanticLabel().getElement().getStyle().set("color", "red");
        getNode1Node2SyntaxLabel().getElement().getStyle().set("color", "green");
        getNode2SemanticLabel().getElement().getStyle().set("color", "blue");

        getAttachedDocumentsLayout().getStyle().set("overflow", "auto");
        verticalPhrase.add(getNode1SemanticLabel(), getNode1Node2SyntaxLabel(), getNode2SemanticLabel(), getAttachedDocumentsLayout());
        add(verticalPhrase);
    }

    public ComboBox<String> getGroupComboBox() {
		return groupComboBox;
	}

	public TextField getFractolonTextField() {
		return fractolon;
	}

	@Override
    public RoadFractoloniViewOptionalPanePresenter getPresenter() {
        return presenter;
    }

	@Override
	public boolean showFractolon() {
		return true;
	}

}
