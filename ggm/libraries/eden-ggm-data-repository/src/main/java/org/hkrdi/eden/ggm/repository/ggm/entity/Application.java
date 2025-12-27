package org.hkrdi.eden.ggm.repository.ggm.entity;


import org.hibernate.envers.Audited;
import org.hkrdi.eden.ggm.repository.entity.BriefEntity;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;

import javax.persistence.*;

@Entity
@Audited
@Table(name = "application")
public class Application extends BriefEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_seq")
    @SequenceGenerator(name = "application_id_seq", allocationSize = 1)
    @Column(name = "application_id")
    private Long id;

    @Column(name = "semantic_grid_id")
    private Long semanticGridId;

    @Transient
    private SemanticMap semanticMap;

    private Integer etlVersion;
    
    private Long originalApplication;
    
    private Long relatedApplicationId;
    
    private String language;

    public Long getRelatedApplicationId() {
		return relatedApplicationId;
	}

	public void setRelatedApplicationId(Long relatedApplicationId) {
		this.relatedApplicationId = relatedApplicationId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSemanticGridId() {
        return semanticGridId;
    }

    public void setSemanticGridId(Long semanticGridId) {
        this.semanticGridId = semanticGridId;
    }

    public SemanticMap getSemanticMap() {
        return semanticMap;
    }

    public void setSemanticMap(SemanticMap semanticMap) {
        this.semanticMap = semanticMap;
    }

    public Integer getEtlVersion() {
		return etlVersion;
	}

	public void setEtlVersion(Integer etlVersion) {
		this.etlVersion = etlVersion;
	}

	public Long getOriginalApplication() {
		return originalApplication;
	}

	public void setOriginalApplication(Long originalSemiotics) {
		this.originalApplication = originalSemiotics;
	}
	
	@Transient
	public String getBriefLabelTmp() {
        return getBrief().getLabel();
    }

	@Transient
    public String getBriefBriefTmp() {
        return getBrief().getBrief();
    }

	@Transient
    public String getBriefDescriptionTmp() {
        return getBrief().getDescription();
    }
}
