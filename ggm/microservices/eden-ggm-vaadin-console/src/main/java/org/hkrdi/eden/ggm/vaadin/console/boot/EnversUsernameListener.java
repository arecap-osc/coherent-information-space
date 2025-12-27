package org.hkrdi.eden.ggm.vaadin.console.boot;

import org.hibernate.envers.RevisionListener;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class EnversUsernameListener implements RevisionListener {
	
    public void newRevision(Object revisionEntity) {
        EnversRevEntity exampleRevEntity = (EnversRevEntity) revisionEntity;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
        	TokenAuthentication token = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
	        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
	        exampleRevEntity.setUsername(userName+"|"+token.getProfileName()+"|"+token.getProfileEmail());
        }else {
        	exampleRevEntity.setUsername("anonimus");
        }
    }
}