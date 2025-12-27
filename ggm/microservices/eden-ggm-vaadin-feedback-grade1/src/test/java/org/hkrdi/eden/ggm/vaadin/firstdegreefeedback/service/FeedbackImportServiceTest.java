package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;


import java.util.ArrayList;
import java.util.List;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.TokenAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackImportServiceTest {

    @Autowired
    private FeedbackApplicationService service;

    @Autowired
    private FeedbackImportService importService;
    
    @Autowired
    private FeedbackDataMapService dataMapService;
    
    private Long appId = null;
    
	@Mock
	TokenAuthentication token;

	private void initSecurity() {
		MockitoAnnotations.initMocks(this);
    	List<String> permissions = new ArrayList<String>();
    	permissions.add("app:*:c,r,w,d");
    	
    	Mockito.when(token.readAuth0Permissions()).thenReturn(permissions);
    	
    	Mockito.when(token.isAuthenticated()).thenReturn(true);

    	SecurityContextHolder.getContext().setAuthentication(token);
	}
	
    @WithMockUser(username="spring")
    @Test
    public void testImport() throws Exception {
    	initSecurity();
    	
    	FeedbackApplication o = service.getAllApplications().get(0);
    	o = service.getApplicationById(11894L);
    	System.out.println("Application="+o.getBrief());
    	appId = o.getId();
    	List<FeedbackDataMap> dms = importService.importFile(o, getClass().getClassLoader().getResourceAsStream("Coloana1-Rand 6.  învăţarea prin experimentare si descoperire (1).docx"));
    	
    	Assert.isTrue(dms.get(0).getColumn() == 0L, "Wrong column");
		Assert.isTrue(dms.get(0).getRow() == 5L, "Wrong row");
//		Assert.isTrue(dms.get(0).getCompleteSemantic().equals(
//				"învăţarea prin experimentare si descoperire prin simțuri. Exprimarea inteligibilă a nevoilor si obținerea răspunsului dorit prin strategii de condiționare a mamei de către copil, mergând către emancipare finală"),
//				"Wrong semantic");

    }
}
