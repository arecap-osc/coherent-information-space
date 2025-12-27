package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository;

import java.util.List;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.UnicursalDataMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnicursalDataMapRepository extends PagingAndSortingRepository<UnicursalDataMap, Long> {

    List<UnicursalDataMap> findAllByApplicationId(Long applicationId);
    
    UnicursalDataMap findOneByApplicationIdAndRowAndColumn(Long applicationId, int row, int column);

}
