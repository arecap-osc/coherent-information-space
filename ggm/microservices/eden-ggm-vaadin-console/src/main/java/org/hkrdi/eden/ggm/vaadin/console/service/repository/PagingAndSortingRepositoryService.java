package org.hkrdi.eden.ggm.vaadin.console.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PagingAndSortingRepositoryService<T, ID> extends CrudRepositoryService<T, ID> {

    PagingAndSortingRepository<T, ID> getRepository();

    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    default Iterable<T> findAll(Sort sort) {
        return getRepository().findAll(sort);
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    default Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    default long countAnyMatching(ID id, Optional<String> search, Optional<String> filter) {
        throw new UnsupportedOperationException();
    }

    default Page<T> findAnyMatching(ID id, Optional<String> search, Optional<String> filter, Pageable pageable){
        throw new UnsupportedOperationException();
    }

    default long countAnyMatching(Long id, Optional<?>... searchParams){
        throw new UnsupportedOperationException();
    }

    default Page<T> findAnyMatching(Long id, Pageable pageable, Optional<?>... searchParams){
        throw new UnsupportedOperationException();
    }

}
