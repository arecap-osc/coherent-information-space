package org.hkrdi.eden.ggm.repository.common.documentimage;

import org.hkrdi.eden.ggm.repository.common.ApplicationIdKey;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class DocumentImageJpaService {
	
	@Autowired
	private DocumentImageJpaRepository repository;
	
	public DocumentImageJpaRepository getRepository(){
		return repository;
	}
	
	public long countAnyMatching(Long applicationId, Optional<?>... searchParams) {
		Long parentId = ((Optional<Long>)searchParams[0]).get();//parent id
		String parentClass = ((Optional<String>)searchParams[1]).get();//parent class
		
		return repository.countDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass);
	}

	public List<DocumentImage> findAnyMatching(Long applicationId, List<Long> parentId, String parentClass) {
		return repository.findDistinctByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass).collect(Collectors.toList());
	}
	
	public void deleteByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCaseAndName(Long applicationId, List<Long> parentId, String parentClass, String fileName) {
		repository.deleteByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCaseAndName(applicationId, parentId, parentClass, fileName);
	}
	
	public Page<DocumentImage> findAnyMatching(Long applicationId, Pageable pageable, Optional<?>... searchParams) {
		Long parentId = ((Optional<Long>)searchParams[0]).get();//parent id
		String parentClass = ((Optional<String>)searchParams[1]).get();//parent class
		return repository.findDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass, pageable);
	}

	public Stream<DocumentImage> findAnyMatching(Long applicationId, Optional<?>... searchParams) {
		Long parentId = ((Optional<Long>)searchParams[0]).get();//parent id
		String parentClass = ((Optional<String>)searchParams[1]).get();//parent class
		return repository.findDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass);
	}
	
	public long countByApplicationIdAndParentIdAndEntityName(Long applicationId, Long parentId, String parentClass) {
		return repository.countDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass);
	}
	
	public long countByApplicationIdAndParentIdAndEntityName(Long applicationId, List<Long> parentId, String parentClass) {
		return repository.countDistinctByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCase(applicationId, parentId, parentClass);
	}
	
	public long getNextEntityIdForNew(){
		return repository.getNextEntityIdForNew();
	}
	
	public void updateParentId(Long applicationId, Long oldParentId, Long newParentId) {
		repository.updateParentId(applicationId, oldParentId, newParentId);
	}
	
	public void deleteAllByApplicationIdAndParentIdAndParentDocClass(Long applicationId, Long parentId, String parentClass) {
		repository.deleteAllByApplicationIdAndParentIdAndParentDocClass(applicationId, parentId, parentClass);
	}
	
	public List<DocumentImage> findAllByApplicationIdAndParentIdAndParentDocAndSource(Long applicationId, Long parentId, String parentClass, DocumentImage.Source source) {
		return repository.findOneByApplicationIdAndParentIdAndParentDocClassAndSource(applicationId, parentId, parentClass, source, new PageRequest(0,1)).getContent();
	}
	
	public DocumentImage findByApplicationIdAndParentIdAndParentDocAndSource(Long applicationId, Long parentId, String parentClass, DocumentImage.Source source) {
		List<DocumentImage> result = repository.findOneByApplicationIdAndParentIdAndParentDocClassAndSource(applicationId, parentId, parentClass, source, new PageRequest(0,1)).getContent();
		if (result.isEmpty()) return null;
		return result.get(0);
	}

	public void deleteAllByApplicationIdAndParentIdAndEntityName(Long applicationId, Long parentId,	String parentEntityName) {
		repository.deleteAllByApplicationIdAndParentIdAndParentDocClass(applicationId, parentId, parentEntityName);
	}

	@Transactional
	public void deleteAllByApplicationIdAndParentIdAndParentDocClassAndSource(Long applicationId,
			Long parentId,
			String parentClass,
			Source source) {
		repository.deleteAllByApplicationIdAndParentIdAndParentDocClassAndSource(applicationId, parentId, parentClass, source);
	}
	
	
	public DocumentImage save(DocumentImage entity) {
		return repository.save(entity);
	}
	
	public DocumentImage findById(Long applicationId, Long id) {
		return repository.findById(applicationId, id);
	}
	
	public void delete(Long applicationId, Long id) {
		repository.deleteById(new ApplicationIdKey(applicationId, id));
	}
	
}
