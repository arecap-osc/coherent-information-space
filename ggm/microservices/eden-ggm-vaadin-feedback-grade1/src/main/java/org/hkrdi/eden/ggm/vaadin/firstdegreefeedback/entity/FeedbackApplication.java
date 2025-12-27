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
@Table(name = "feedback_application")
public class FeedbackApplication implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_application_id_seq")
    @SequenceGenerator(name = "feedback_application_id_seq", allocationSize = 1)
    @Column(name = "feedback_application_id")
    private Long id;

	@Column(columnDefinition = "text")
    private String label;

    @Column(columnDefinition = "text")
    private String brief;

    @Column(columnDefinition = "text")
    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
