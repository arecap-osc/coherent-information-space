package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.dimensions;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
//@ParentLayout(TemplateRouterLayout.class)
@HtmlImport("frontend://feedbacks/DimenssionUnicursalViewDesign.html")
@Tag("dimenssionunicursal-view")
public class DimenssionUnicursalView extends PolymerTemplate<TemplateModel> implements FlowView{
	@Id("headerContainer")
	protected HorizontalLayout headerContainer;
	
	@Id("footerContainer")
	protected HorizontalLayout footerContainer;
	
	@Id("mainContainer")
	protected VerticalLayout mainContainer;

	@Autowired
	private DimenssionUnicursalPresenter presenter;
	

	@Autowired
	private DimenssionImageGenerator dimenssionImageGenerator;
	
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
	
	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowView.super.localeChange(event);
		mainContainer.removeAll();
		buildView();
	}

	private HorizontalLayout generateRow(int i) {
		HorizontalLayout gridRow = new HorizontalLayout();
//		gridRow.setClassName("gridRow");
		gridRow.setSpacing(false);
		gridRow.setPadding(false);
		dimenssionImageGenerator.generateRowForDisplay(i, gridRow);
		return gridRow;
	}

	@Override
	public DimenssionUnicursalPresenter getPresenter() {
		return presenter;
	}

	@Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }
}
