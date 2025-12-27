package org.hkrdi.eden.ggm.vaadin.console.etl.data.repository;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.Etl;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface EtlRepository extends PagingAndSortingRepository<Etl, Long> {

	public List<Etl> findAll();
	
	public Stream<Etl> findAllByApplication(Application application);

	public List<Etl> findAllByApplicationAndUsedInApplication(Application application, Boolean usedInApplication);

	public List<Etl> findAllByApplicationAndUsedInApplicationAndLevel(Application application, Boolean usedInApplication, Integer level);
	
	public List<Etl> findAllByApplicationAndUsedInApplicationAndDescription(Application application, Boolean usedInApplication, String description);
}
