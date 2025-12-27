package org.hkrdi.eden.ggm.vaadin.console.microservice.semanticmap.word;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.vaadin.console.microservice.layout.template.LeftSideContentView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class MapEditorView extends LeftSideContentView {

	@Autowired
	private MapEditorPresenter presenter;

	private HorizontalLayout mapWordsLayout = new HorizontalLayout();

	private HorizontalLayout mapLinksLayout = new HorizontalLayout();


	@Override
	public void buildView() {
		super.buildView();
		buildWrapperViews();
	}

	private void buildWrapperViews() {
		getStyle().set("padding-right","10px");
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setSizeFull();
		wrapper.getStyle().set("padding-right","10px");
		mapWordsLayout.setWidthFull();
		mapWordsLayout.setHeight("40%");
		mapWordsLayout.setPadding(false);
		mapWordsLayout.setSpacing(false);
		mapWordsLayout.getStyle().set("margin-top", "50px");
		mapWordsLayout.getStyle().set("overflow", "auto");
		mapLinksLayout.setWidthFull();
		mapLinksLayout.setHeight("60%");
		mapLinksLayout.setPadding(false);
		mapLinksLayout.setSpacing(false);
		mapLinksLayout.getStyle().set("overflow", "auto");
		mapWordsLayout.add(getPresenter().getFromMapWordFormView());
		wrapper.add(mapWordsLayout, mapLinksLayout);
		add(wrapper);
	}

	@Override
	public MapEditorPresenter getPresenter() {
		return presenter;
	}


	public HorizontalLayout getMapWordsLayout() {
		return mapWordsLayout;
	}

	public HorizontalLayout getMapLinksLayout() {
		return mapLinksLayout;
	}
}