package org.hkrdi.eden.ggm.repository.ggm.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "etl")
public class Etl implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etl_id_seq")
    @SequenceGenerator(name = "etl_id_seq", allocationSize = 1)
    @Column(name = "etl_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private Application application;//application name
    
    private Integer level;//level

    private Integer node;

    @Column(columnDefinition = "text")
    private String label;

    @Column(columnDefinition = "text")
    private String brief;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String etlLog;
    
    private Integer originalNode;
    
    private Boolean usedInApplication;
    
    public Etl() {
    }
    
	public Etl(Application application, Integer level, Integer node, String label, String brief, String description,
			String etlLog, Integer originalNode, Boolean usedInApplication) {
		super();
		this.application = application;
		this.level = level;
		this.node = node;
		
		this.label = label.equals("Empty Title")?"":label;
		this.brief = brief.equals("Empty Title")?"":brief;
		this.description = description.equals("Empty Title")?"":description;
		this.etlLog = etlLog;
		this.originalNode = originalNode;
		this.usedInApplication = usedInApplication;
	}

	public Etl(Application application, Integer level, Integer node, String label, String brief,
			String description) {
		super();
		this.application = application;
		this.level = level;
		this.node = node;
		this.label = label.equals("Empty Title")?"":label;
		this.brief = brief.equals("Empty Title")?"":brief;
		this.description = description.equals("Empty Title")?"":description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
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

	public String getEtlLog() {
		return etlLog;
	}

	public void setEtlLog(String etlLog) {
		this.etlLog = etlLog;
	}

	public Integer getOriginalNode() {
		return originalNode;
	}

	public void setOriginalNode(Integer originalNode) {
		this.originalNode = originalNode;
	}

	public Boolean getUsedInApplication() {
		return usedInApplication;
	}

	public void setUsedInApplication(Boolean usedInApplication) {
		this.usedInApplication = usedInApplication;
	}

	public String getImportDescription() {
		if (getBrief()!=null && getLabel()!=null && !getBrief().isEmpty() && !getLabel().isEmpty() && !getBrief().equals(getDescription()) && !getLabel().equals(getDescription())){
			return (getBrief()!=null?getBrief() +" ":"")+
					(getLabel()!=null?getLabel()+" ":getLabel())+ getDescription();
		}
		if (getBrief()!=null && !getBrief().isEmpty() && !getBrief().equals(getDescription())){
			return (getBrief()!=null?getBrief() +" ":"")+ getDescription();
		}
		return getDescription();
	}

	@Override
	public String toString() {
		if (getBrief()!=null && getLabel()!=null && !getBrief().isEmpty() && !getLabel().isEmpty() && !getBrief().equals(getDescription()) && !getLabel().equals(getDescription())){
			return (getBrief()!=null?getBrief() +" ":"")+
					(getLabel()!=null?getLabel()+" ":getLabel())+ getDescription();
		}
		if (getBrief()!=null && !getBrief().isEmpty() && !getBrief().equals(getDescription())){
			return (getBrief()!=null?getBrief() +" ":"")+ getDescription();
		}
		return getDescription();
	}
    
}
