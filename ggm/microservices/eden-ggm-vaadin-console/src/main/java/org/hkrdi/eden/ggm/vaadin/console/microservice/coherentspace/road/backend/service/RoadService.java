package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.repository.RoadRepository;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoadService implements CrudRepositoryService<Road, Long> {

    @Autowired
    private RoadRepository repository;

	@Override
	public CrudRepository<Road, Long> getRepository() {
		return repository;
	}

	@PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'write')")
    @Override
    public <S extends Road> S save(S entity) {
    	return CrudRepositoryService.super.save(entity);
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission(#entity, 'app', 'delete')")
    @Override
    public void delete(Road semanticGrid) {
	     CrudRepositoryService.super.delete(semanticGrid);
    }
    
    @PreAuthorize("isAuthenticated() and hasPermission('*', 'app', 'read')")
    @PostAuthorize("hasPermission(returnObject.get(), 'app', 'read')")
    @Override
    public Optional<Road> findById(Long id) {
    	return CrudRepositoryService.super.findById(id);
    }

	@PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<String> getGroupNamesAndFractolon(String network) {
		return repository.findDistinctGroupNameByNetworkAndFractolonIsNotNull(network);
	}

	@PostFilter("hasPermission(filterObject, 'app', 'read')")
	public List<Road> getRoadsByNetworkAndGroupName(String network, String groupName) {
		return repository.findAllByNetworkAndGroupNameOrderByNameAsc(network, groupName);
	}
	
	@PostFilter("hasPermission(filterObject, 'app', 'read')")
	public List<Road> getRoadsByNetwork(String network) {
		return repository.findAllByNetworkAndGroupNameIsNotNullAndNameIsNotNullOrderByGroupNameAscOrderPositionAscNameAsc(network);
	}
	
	@PostFilter("hasPermission(filterObject, 'app', 'read')")
	public List<Road> findAllByGroupNameStartsWithIgnoreCaseOrNameStartsWithIgnoreCaseOrderByNetworkAscAndGroupNameAscAndOrderPositionAsc(String text){
		return repository.findAllByGroupNameStartsWithIgnoreCaseOrNameStartsWithIgnoreCaseOrderByNetworkAsc(text, text);
	}
	
	@PostFilter("hasPermission(filterObject, 'app', 'read')")
    public List<Road> getAllRoads() {
		return repository.findAllOrderByNetworkAsc();
//        List<Road> roads = new ArrayList<>();
//        repository.findAll().forEach(roads::add);
//        return roads;
    }
}
