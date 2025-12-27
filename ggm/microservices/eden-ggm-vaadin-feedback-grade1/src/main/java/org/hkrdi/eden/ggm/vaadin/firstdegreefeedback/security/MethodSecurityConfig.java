package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.permissions.RbacPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, mode=AdviceMode.PROXY)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	private RbacPermissionEvaluator rbacPermissionEvaluator;

	@Autowired
	public void setCustomPermissionEvaluator(RbacPermissionEvaluator customPermissionEvaluator) {
	    this.rbacPermissionEvaluator = customPermissionEvaluator;
	}   
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
	    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
	    expressionHandler.setPermissionEvaluator(rbacPermissionEvaluator);
	    return expressionHandler;
	}

}