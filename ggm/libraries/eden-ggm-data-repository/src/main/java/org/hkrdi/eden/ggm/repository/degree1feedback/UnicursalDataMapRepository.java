package org.hkrdi.eden.ggm.repository.degree1feedback;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.UnicursalDataMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnicursalDataMapRepository extends PagingAndSortingRepository<UnicursalDataMap, Long> {

    List<UnicursalDataMap> findAllByApplicationId(Long applicationId);
    
    UnicursalDataMap findOneByApplicationIdAndRowAndColumn(Long applicationId, int row, int column);

}
