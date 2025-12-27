package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.repository;

import java.util.List;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.FeedbackApplication;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackApplicationRepository extends PagingAndSortingRepository<FeedbackApplication, Long> {

    List<FeedbackApplication> findAllByLabelStartsWithIgnoreCase(String filterText);
    
    FeedbackApplication findOneByLabel(String filterText);

}
