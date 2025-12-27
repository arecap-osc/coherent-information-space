package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.unicursal;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.MainLayout;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common.FeedbackSelection;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmEntityPresenter;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp.IGgmEntityView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
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
//@PortalWindow
@HtmlImport("frontend://feedbacks/UnicursalEditViewDesign.html")
@Tag("unicursal-edit-view")
public class UnicursalEditView extends PolymerTemplate<TemplateModel> 
						implements IGgmEntityView{
	private static final long serialVersionUID = 1L;
	
	@Id("dimunic")
	private Element dimunic;

	@Id("dimenssion")
	private Element dimenssion;

	@Id("unicursal")
	private Element unicursal;

	@Id("semantic")
	private TextArea semantic;
	
	@Id("save")
	private Button save;
	
	@Autowired
	private FeedbackSelection feedbackSelection;
	
	@Autowired
	private UnicursalEditPresenter presenter;

	@Override
	public IGgmEntityPresenter<?> getPresenter() {
		return presenter;
	}

	@Override
	public void buildView() {
		System.out.println("asdsadsa");
//		feedbackSelection = BeanUtil.getBean(FeedbackSelection.class);
//		headerContainer.add(new Label(feedbackSelection.getColumn()+" row="+feedbackSelection.getRow()));
	}

	@Override
	public Button getSaveButton() {
		return save;
	}
}
