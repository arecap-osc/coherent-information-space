package org.hkrdi.eden.ggm.repository.degree1feedback.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;

import java.io.Serializable;

@Entity
@Audited
@Table(name = "feedback_data_map")
public class FeedbackDataMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_data_map_id_seq")
    @SequenceGenerator(name = "feedback_data_map_id_seq", allocationSize = 1)
    @Column(name = "feedback_data_map_id")
    private Long id;
	
	private Long feedbackPosition;
	
	@Transient
	private String feedbackPositionAsString;
	
	private Long applicationId;
	
	@Column(name = "feedback_row")
	private int row;
	
	@Column(name = "feedback_column")
	private int column;
	
	private String dimenssion;
	
	private String unicursal;
	
	private String unicursalPurple;
	
	private String unicursalGreen;
	
	private String iesireDate;
	
	private String procesareDate;
	
	private String bazeStrategii;
	
	private String intrareDate;
	
	private String evaluareRaspunsuri;
	
	private String bazeExperiente;
	
	@Column(columnDefinition = "text")
	private String completeSemantic;
	
	@Column(columnDefinition = "text")
	private String inOutSemantic;
	
	@Column(columnDefinition = "text")
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

	public void setFeedbackPosition(Long position) {
		this.feedbackPosition = position;
	}

	public String getIesireDate2() {
		return iesireDate;
	}

	public String getIesireDate3() {
		return iesireDate;
	}
	
	public String getProcesareDate2() {
		return procesareDate;
	}

	public String getBazeStrategii2() {
		return bazeStrategii;
	}

	public String getIntrareDate2() {
		return intrareDate;
	}
	
	public String getIntrareDate3() {
		return intrareDate;
	}

	public String getEvaluareRaspunsuri2() {
		return evaluareRaspunsuri;
	}

	public String getBazeExperiente2() {
		return bazeExperiente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFeedbackPositionAsString() {
		return feedbackPosition+"";
	}

	public String getUnicursalPurple() {
		return unicursalPurple;
	}

	public void setUnicursalPurple(String unicursalPurple) {
		this.unicursalPurple = unicursalPurple;
	}

	public String getUnicursalGreen() {
		return unicursalGreen;
	}

	public void setUnicursalGreen(String unicursalGreen) {
		this.unicursalGreen = unicursalGreen;
	}
}
