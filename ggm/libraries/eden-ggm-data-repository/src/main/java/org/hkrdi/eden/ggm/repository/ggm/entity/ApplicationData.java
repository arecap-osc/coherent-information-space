package org.hkrdi.eden.ggm.repository.ggm.entity;


import org.hibernate.envers.Audited;
import org.hkrdi.eden.ggm.repository.entity.DataMap;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Audited
@Table(name = "application_data")
public class ApplicationData implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_data_id_seq")
    @SequenceGenerator(name = "application_data_id_seq", allocationSize = 1)
    @Column(name = "application_data_id")
    private Long id;

    private Long dataMapId;

    private String network;

    private Long clusterIndex;

    private Long addressIndex;

    private Long toAddressIndex;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private Application application;


    @Column(columnDefinition = "text")
    private String networkSemantic;

    @Column(columnDefinition = "text")
    private String clusterSemantic;

    @Column(columnDefinition = "text")
    private String semantic;

    @Column(columnDefinition = "text")
    private String semanticDetails;

    @Column(columnDefinition = "text")
    private String syntax;

    @Column(columnDefinition = "text")
    private String syntaxDetails;

    private Boolean syntaxDone = false;

    @Column(columnDefinition = "text")
    private String verb;

    @Column(columnDefinition = "text")
    private String conjunction;

    @Column(columnDefinition = "text")
    private String verbalization;

    @Column(columnDefinition = "text")
    private String phrase;

    @Column(columnDefinition = "text")
    private String verbDetails;

    @Column(columnDefinition = "text")
    private String whoWhat;

    @Column(columnDefinition = "text")
    private String how;

    @Column(columnDefinition = "text")
    private String why;

    @Column(columnDefinition = "text")
    private String whereWhen;

    @Transient
    private List<DataMap> wells;

    @Transient
    private List<DataMap> routes;

    public ApplicationData() {
		super();
	}
    
	public ApplicationData(String network, Long addressIndex, 
			Application application, String semantic) {
		super();
		this.network = network;		
		this.addressIndex = addressIndex;
		this.application = application;
		this.semantic = semantic;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataMapId() {
        return dataMapId;
    }

    public void setDataMapId(Long dataMapId) {
        this.dataMapId = dataMapId;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(Long clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    public Long getAddressIndex() {
        return addressIndex;
    }

    public void setAddressIndex(Long addressIndex) {
        this.addressIndex = addressIndex;
    }

    public Long getToAddressIndex() {
        return toAddressIndex;
    }

    public void setToAddressIndex(Long toAddressIndex) {
        this.toAddressIndex = toAddressIndex;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getNetworkSemantic() {
        return networkSemantic;
    }

    public void setNetworkSemantic(String networkSemantic) {
        this.networkSemantic = networkSemantic;
    }

    public String getClusterSemantic() {
        return clusterSemantic;
    }

    public void setClusterSemantic(String clusterSemantic) {
        this.clusterSemantic = clusterSemantic;
    }

    public String getSemantic() {
        return semantic;
    }

    public void setSemantic(String semantic) {
        this.semantic = semantic;
    }

    public String getSemanticDetails() {
        return semanticDetails;
    }

    public void setSemanticDetails(String semanticDetails) {
        this.semanticDetails = semanticDetails;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getSyntaxDetails() {
        return syntaxDetails;
    }

    public void setSyntaxDetails(String syntaxDetails) {
        this.syntaxDetails = syntaxDetails;
    }

    public Boolean getSyntaxDone() {
        return syntaxDone;
    }

    public void setSyntaxDone(Boolean syntaxDone) {
        this.syntaxDone = syntaxDone;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    public String getVerbalization() {
        return verbalization;
    }

    public void setVerbalization(String verbalization) {
        this.verbalization = verbalization;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getVerbDetails() {
        return verbDetails;
    }

    public void setVerbDetails(String verbDetails) {
        this.verbDetails = verbDetails;
    }

    public String getWhoWhat() {
        return whoWhat;
    }

    public void setWhoWhat(String whoWhat) {
        this.whoWhat = whoWhat;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getWhereWhen() {
        return whereWhen;
    }

    public void setWhereWhen(String whereWhen) {
        this.whereWhen = whereWhen;
    }

    public List<DataMap> getWells() {
		return wells;
	}

	public void setWells(List<DataMap> wells) {
		this.wells = wells;
	}

	public List<DataMap> getRoutes() {
		return routes;
	}

	public void setRoutes(List<DataMap> routes) {
		this.routes = routes;
	}

	

}
