package org.hkrdi.eden.ggm.repository.degree1feedback;

import org.hkrdi.eden.ggm.repository.degree1feedback.entity.FeedbackApplication;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackApplicationRepository extends PagingAndSortingRepository<FeedbackApplication, Long> {

    List<FeedbackApplication> findAllByLabelStartsWithIgnoreCase(String filterText);
    
    FeedbackApplication findOneByLabel(String filterText);

}
