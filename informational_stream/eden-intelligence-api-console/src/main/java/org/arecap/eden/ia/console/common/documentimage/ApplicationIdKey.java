package org.arecap.eden.ia.console.common.documentimage;

import java.io.Serializable;

//@Embeddable
public class ApplicationIdKey implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long applicationId;

	public ApplicationIdKey() {
	}
	
	public ApplicationIdKey(Long applicationId, Long id) {
		this.id = id;
		this.applicationId = applicationId;
	}
	
	
	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationIdKey other = (ApplicationIdKey) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
