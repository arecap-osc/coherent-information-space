package org.hkrdi.eden.ggm.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EmbeddedBrief implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "text")
    private String label;

    @Column(columnDefinition = "text")
    private String brief;

    @Column(columnDefinition = "text")
    private String description;

    public EmbeddedBrief() {
    }

    public EmbeddedBrief(String brief, String description, String label) {
        this.brief = brief;
        this.description = description;
        this.label = label;
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
