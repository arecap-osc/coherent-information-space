package org.hkrdi.eden.ggm.repository.ggm;

import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {

    Iterable<Application> findAllByBriefLabelStartsWithIgnoreCase(String filterText);

}
