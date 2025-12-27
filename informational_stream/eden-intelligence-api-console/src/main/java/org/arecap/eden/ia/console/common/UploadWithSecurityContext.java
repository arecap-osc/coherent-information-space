package org.arecap.eden.ia.console.common;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.shared.Registration;
import org.arecap.eden.ia.console.common.i18n.UploadI18n;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UploadWithSecurityContext extends UploadI18n {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient SecurityContext sc;
	
	public UploadWithSecurityContext(SecurityContext sc) {
		super();
		this.sc = sc;
	}
	
	public UploadWithSecurityContext(SecurityContext sc, Receiver receiver) {
		super(receiver);
		this.sc = sc;
	}

	@Override
	public Registration addSucceededListener(ComponentEventListener<SucceededEvent> listener) {
		ComponentEventListener<SucceededEvent> listenerWithSecurity = new ComponentEventListener<SucceededEvent>() {

			@Override
			public void onComponentEvent(SucceededEvent event) {
				try {
					SecurityContextHolder.clearContext();
					SecurityContextHolder.getContext().setAuthentication(sc.getAuthentication());
					listener.onComponentEvent(event);
				}finally {
					SecurityContextHolder.clearContext();
				}
			}
			
		};
		return super.addSucceededListener(listenerWithSecurity);
	}
}
