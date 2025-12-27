package org.arecap.eden.ia.console.service;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ListRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    List<T> findAll();


}
