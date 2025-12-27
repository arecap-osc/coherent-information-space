package org.hkrdi.eden.ggm.repository.degree1feedback;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackDataMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackDataMapRepository extends PagingAndSortingRepository<FeedbackDataMap, Long> {

    List<FeedbackDataMap> findAllByApplicationId(Long applicationId);
    
    List<FeedbackDataMap> findAllByApplicationIdAndRowAndColumnOrderByFeedbackPosition(Long applicationId, int row, int column);

    Optional<FeedbackDataMap> findByApplicationIdAndRowAndColumnAndFeedbackPosition(Long applicationId, int row, int column, long feedbackPosition);
}
