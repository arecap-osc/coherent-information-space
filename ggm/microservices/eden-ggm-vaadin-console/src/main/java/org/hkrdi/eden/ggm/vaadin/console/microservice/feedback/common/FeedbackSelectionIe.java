package org.hkrdi.eden.ggm.vaadin.console.microservice.feedback.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vaadin.flow.spring.annotation.UIScope;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.springframework.web.exchange.annotation.InformationExchange;

@InformationExchange
@UIScope
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackSelectionIe {

	private FeedbackApplication application;
	private int column = 3;
	private int row = 3;
	private long maxNextFeedback = 0;
	private long minBackFeedback = 0;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private FeedbackDataMap feedbackDataMap;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private UnicursalDataMap unicursalDataMap;
	
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
	public FeedbackDataMap getFeedbackDataMap() {
		return feedbackDataMap;
	}
	public void setFeedbackDataMap(FeedbackDataMap feedbackDataMap) {
		this.feedbackDataMap = feedbackDataMap;
	}
	public UnicursalDataMap getUnicursalDataMap() {
		return unicursalDataMap;
	}
	public void setUnicursalDataMap(UnicursalDataMap unicursalDataMap) {
		this.unicursalDataMap = unicursalDataMap;
	}
	public long getMaxNextFeedback() {
		return maxNextFeedback;
	}
	public void setMaxNextFeedback(long maxNextFeedback) {
		this.maxNextFeedback = maxNextFeedback;
	}
	public long getMinBackFeedback() {
		return minBackFeedback;
	}
	public void setMinBackFeedback(long minBackFeedback) {
		this.minBackFeedback = minBackFeedback;
	}
	
	
}
