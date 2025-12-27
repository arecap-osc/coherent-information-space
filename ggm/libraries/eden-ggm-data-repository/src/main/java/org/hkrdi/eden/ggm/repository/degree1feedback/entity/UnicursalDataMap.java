package org.hkrdi.eden.ggm.repository.degree1feedback.entity;

import javax.persistence.*;

import org.hibernate.envers.Audited;

import java.io.Serializable;

@Entity
@Audited
@Table(name = "unicursal_data_map")
public class UnicursalDataMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unicursal_data_map_id_seq")
    @SequenceGenerator(name = "unicursal_data_map_id_seq", allocationSize = 1)
    @Column(name = "unicursal_data_map_id")
    private Long id;
	
	private Long applicationId;
	
	@Column(name = "feedback_row")
	private int row;
	
	@Column(name = "feedback_column")
	private int column;
	
	@Column(columnDefinition = "text")
	private String dimenssion;
	
	@Column(columnDefinition = "text")
	private String unicursal;
	
	@Column(columnDefinition = "text")
	private String semantic;
	
	private String definition;
	
	@Column(columnDefinition = "text")
	private String dimenssionUrl;
	
	@Column(columnDefinition = "text")
	private String unicursalUrl;
	
	@Column(columnDefinition = "text")
	private String unicursalPurple;
	
	@Column(columnDefinition = "text")
	private String unicursalGreen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getSemantic() {
		return semantic;
	}

	public void setSemantic(String semantic) {
		this.semantic = semantic;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getDimenssionUrl() {
		return dimenssionUrl;
	}

	public void setDimenssionUrl(String dimenssionUrl) {
		this.dimenssionUrl = dimenssionUrl;
	}

	public String getUnicursalUrl() {
		return unicursalUrl;
	}

	public void setUnicursalUrl(String unicursalUrl) {
		this.unicursalUrl = unicursalUrl;
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