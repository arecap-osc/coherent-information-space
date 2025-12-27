package org.arecap.eden.ia.console.boot;

import org.arecap.eden.ia.console.security.TokenAuthentication;
import org.hibernate.envers.RevisionListener;
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