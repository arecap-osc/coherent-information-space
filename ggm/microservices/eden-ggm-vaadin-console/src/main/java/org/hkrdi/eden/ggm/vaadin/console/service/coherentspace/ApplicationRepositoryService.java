package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;


import org.hkrdi.eden.ggm.repository.entity.EmbeddedBrief;
import org.hkrdi.eden.ggm.repository.ggm.ApplicationRepository;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.SemanticMap;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.semanticmap.SemanticMapRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationRepositoryService implements CrudRepositoryService<Application, Long> {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SemanticMapRepositoryService semanticMapRepositoryService;

    @Override
    public ApplicationRepository getRepository() {
        return applicationRepository;
    }

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        applicationRepository.findAll().forEach(applications::add);
        applications.stream().forEach(this::setApplicationSemanticMap);
        return applications.stream().filter(s -> s.getBrief().getBrief() != null && !s.getBrief().getBrief().startsWith("FAILED-")).collect(Collectors.toList());
    }
    
    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<Application> getAllApplicationsAndEmpty() {
        List<Application> applications = new ArrayList<>();
        applications.add(new Application());
        applications.get(0).setBrief(new EmbeddedBrief());
        applications.addAll(getAllApplications());
        return applications;
    }
    
    public Application getEmpty() {
        Application app = new Application();
        app.setBrief(new EmbeddedBrief());
        return app;
    }
    
    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<Application> getRelatedApplications(Application currentApp) {
    	List<Application> applications = getAllApplications();
    	List<Application> result = applications.stream().filter(app-> currentApp.getId().equals(app.getRelatedApplicationId())).collect(Collectors.toList());
    	result.add(currentApp);
    	result.addAll(applications.stream().filter(app-> app.getId().equals(currentApp.getRelatedApplicationId())).collect(Collectors.toList()));
    	return result;
    }

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<SemanticMap> getAllSemanticMaps() {
        return semanticMapRepositoryService.findAll();
    }

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<Application> getAllApplicationsByBriefLabelStartsWithIgnoreCase(String textFilter) {
        List<Application> applications = new ArrayList<>();
        applicationRepository.findAllByBriefLabelStartsWithIgnoreCase(textFilter).forEach(applications::add);
        applications.stream().forEach(this::setApplicationSemanticMap);
        return applications.stream().filter(s -> s.getBrief().getBrief() != null && !s.getBrief().getBrief().startsWith("FAILED-")).collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'write')")
    @Override
    public Application save(Application application) {
        return applicationRepository.save(application) ;
    }

    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'delete')")
    @Override
    public void delete(Application application) {
        applicationRepository.delete(application);
    }

    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostAuthorize("hasPermission(returnObject, 'app', 'read')")
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).get();
    }

    public List<Application> fetchApplicationsNotReimported() {
        return getAllApplications().stream().
                filter(s -> s.getEtlVersion() != null && s.getEtlVersion() > 0).collect(Collectors.toList());
    }

    @PostFilter("hasPermission(filterObject, 'app', 'read')")
    public void setApplicationSemanticMap(Application application) {
        if (application.getSemanticGridId() != null) {
            application.setSemanticMap(semanticMapRepositoryService.findById(application.getSemanticGridId())
                    .orElse(new SemanticMap()));
            return;
        }

        application.setSemanticMap(new SemanticMap());
    }
}
