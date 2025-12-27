package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.dimensions;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

@UIScope
@Component
@ParentLayout(MainLayout.class)
@HtmlImport("frontend://feedbacks/DimenssionUnicursalViewDesign.html")
@Tag("dimenssionunicursal-view")
public class DimenssionUnicursalView extends PolymerTemplate<TemplateModel> implements IGgmView{
	@Id("headerContainer")
	protected HorizontalLayout headerContainer;
	
	@Id("footerContainer")
	protected HorizontalLayout footerContainer;
	
	@Id("mainContainer")
	protected VerticalLayout mainContainer;

	@Autowired
	private FeedbackSelection feedbackSelection;
	
	@Autowired
	private DimenssionUnicursalPresenter presenter;
	
	@Override
	public void buildView() {
//		primul.add(new Label("inteligent"));
		mainContainer.add(generateRow(0));
		mainContainer.add(generateRow(1));
		mainContainer.add(generateRow(2));
		mainContainer.add(generateRow(3));
		mainContainer.add(generateRow(4));
		mainContainer.add(generateRow(5));
	}

	private HorizontalLayout generateRow(int i) {
		HorizontalLayout gridRow = new HorizontalLayout();
		gridRow.setClassName("gridRow");
		(new DimenssionGenerator()).generateRow(i, gridRow);
		return gridRow;
	}

	class DimenssionGenerator {
		private static final String IMAGE_PREFIX = "frontend/img/dimenssion_";
		private static final String IMAGE_SUFIX = ".png";
		
		public void generateRow(int row, HorizontalLayout gridRow) {
			for (int col= 0; col<6; col++){
				String imgUrl = "frontend/img/dimenssion_default.png";
				//TODO used until we have all images
				if (row == 0 && col == 0){
					imgUrl = IMAGE_PREFIX+row+col+IMAGE_SUFIX;
				}
				
				Image image = create(imgUrl, "", 10);
				image.getStyle().set("cursor", "pointer");
				int colF = col;
				int rowF = row;
				image.addClickListener(e->{
					gridRow.getUI().ifPresent(ui -> {
						feedbackSelection.setColumn(colF);
						feedbackSelection.setRow(rowF);
						ui.navigate("first-degree-feedback_unicursal_edit");
					});
				});
				gridRow.add(image);
				
//				Button button = new Button("", create(imgUrl, "", 10), e->{
//					gridRow.getUI().ifPresent(ui -> ui.navigate("first-degree-feedback_unicursal_edit"));
//				});
//				button.setWidth("250px");
//				button.setHeight("125px");
//				
//				gridRow.add(button);
			}
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
	}

	@Override
	public IGgmPresenter getPresenter() {
		return presenter;
	}

}
