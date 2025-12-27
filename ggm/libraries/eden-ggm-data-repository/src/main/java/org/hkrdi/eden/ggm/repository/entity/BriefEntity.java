package org.hkrdi.eden.ggm.repository.entity;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BriefEntity<ID> implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Embedded
    private EmbeddedBrief brief;

    public EmbeddedBrief getBrief() {
        return brief;
    }

    public void setBrief(EmbeddedBrief brief) {
        this.brief = brief;
    }

    public abstract ID getId();

    public abstract void setId(ID id);

}
