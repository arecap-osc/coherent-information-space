package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.export.FeedbackExportComponent;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.TopBarView;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class UnicursalMainView extends VerticalLayout 
						implements FlowView{
	private static final long serialVersionUID = 1L;
	
	@Id("footerContainer")
	protected HorizontalLayout footerContainer;

	@Autowired
	private UnicursalMainPresenter presenter;

	@Autowired
	private TopBarView topBarView;
	
	@Autowired
	private UnicursalEditView unicursalEditView;
	
	@Autowired
	private UnicursalGridView unicursalGridView;
	
	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private FeedbackExportComponent exportButton;

	public UnicursalMainPresenter getPresenter() {
		return presenter;
	}
	
	private Label titleLabel;

	@Override
	public void buildView() {
//		this.setSizeFull();
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setPadding(false);
		headerLayout.setWidthFull();
		titleLabel = new Label();
		headerLayout.add(titleLabel);

		Anchor backAnchor = new Anchor();
	    backAnchor.setText("msg.feedback.unicursal.mainview.back");
	    backAnchor.getElement().addEventListener("click", e -> {
	    	feedbackSelection.setUnicursalDataMap(null);
	    	feedbackSelection.setFeedbackDataMap(null);
	    	feedbackSelection.setColumn(-1);
	    	feedbackSelection.setRow(-1);

	    	UI.getCurrent().getPage().getHistory().back();
    	});
	    headerLayout.add(backAnchor);
	    headerLayout.add(exportButton);
	    
	    headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

	    topBarView.add(headerLayout);
		add(unicursalEditView, unicursalGridView);
//		feedbackSelection = BeanUtil.getBean(FeedbackSelection.class);
//		headerContainer.add(new Label(feedbackSelection.getColumn()+" row="+feedbackSelection.getRow()));
	}
	
	@Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }

	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowView.super.localeChange(event);
		try{
			afterPrepareView();
		}catch(Throwable t) {
			//silent
		}
	}
	
	@Override
	public void afterPrepareView() {
//		titleLabel.setText("Aplicatia: "+feedbackSelection.getApplication().getLabel()+
//		" - coloana "+(unicursalEditView.getPresenter().getEntity().getColumn()+1)+", rand "+ (unicursalEditView.getPresenter().getEntity().getRow()+1));
		titleLabel.setText(GgmI18NProviderStatic.getTranslation("msg.feedback.unicursal.mainview.title", 
				feedbackSelection.getApplication().getLabel(),
				unicursalEditView.getPresenter().getEntity().getColumn()+1,
				unicursalEditView.getPresenter().getEntity().getRow()+1));
		exportButton.setup();
	}
}
