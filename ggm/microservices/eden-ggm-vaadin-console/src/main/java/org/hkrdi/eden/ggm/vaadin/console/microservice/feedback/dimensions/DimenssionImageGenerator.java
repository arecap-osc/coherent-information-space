package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions;

import java.util.Locale;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.JsUtil;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.UnicursalFeedbackRoute;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DimenssionImageGenerator {

	@Autowired
	private FeedbackDataMapService feedbackService;
	
	@Autowired FeedbackSelectionIe feedbackSelection;
	
	private static final String IMAGE_CELL_PREFIX = "frontend/img/dimenssion_0";
	private static final String IMAGE_CELL_SUFIX = ".png";
	
	private static final String IMAGE_DIMMENSSION_PREFIX = "frontend/img/dim_0";
	private static final String IMAGE_DIMMENSSION_SUFIX = ".png";
	
	private static final String IMAGE_DIAGRAM_PREFIX = "frontend/img/diag_0";
	private static final String IMAGE_DIAGRAM_SUFIX = ".png";
	
	String[] dimTitle = {"msg.unicursal.dimenssions.label", null, null, null, null, null};
	
	String[] diagTitle = {"msg.unicursal.diagrams.label", null, null, null, null, null};
	
	public void generateRowForDisplay(int row, HorizontalLayout gridRow) {
		for (int col= 0; col<6; col++){
			gridRow.add(generateCellForDisplayWithCCCDCUCDimenssion(row, col, gridRow));
			
		}
		
		gridRow.add(create("frontend/img/white.png", "", 20));
		
		gridRow.add(generateCellForDisplayWithExplanation(false, row, dimTitle[row], UnicursalDataMapService.getDimensionDefinition(row), IMAGE_DIMMENSSION_PREFIX+row+IMAGE_DIMMENSSION_SUFIX));
		
		gridRow.add(generateCellForDisplayWithExplanation(true, row, diagTitle[row], UnicursalDataMapService.getUnicursalDefinition(row), IMAGE_DIAGRAM_PREFIX+row+IMAGE_DIAGRAM_SUFIX));
	}
	
	public Component generateCellForDisplayWithExplanation(boolean diagram, int row, String title, String explanation, String imgUrl) {
		return generateCellForExplanation(diagram, row, title, explanation, imgUrl, -1, -1, true);
	}
	
	public Component generateCellForExplanation(boolean diagram, int row, String title, String explanation, 
												String imgUrl, int externalWidth, int externalHeight, boolean withTitle) {
		Locale locale = UI.getCurrent().getLocale();
		title = GgmI18NProviderStatic.getTranslation(title, locale);
		explanation = GgmI18NProviderStatic.getTranslation(explanation, locale);
		VerticalLayout div = new VerticalLayout();
		div.setSpacing(false);
		div.setPadding(false);
		if (externalWidth == -1) {
			div.setWidth(diagram?"280px":"240px");
		}else {
			div.setWidth(externalWidth+"px");
		}
		
		if(externalHeight == -1) {
			div.setHeight("120px");
		}else {
			div.setHeight(externalHeight+"px");
		}
		
		if (withTitle) {
			Label titleLabel = new Label(title == null?".":title);
			titleLabel.getStyle().set("font-size", "16px");
			titleLabel.getStyle().set("font-weight", "bold");
			titleLabel.getStyle().set("color", title == null?"#ffffff":(diagram?"#00A2E8":"#ED1C24"));
			HorizontalLayout hlTop = new HorizontalLayout(titleLabel);
	//		hlTop.setSpacing(false);
			hlTop.setPadding(false);
			hlTop.setWidthFull();
			hlTop.setFlexGrow(1);
			div.add(hlTop);
		}
		
		//
		Image image = create(imgUrl, "", 10);
		div.add(image);
		//
		
		Label explanationLabel = new Label(explanation);
//		explanationLabel.getStyle().set("font-size", "16px");
//		explanationLabel.getStyle().set("font-weight", "bold");
		explanationLabel.getStyle().set("color", diagram?"#00A2E8":"#ED1C24");
		HorizontalLayout hl_bottom = new HorizontalLayout(explanationLabel);
		hl_bottom.setPadding(false);
		hl_bottom.setWidthFull();
		hl_bottom.setFlexGrow(1);
		div.add(hl_bottom);
		return div;
		
	}
	
	public Component generateCellForDisplayWithCCCDCUCDimenssion(int row, int col, HorizontalLayout gridRow) {
		String[] letterLabels = feedbackService.getUnicursalDefaultDefinitionLabel(row, col).split("~");
		for (int i=0;i<letterLabels.length;i++) {
			letterLabels[i] = GgmI18NProviderStatic.getTranslation(letterLabels[i]);
		}
		return generateCellForDisplayWithCCCDCUC(row, col, gridRow, 
				letterLabels[0], letterLabels[1], letterLabels[2],
				letterLabels[3], letterLabels[4], letterLabels[5]);
	}
	
	public Component generateCellForDisplayWithCCCDCUC(int row, int col, HorizontalLayout gridRow, 
			String a1, String a2, String a3,
			String b1, String b2, String b3) {
		return generateCellForDisplayWithCCCDCUCAndExplanation(row, col, gridRow, 
				a1, a2, a3,
				b1, b2, b3, null, false);
	}
	
	public Component generateCellForDisplayWithCCCDCUCAndExplanation(int row, int col, HorizontalLayout gridRow, 
												String a1, String a2, String a3,
												String b1, String b2, String b3, String explanation, boolean isDimmesionImage) {
		VerticalLayout div = new VerticalLayout();
		div.setSpacing(false);
		div.setPadding(false);
		div.setWidth(isDimmesionImage?"240px":"175px");
		div.setHeight(isDimmesionImage?"80px":"120px");
		
		String imgUrl = isDimmesionImage?IMAGE_DIMMENSSION_PREFIX+col+IMAGE_DIMMENSSION_SUFIX:IMAGE_CELL_PREFIX+col+IMAGE_CELL_SUFIX;
		
		HorizontalLayout hlTop = new HorizontalLayout(
				createLabelWithFontMic(a1), new Label(" "), createLabelWithFontMic(a2), new Label(" "), createLabelWithFontMic(a3));
//		hlTop.setSpacing(false);
		hlTop.setPadding(false);
		hlTop.setWidthFull();
		hlTop.setFlexGrow(1);
		
		div.add(hlTop);
		
		//
		Image image = create(imgUrl, "", 20);
		if (!isDimmesionImage) {
			image.getStyle().set("cursor", "pointer");
			int colF = col;
			int rowF = row;
			image.addClickListener(e->{
				gridRow.getUI().ifPresent(ui -> {
					feedbackSelection.setColumn(colF);
					feedbackSelection.setRow(rowF);
					JsUtil.execute("" +
							"window.location = $0;", RouteConfiguration.forApplicationScope().getUrl(UnicursalFeedbackRoute.class));
//					ui.navigate();
//					ui.navigate("first-degree-feedback/unicursal_edit");
				});
			});
		}
		div.add(image);
		//
		
		HorizontalLayout hl_bottom = new HorizontalLayout(createLabelWithFontMic(b1), new Label(" "), createLabelWithFontMic(b2), new Label(" "), createLabelWithFontMic(b3));
		hl_bottom.setPadding(false);
		hl_bottom.setWidthFull();
		hl_bottom.setFlexGrow(1);
		div.add(hl_bottom);
		
		if (explanation != null) {
			div.add(new HorizontalLayout(new Label(explanation)));
		}
		
		return div;
	}

	/**
     * marign right distance if using Icon in Button with Text. so there is space between the icon and the button text
     * @param margin_right
     * @return
     */
    private Image create(String url, String alt, int margin_right) {
		Image image = new Image(url, alt);
        image.getStyle().set("vertical-align", "middle"); // otherwise the icon will be just on the top left corner in the button
        image.getStyle().set("margin-right", margin_right+"px"); //some space between icon and button text
        return image;
    }
    
    private Label createLabelWithFontMic(String text) {
    	Label lA1 = new Label(text);
		lA1.setClassName("font-size-mic");
		
		return lA1;
    }
}
