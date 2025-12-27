package org.hkrdi.eden.ggm.vaadin.console.service.degree1feedback;


import org.hkrdi.eden.ggm.repository.degree1feedback.FeedbackApplicationRepository;
import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackApplicationService implements CrudRepositoryService<FeedbackApplication, Long> {

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
    @Override
    public <S extends FeedbackApplication> S save(S entity) {
    	return CrudRepositoryService.super.save(entity);
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'delete')")
    @Override
    public void delete(FeedbackApplication semanticGrid) {
	     CrudRepositoryService.super.delete(semanticGrid);
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostAuthorize("hasPermission(returnObject.get(), 'app', 'read')")
    @Override
    public Optional<FeedbackApplication> findById(Long id) {
    	return CrudRepositoryService.super.findById(id);
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

	@Override
	public CrudRepository<FeedbackApplication, Long> getRepository() {
		return applicationRepository;
	}
}
