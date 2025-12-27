package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;


import java.util.ArrayList;
import java.util.List;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
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
public class FeedbackApplicationServiceTest {

    @Autowired
    private FeedbackApplicationService service;

    @Autowired
    private UnicursalDataMapService unicursalDataMapService;
    
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
    @Before
    public void testApplicationCreation() {
    	initSecurity();
    	
    	FeedbackApplication feedbackApplication = new FeedbackApplication();
    	String appName = "test"+System.currentTimeMillis();
    	feedbackApplication.setLabel(appName);
    	feedbackApplication.setBrief(appName);
    	feedbackApplication.setDescription(appName);
    	FeedbackApplication o = service.save(feedbackApplication);
    	appId = o.getId();
    	Assert.notNull(o, "The object is not saved");
    	Assert.isTrue(o.getLabel().equals(appName), "the object is not saved");
    	System.out.println("Appication name="+appName+ " with id="+appId);
    }
    
//    @WithMockUser(username="spring")
//    @Test
    public void testUnicursalCreation() {
    	initSecurity();
    	FeedbackApplication o = service.getApplicationById(appId);
    	unicursalDataMapService.generateAndSave(o.getId());
    	Assert.isTrue(unicursalDataMapService.findAll(o.getId()).size() == 36, "not the expected size of 36");
    }
    
    @WithMockUser(username="spring")
    @Test
    public void testFeedbackDataMapCreation() {
    	initSecurity();
    	
    	testUnicursalCreation();
    	
    	FeedbackApplication o = service.getApplicationById(appId);
    	List<UnicursalDataMap> us = unicursalDataMapService.findAll(appId);
    	us.stream().forEach(u->dataMapService.generateAllCombinationsAndSave(appId, u.getRow(), u.getColumn()));
    	Assert.isTrue(dataMapService.findAll(o.getId()).size() == 36 * 81, "not the expected size of "+(36*81));
    }
}
