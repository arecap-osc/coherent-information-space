package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_data_map")
public class FeedbackDataMap implements IGgmEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_data_map_id_seq")
    @SequenceGenerator(name = "feedback_data_map_id_seq", allocationSize = 1)
    @Column(name = "feedback_data_map_id")
    private Long id;
	
	private Long applicationId;
	
	@Column(name = "feedback_row")
	private int row;
	
	@Column(name = "feedback_column")
	private int column;
	
	private Long feedbackPosition;

	private String dimenssion;
	
	private String unicursal;
	
	private String iesireDate;
	
	private String procesareDate;
	
	private String bazeStrategii;
	
	private String intrareDate;
	
	private String evaluareRaspunsuri;
	
	private String bazeExperiente;
	
	private String completeSemantic;
	
	private String inOutSemantic;
	
	private String outInSemantic;
	
	private boolean done;

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getDimenssion() {
		return dimenssion;
	}

	public void setDimenssion(String dimenssion) {
		this.dimenssion = dimenssion;
	}

	public String getUnicursal() {
		return unicursal;
	}

	public void setUnicursal(String unicursal) {
		this.unicursal = unicursal;
	}

	public String getIesireDate() {
		return iesireDate;
	}

	public void setIesireDate(String iesireDate) {
		this.iesireDate = iesireDate;
	}

	public String getProcesareDate() {
		return procesareDate;
	}

	public void setProcesareDate(String procesareDate) {
		this.procesareDate = procesareDate;
	}

	public String getBazeStrategii() {
		return bazeStrategii;
	}

	public void setBazeStrategii(String bazeStrategii) {
		this.bazeStrategii = bazeStrategii;
	}

	public String getIntrareDate() {
		return intrareDate;
	}

	public void setIntrareDate(String intrareDate) {
		this.intrareDate = intrareDate;
	}

	public String getEvaluareRaspunsuri() {
		return evaluareRaspunsuri;
	}

	public void setEvaluareRaspunsuri(String evaluareRaspunsuri) {
		this.evaluareRaspunsuri = evaluareRaspunsuri;
	}

	public String getBazeExperiente() {
		return bazeExperiente;
	}

	public void setBazeExperiente(String bazeExxperiente) {
		this.bazeExperiente = bazeExxperiente;
	}

	public String getCompleteSemantic() {
		return completeSemantic;
	}

	public void setCompleteSemantic(String completeSemantic) {
		this.completeSemantic = completeSemantic;
	}

	public String getInOutSemantic() {
		return inOutSemantic;
	}

	public void setInOutSemantic(String inOutSemantic) {
		this.inOutSemantic = inOutSemantic;
	}

	public String getOutInSemantic() {
		return outInSemantic;
	}

	public void setOutInSemantic(String outInSemantic) {
		this.outInSemantic = outInSemantic;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Long getId() {
		return id;
	}

	public Long getFeedbackPosition() {
		return feedbackPosition;
	}

	public void setFeedbackPosition(Long feedbackPosition) {
		this.feedbackPosition = feedbackPosition;
	}
}
