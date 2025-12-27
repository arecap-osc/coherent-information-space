package org.hkrdi.eden.ggm.repository.common.documentimage.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hkrdi.eden.ggm.repository.common.ApplicationIdKey;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@NamedEntityGraphs({
    @NamedEntityGraph(name="documentImage.bringimage", attributeNodes = {
            @NamedAttributeNode("imageContent")}),
    @NamedEntityGraph(name="documentImage.noJoins", attributeNodes = {
    })
})
@Entity
@Audited
@IdClass(ApplicationIdKey.class)
public class DocumentImage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Id
	private Long applicationId;

	private String name;
	
	private LocalDateTime date;
	
	private String explanation;
	
	private String url;

	private Long parentId;
	
	private String parentDocClass; 
	
	private Long userId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private DocumentImageContent imageContent;
	
	@Enumerated(EnumType.STRING)
	private Source source;
	
	public enum Source {
		ATTACHED("ATTACHED"), GENERATED("GENERATED"), LOGO("LOGO");
		
		private final String stringValue;
		
		Source(final String s) { 
		   stringValue = s; 
	    }
	    
	    public static String getCaption(Source input) {
	    	switch(input) {
	    	case ATTACHED: return "Attached";
	    	case GENERATED: return "Generated";
	    	case LOGO: return "Logo";
	    	default: return "";
	    	}
	    }
	    
	    public String getValue() {
	    	return this.stringValue;
	    }

	};
	
	
	
	public DocumentImage() {
	}
	
	public DocumentImage(String name, LocalDateTime date) {
		super();
		this.name = name;
		this.date = date;
		
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
	
	public DocumentImage(Long applicationId, String name, LocalDateTime date, byte[] image, Long userId, Long parentId, String parentDocClass, Source source) {
		super();
		this.name = name;
		this.date = date;
		this.imageContent = new DocumentImageContent(applicationId, image);
		this.parentDocClass = parentDocClass;
		this.userId = userId;
		this.parentId = parentId;
		this.source = source;
		this.applicationId = applicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getImageContent() {
		if (imageContent == null) return null;
		return imageContent.getImage();
	}
	
	public boolean isImagedPresent() {
		if (imageContent == null) return false;
		return imageContent.isImagePresent();
	}

	public void setImage(byte[] image) {
		if (imageContent == null) {
			imageContent = new DocumentImageContent();
			imageContent.setApplicationId(applicationId);
		}
		this.imageContent.setImage(image);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getDateFormatted() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(formatter);
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentDocClass() {
		return parentDocClass;
	}

	public void setParentDocClass(String parentDocClass) {
		this.parentDocClass = parentDocClass;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "DocumentImage [name=" + name + ", date=" + date + ", parentId=" + parentId + ", parentDocClass="
				+ parentDocClass + ", userId=" + userId + ", source=" + source + "]";
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


	
	

}
