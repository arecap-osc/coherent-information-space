package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

@UIScope
@Component
@ParentLayout(MainLayout.class)
@HtmlImport("frontend://feedbacks/UnicursalMainViewDesign.html")
@Tag("unicursal-main-view")
//@Uses(UnicursalGridView.class)
//@Uses(UnicursalEditView.class)
public class UnicursalMainView extends PolymerTemplate<TemplateModel> 
						implements IGgmView{
	private static final long serialVersionUID = 1L;
	
	@Id("footerContainer")
	protected HorizontalLayout footerContainer;
	
	@Autowired
	private UnicursalEditView unicursalEditView;
	
	@Autowired
	private UnicursalGridView unicursalGridView;
	
	@Autowired
	private UnicursalEditPresenter presenter;
	
	@Autowired
	private FeedbackSelection feedbackSelection;
	
//	@Id("edit-view")
//	private UnicursalEditView unicursalEditView;
//	
//	@Id("grid-view")
//	private UnicursalGridView unicursalGridView;

	public IGgmPresenter<?> getPresenter() {
		return presenter;
	}
	
	@Override
	public void buildView() {
		footerContainer.add(new Label("Label"));
		footerContainer.add(unicursalEditView, unicursalGridView);
//		feedbackSelection = BeanUtil.getBean(FeedbackSelection.class);
//		headerContainer.add(new Label(feedbackSelection.getColumn()+" row="+feedbackSelection.getRow()));
	}
}
