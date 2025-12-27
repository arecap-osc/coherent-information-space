package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository;

import java.util.List;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackDataMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackDataMapRepository extends PagingAndSortingRepository<FeedbackDataMap, Long> {

    List<FeedbackDataMap> findAllByApplicationId(Long applicationId);
    
    List<FeedbackDataMap> findAllByApplicationIdAndRowAndColumn(Long applicationId, int row, int column);

}
