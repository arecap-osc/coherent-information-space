package org.arecap.eden.ia.console.boot;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

@Entity
@RevisionEntity(EnversUsernameListener.class)
public class EnversRevEntity extends DefaultRevisionEntity {
	private String username;

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
}