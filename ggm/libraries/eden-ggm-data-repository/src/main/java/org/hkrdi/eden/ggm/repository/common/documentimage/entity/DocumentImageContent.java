package org.hkrdi.eden.ggm.repository.common.documentimage.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.envers.Audited;
import org.hkrdi.eden.ggm.repository.common.ApplicationIdKey;


@Entity
@Audited
@IdClass(ApplicationIdKey.class)
public class DocumentImageContent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Id
	private Long applicationId;
	
	@Column(columnDefinition="text")
	private String image;
	
	public DocumentImageContent() {
	}

	public DocumentImageContent(Long applicationId, byte[] image2) {
		this.image = convertImage(image2);
		this.applicationId = applicationId;
	}

	private String convertImage(byte[] ima) {
		try {
			return new String(Base64.getEncoder().encode(ima), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private byte[] convertImage(String im) {
		try {
			return Base64.getDecoder().decode(im.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isImagePresent() {
		return image != null;
	}
	
	public byte[] getImage() {
		return convertImage(image);
	}

	public void setImage(byte[] image) {
		this.image = convertImage(image);
	}

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

}
