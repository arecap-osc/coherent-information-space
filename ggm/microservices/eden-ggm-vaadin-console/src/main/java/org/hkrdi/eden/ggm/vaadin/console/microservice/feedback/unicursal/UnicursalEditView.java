package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions.DimenssionImageGenerator;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

@UIScope
@SpringComponent
@HtmlImport("frontend://feedbacks/UnicursalEditViewDesign.html")
@Tag("unicursal-edit-view")
public class UnicursalEditView extends PolymerTemplate<TemplateModel> 
						implements FlowEntityView{
	private static final long serialVersionUID = 1L;
	
	@Id("top-layout-with-images")
	private HorizontalLayout topLayoutWithImages;
	
	@Id("layout-with-images-semantic-save")
	private HorizontalLayout topLayoutWithImagesSemanticSave;

	@Id("semantic")
	private TextArea semantic;
	
	@Id("save")
	private Button save;
	
	@Autowired
	private UnicursalEditPresenter presenter;
	
	@Autowired
	private DimenssionImageGenerator dimenssionImageGenerator;

	@Override
	public UnicursalEditPresenter getPresenter() {
		return presenter;
	}

	@Override
	public Button getSaveButton() {
		return save;
	}

	@Override
    public void onAttach(AttachEvent attachEvent) {
        FlowEntityView.super.onAttach(attachEvent);
    }
 
	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowEntityView.super.localeChange(event);
		save.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
		try{
			afterPrepareView();
		}catch(Throwable t) {
			//silent
		}
	}
	
	@Override
	public void prepareView() {
		FlowEntityView.super.prepareView();
//		semantic.getStyle().set("width", "100%");	
		topLayoutWithImagesSemanticSave.setSizeFull();
		semantic.setSizeFull();
	}

	@Override
	public void afterPrepareView() {
		String IMAGE_UNICURSAL_PREFIX = "frontend/img/dim_0";
		String IMAGE_DIAGRAM_PREFIX = "frontend/img/diag_0";
		String IMAGE_SUFIX = ".png";
		
		int cellHeight = 120;
		int cellWidth = 200;
		
		topLayoutWithImages.setSpacing(false);
		topLayoutWithImages.setPadding(false);
		topLayoutWithImages.removeAll();
		topLayoutWithImages.add(
				dimenssionImageGenerator.generateCellForExplanation(false, 
						getPresenter().getEntity().getColumn(), 
						null, 
						UnicursalDataMapService.getDimensionDefinition(getPresenter().getEntity().getColumn()),
//						getPresenter().getEntity().getDimenssion(),
						IMAGE_UNICURSAL_PREFIX+getPresenter().getEntity().getColumn()+IMAGE_SUFIX, cellWidth, cellHeight, false),

				dimenssionImageGenerator.generateCellForExplanation(false, 
						getPresenter().getEntity().getColumn(), 
						null, 
						UnicursalDataMapService.getUnicursalDefinition(getPresenter().getEntity().getColumn()),
//						getPresenter().getEntity().getUnicursal(),
						IMAGE_DIAGRAM_PREFIX+getPresenter().getEntity().getColumn()+IMAGE_SUFIX, cellWidth, cellHeight, false),
				
				dimenssionImageGenerator.generateCellForExplanation(false, 
						getPresenter().getEntity().getColumn(), 
						null, 
						UnicursalDataMapService.getUnicursalPurpleDefinition(getPresenter().getEntity().getColumn()),
//						getPresenter().getEntity().getUnicursalPurple(),
						IMAGE_DIAGRAM_PREFIX+getPresenter().getEntity().getColumn()+"_1"+IMAGE_SUFIX, cellWidth, cellHeight, false),
				
				dimenssionImageGenerator.generateCellForExplanation(false, 
						getPresenter().getEntity().getColumn(), 
						null, 
						UnicursalDataMapService.getUnicursalGreenDefinition(getPresenter().getEntity().getColumn()),
//						getPresenter().getEntity().getUnicursalGreen(),
						IMAGE_DIAGRAM_PREFIX+getPresenter().getEntity().getColumn()+"_2"+IMAGE_SUFIX, cellWidth, cellHeight, false));

	}
	
}

