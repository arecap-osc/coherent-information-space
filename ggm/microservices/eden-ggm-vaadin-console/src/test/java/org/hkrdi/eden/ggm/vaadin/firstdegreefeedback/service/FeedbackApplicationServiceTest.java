package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;


import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackApplicationService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.FeedbackDataMapService;
import org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback.UnicursalDataMapService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FeedbackApplicationServiceTest.class)
@ComponentScan("org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback")
@EnableJpaRepositories(basePackages = {"org.hkrdi.eden.ggm.repository.degree1feedback"})
@EntityScan({"org.hkrdi.eden.ggm.repository.degree1feedback.entity"})

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
    }
    
//    @WithMockUser(username="spring")
//    @Test
    public void testUnicursalCreation() {
    	initSecurity();
    	FeedbackApplication o = service.findById(appId).get();
    	unicursalDataMapService.generateAndSave(o.getId());
    	Assert.isTrue(unicursalDataMapService.findAllByApplicationId(o.getId()).size() == 36, "not the expected size of 36");
    }
    
    @WithMockUser(username="spring")
    @Test
    public void testFeedbackDataMapCreation() {
    	initSecurity();
    	
    	testUnicursalCreation();
    	
    	FeedbackApplication o = service.findById(appId).get();
    	List<UnicursalDataMap> us = unicursalDataMapService.findAllByApplicationId(appId);
    	us.stream().forEach(u->dataMapService.generateAllCombinationsAndSave(appId, u.getRow(), u.getColumn()));
    	Assert.isTrue(dataMapService.findAllByApplicationId(o.getId()).size() == 36 * 81, "not the expected size of "+(36*81));
    }
}
