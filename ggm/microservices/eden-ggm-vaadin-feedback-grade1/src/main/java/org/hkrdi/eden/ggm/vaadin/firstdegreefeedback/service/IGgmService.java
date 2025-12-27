package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.service;

import java.util.Optional;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.IGgmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGgmService<T extends IGgmEntity> {
	public T save(T iGgmEntity);

	public void delete(Long id);

	public T load(Long id);
	
	/////////////////////////
	
	public T findOne(Long id);
	
	public default long countAnyMatching(Long parrentId, Optional<String> search, Optional<String> filter){
		throw new UnsupportedOperationException();
	}
	
	public default Page<T> findAnyMatching(Long parrentId, Optional<String> search, Optional<String> filter, Pageable pageable){
		throw new UnsupportedOperationException();
	}

	public default long countAnyMatching(Long parrentId, Optional<?>... searchParams){
		throw new UnsupportedOperationException();
	}

	public default Page<T> findAnyMatching(Long parrentId, Pageable pageable, Optional<?>... searchParams){
		throw new UnsupportedOperationException();
	}
}
