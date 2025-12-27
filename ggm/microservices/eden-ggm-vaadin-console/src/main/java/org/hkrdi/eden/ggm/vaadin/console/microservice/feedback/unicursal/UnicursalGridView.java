package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.unicursal;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.console.common.i18n.GgmI18NProviderStatic;
import org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common.FeedbackSelectionIe;
import org.hkrdi.eden.ggm.vaadin.console.mvp.FlowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Stream;

@UIScope
@SpringComponent
//@HtmlImport("frontend://feedbacks/UnicursalGridViewDesign.html")
//@Tag("unicursal-grid-view")
public class UnicursalGridView extends VerticalLayout 
						implements FlowView{
	private static final long serialVersionUID = 1L;
	
	private Grid<FeedbackDataMap> grid = new Grid<>(FeedbackDataMap.class);

	@Autowired
	private FeedbackSelectionIe feedbackSelection;
	
	@Autowired
	private UnicursalGridPresenter presenter;

	public UnicursalGridPresenter getPresenter() {
		return presenter;
	}
	
	@Override
    public void onAttach(AttachEvent attachEvent) {
        FlowView.super.onAttach(attachEvent);
    }
	
	@Override
	public void buildView() {
		setSizeFull();
        setPadding(false);
        setupSyntaxesGrid();
	}
	
	@Override
	public void localeChange(LocaleChangeEvent event) {
		FlowView.super.localeChange(event);
		Stream.of("feedbackPosition","iesireDate", "procesareDate", "bazeStrategii","intrareDate","evaluareRaspunsuri",
		"bazeExperiente", "completeSemantic", "inOutSemantic", "outInSemantic").forEach(
				column->translateGrigColumn(column, "msg.feedback.unicursal.grid.column.name."+column.toLowerCase()));
	}
	
	private void translateGrigColumn(String columnName, String msgKey) {
		if (grid.getColumnByKey(columnName) != null) {
			grid.getColumnByKey(columnName).setHeader(GgmI18NProviderStatic.getTranslation(msgKey));
		}
	}
	
	private void setupSyntaxesGrid() {

        grid.setColumns("feedbackPosition","iesireDate", "procesareDate", "bazeStrategii","intrareDate","evaluareRaspunsuri",
        		"bazeExperiente", "completeSemantic", "inOutSemantic", "outInSemantic");
        grid.getColumnByKey("feedbackPosition").setWidth("50px").setFlexGrow(0);
        
        Arrays.asList(new String[] {"feedbackPosition","iesireDate", "procesareDate", "bazeStrategii","intrareDate","evaluareRaspunsuri",
		"bazeExperiente"}).stream().forEach(column->grid.getColumnByKey(column).setWidth("140px").setFlexGrow(0));
        grid.getColumnByKey("feedbackPosition").setWidth("50px").setFlexGrow(0);
        
//        syntaxesGrid.addColumn(new ComponentRenderer<>(renderer -> renderer.getAddressIndex()
//                .compareTo(addressIndex) == 0 ? VaadinIcon.ANGLE_LEFT.create() : VaadinIcon.ANGLE_RIGHT.create()))
//                .setHeader("Directie").setWidth("30px");
//        syntaxesGrid.addColumn(new ComponentRenderer<>(renderer -> renderer.getAddressIndex()
//                .compareTo(addressIndex) == 0 ?
//                new Label(renderer.getToAddressIndex()+"") : new Label(renderer.getAddressIndex()+"")))
//                .setHeader("Index").setWidth("50px");
//        syntaxesGrid.addColumn(DataMap::getTrivalentLogic).setHeader("Tip");
//        syntaxesGrid.addColumn(DataMap::getTrivalentLogicType).setHeader("Legatura");
        grid.addThemeNames("column-dividers", "row-stripes");
        grid.asSingleSelect().addValueChangeListener(e->presenter.onGridItemSelected(e.getValue()));
        add(grid);
    }

	public Grid<FeedbackDataMap> getGrid() {
		return grid;
	}

}
