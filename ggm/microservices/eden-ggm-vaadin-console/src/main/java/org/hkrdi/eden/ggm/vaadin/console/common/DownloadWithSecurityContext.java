package org.hkrdi.eden.ggm.vaadin.console.common;

import java.io.InputStream;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

public class DownloadWithSecurityContext extends Anchor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient SecurityContext sc;
	private transient InputStreamFactory factory;
	
	Button downloadButton = new Button(VaadinIcon.DOWNLOAD_ALT.create());
	
	public DownloadWithSecurityContext() {
		this(new Button(VaadinIcon.DOWNLOAD_ALT.create()));
	}
	
	public DownloadWithSecurityContext(Button downloadButton) {
		super();
		getElement().setAttribute("download", true);
        add(downloadButton);
	}
	
	public void configureDownload(String fileName, InputStreamFactory factory) {
		this.sc = SecurityContextHolder.getContext();
		this.factory = factory;
		setHref(new StreamResource(fileName, ()-> createInputStream()));
	}
	
	private InputStream createInputStream() {
		boolean scAllreadySet = false;
		try {
			scAllreadySet = SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null;
			if (!scAllreadySet) { 
				SecurityContextHolder.clearContext();
				SecurityContextHolder.setContext(sc);
				SecurityContextHolder.getContext().setAuthentication(sc.getAuthentication());
			}
			return factory.createInputStream();
		}finally {
			if (!scAllreadySet) { SecurityContextHolder.clearContext();}
		}
	}
}
