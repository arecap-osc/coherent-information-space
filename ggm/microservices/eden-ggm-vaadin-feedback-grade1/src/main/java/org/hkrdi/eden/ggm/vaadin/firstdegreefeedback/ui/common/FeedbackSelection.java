package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.common;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.springframework.web.exchange.annotation.InformationExchange;

import com.vaadin.flow.spring.annotation.UIScope;

@InformationExchange
@UIScope
public class FeedbackSelection {

	private FeedbackApplication application;
	private int column = 3;
	private int row = 3;
	
	public FeedbackApplication getApplication() {
		return application;
	}
	public void setApplication(FeedbackApplication application) {
		this.application = application;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	
}
