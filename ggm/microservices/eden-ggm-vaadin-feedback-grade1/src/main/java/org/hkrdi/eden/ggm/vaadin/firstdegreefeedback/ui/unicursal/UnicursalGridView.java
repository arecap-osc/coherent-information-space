package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

@UIScope
@Component
@ParentLayout(MainLayout.class)
@HtmlImport("frontend://feedbacks/UnicursalGridViewDesign.html")
@Tag("unicursal-grid-view")
public class UnicursalGridView extends PolymerTemplate<TemplateModel> 
						implements IGgmView{
	private static final long serialVersionUID = 1L;
	
	@Id("grid")
	private Element grid;

	@Autowired
	private FeedbackSelection feedbackSelection;
	
	@Autowired
	private UnicursalEditPresenter presenter;

	public IGgmPresenter<?> getPresenter() {
		return presenter;
	}
}
