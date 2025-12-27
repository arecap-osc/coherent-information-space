package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions.DimenssionImageGenerator;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowEntityView;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
@HtmlImport("frontend://feedbacks/FeedbackDataMapEditViewDesign.html")
@Tag("feedbackdatamap-edit-view")
public class FeedbackDataMapEditView extends PolymerTemplate<TemplateModel> 
						implements FlowEntityView{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private FeedbackDataMapEditPresenter presenter;
	
	@Autowired
	private DimenssionImageGenerator dimenssionImageGenerator;

	@Id("top-layout-with-images")
	private HorizontalLayout topLayoutWithImages;
	
	@Id("save")
	private Button save;
	
	@Id("previous")
	private Button previousButton;
	
	@Id("next")
	private Button nextButton;

	@Id("cancel")
	private Button cancelButton;
	
	private Dialog dialog;
	
	@Id("inOutSemantic")
	private TextArea inOutSemantic;

	@Id("outInSemantic")
	private TextArea outInSemantic;

	@Id("completeSemantic")
	private TextArea completeSemantic;

	@Id("colrow")
	private Label colrow;
	
	@Id("iesireDate")
	private Label iesireDate;

	@Id("procesareDate")
	private Label procesareDate;

	@Id("bazeStrategii")
	private Label bazeStrategii;

	@Id("intrareDate")
	private Label intrareDate;

	@Id("evaluareRaspunsuri")
	private Label evaluareRaspunsuri;

	@Id("bazeExperiente")
	private Label bazeExperiente;

	@Id("iesireDate2")
	private Label iesireDate2;
	
	@Id("iesireDate3")
	private Label iesireDate3;

	@Id("procesareDate2")
	private Label procesareDate2;

	@Id("bazeStrategii2")
	private Label bazeStrategii2;

	@Id("intrareDate2")
	private Label intrareDate2;

	@Id("intrareDate3")
	private Label intrareDate3;
	
	@Id("evaluareRaspunsuri2")
	private Label evaluareRaspunsuri2;

	@Id("bazeExperiente2")
	private Label bazeExperiente2;
	
	
	@Override
	public void buildView() {
		//to add parent buttons listeners
		FlowEntityView.super.buildView();
		
		cancelButton.addClickListener(e->presenter.onCancel());

		nextButton.addClickListener(e -> {
			getPresenter().onNext();
		});

		previousButton.addClickListener(e -> {
			getPresenter().onPrevious();
		});

	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowEntityView.super.localeChange(event);
		save.setText(GgmI18NProviderStatic.getTranslation("button.save.label"));
		cancelButton.setText(GgmI18NProviderStatic.getTranslation("button.cancel.label"));
		try{
			afterPrepareView();
		}catch(Throwable t) {
			//silent
		}
	}
	
	@Override
	public void afterPrepareView() {
		FlowEntityView.super.afterPrepareView();
		
		FeedbackDataMap entity = getPresenter().getEntity();
		
		colrow.setText(GgmI18NProviderStatic.getTranslation("msg.feedback.datamap.edit.colrow.title", 
				entity.getColumn()+1,entity.getRow()+1, entity.getFeedbackPositionAsString(), feedbackSelection.getUnicursalDataMap().getSemantic()));
//		colrow.setText("COLOANA "+(entity.getColumn()+1)+" - RAND "+ (entity.getRow()+1) +" - ETAPA "+entity.getFeedbackPositionAsString()+": "+feedbackSelection.getUnicursalDataMap().getSemantic());

		iesireDate.setText(entity.getIesireDate());
		iesireDate2.setText(entity.getIesireDate());
		iesireDate3.setText(entity.getIesireDate());
		
		procesareDate.setText(entity.getProcesareDate());
		procesareDate2.setText(entity.getProcesareDate());
		
		bazeStrategii.setText(entity.getBazeStrategii());
		bazeStrategii2.setText(entity.getBazeStrategii());
		
		intrareDate.setText(entity.getIntrareDate());
		intrareDate2.setText(entity.getIntrareDate());
		intrareDate3.setText(entity.getIntrareDate());
		
		evaluareRaspunsuri.setText(entity.getEvaluareRaspunsuri());
		evaluareRaspunsuri2.setText(entity.getEvaluareRaspunsuri());
		
		bazeExperiente.setText(entity.getBazeExperiente());
		bazeExperiente2.setText(entity.getBazeExperiente());
		
		inOutSemantic.setSizeFull();
		outInSemantic.setSizeFull();
		completeSemantic.setSizeFull();
		
		nextButton.setEnabled(getPresenter().hasNext());
		previousButton.setEnabled(getPresenter().hasPrevious());
		
		drawImages();
	}

	private void drawImages() {
		FeedbackDataMap entity =getPresenter().getEntity();

		String IMAGE_UNICURSAL_PREFIX = "frontend/img/dim_0";
		String IMAGE_DIAGRAM_PREFIX = "frontend/img/diag_0";
		String IMAGE_SUFIX = ".png";
		
		int cellWidth = 240;
		int cellHeight = 130;
		
		topLayoutWithImages.setSpacing(false);
		topLayoutWithImages.setPadding(false);
		topLayoutWithImages.removeAll();
		
		topLayoutWithImages.add(
				dimenssionImageGenerator.generateCellForDisplayWithCCCDCUCAndExplanation(
						entity.getRow(), entity.getColumn(), topLayoutWithImages,
						entity.getIesireDate(), entity.getProcesareDate(), entity.getBazeStrategii(),
						entity.getIntrareDate(), entity.getEvaluareRaspunsuri(), entity.getBazeExperiente(),
						GgmI18NProviderStatic.getTranslation(
								UnicursalDataMapService.getDimensionDefinition(entity.getColumn())),
						true
						),

				dimenssionImageGenerator.generateCellForExplanation(false, 
						entity.getColumn(), 
						"", 
						UnicursalDataMapService.getUnicursalDefinition(entity.getColumn()),
//						entity.getUnicursal(),
						IMAGE_DIAGRAM_PREFIX+entity.getColumn()+IMAGE_SUFIX, cellWidth, cellHeight, false),
				
				dimenssionImageGenerator.generateCellForExplanation(false, 
						entity.getColumn(), 
						null, 
						UnicursalDataMapService.getUnicursalPurpleDefinition(entity.getColumn()),
//						entity.getUnicursalPurple(),
						IMAGE_DIAGRAM_PREFIX+entity.getColumn()+"_1"+IMAGE_SUFIX, cellWidth, cellHeight, false),
				
				dimenssionImageGenerator.generateCellForExplanation(false, 
						entity.getColumn(), 
						null, 
						UnicursalDataMapService.getUnicursalGreenDefinition(entity.getColumn()),
//						entity.getUnicursalGreen(),
						IMAGE_DIAGRAM_PREFIX+entity.getColumn()+"_2"+IMAGE_SUFIX, cellWidth, cellHeight, false));

	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	
	@Override
	public FeedbackDataMapEditPresenter getPresenter() {
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

	public Button getCancelButton() {
		return cancelButton;
	}

	public Dialog getDialog() {
		return dialog;
	}

}

