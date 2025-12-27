package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository.FeedbackApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FeedbackApplicationService {

    @Autowired
    private FeedbackApplicationRepository applicationRepository;

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<FeedbackApplication> getAllApplications() {
        List<FeedbackApplication> semanticGrids = new ArrayList<>();
        applicationRepository.findAll().forEach(semanticGrids::add);
        return semanticGrids.stream().filter(s->s.getBrief()!=null&&!s.getBrief().startsWith("FAILED-")).collect(Collectors.toList());
    }

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<FeedbackApplication> getAllApplicationsByBriefLabelStartsWithIgnoreCase(String textFilter) {
        List<FeedbackApplication> semanticGrids = new ArrayList<>();
        applicationRepository.findAllByLabelStartsWithIgnoreCase(textFilter).forEach(semanticGrids::add);
        return semanticGrids.stream().filter(s->s.getBrief()!=null&&!s.getBrief().startsWith("FAILED-")).collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'write')")
    public FeedbackApplication save(FeedbackApplication feedbackApplication) {
        return applicationRepository.save(feedbackApplication);
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'delete')")
    public void delete(FeedbackApplication semanticGrid) {
        applicationRepository.delete(semanticGrid);
    }

    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostAuthorize("hasPermission(returnObject, 'app', 'read')")
    public FeedbackApplication getApplicationById(Long id) {
        return applicationRepository.findById(id).get();
    }

    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<FeedbackApplication> getApplicationsByLabel(String label) {
        return applicationRepository.findAllByLabelStartsWithIgnoreCase(label);
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostAuthorize("hasPermission(returnObject, 'app', 'read')")
    public FeedbackApplication getApplicationByLabel(String label) {
        return applicationRepository.findOneByLabel(label);
    }
}
