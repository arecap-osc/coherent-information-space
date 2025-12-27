package org.hkrdi.eden.ggm.repository.common.documentimage;

import org.hkrdi.eden.ggm.repository.common.ApplicationIdKey;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;


public interface DocumentImageJpaRepository  extends JpaRepository<DocumentImage, ApplicationIdKey> {
	
//	Page<DocumentImage> findDistinctByUserIdAndParentId(Long userId, Long parentId, Pageable pageable);

//	int countDistinctByUserIdAndParentId(Long userId, Long parentId);

	
	
	long countDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(Long applicationId, Long parentId, String parentClass);
	
	long countDistinctByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCase(Long applicationId, List<Long> parentId, String parentClass);

	@Transactional
	Page<DocumentImage> findDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(Long applicationId, Long parentId, String parentClass,
			Pageable pageable);

	@Transactional
	Stream<DocumentImage> findDistinctByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCase(Long applicationId, List<Long> parentId, String parentClass);
	
	@Transactional
	@Modifying
	void deleteByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCaseAndName(Long applicationId, List<Long> parentId, String parentClass, String fileName);
	
	@Transactional
	Stream<DocumentImage> findDistinctByApplicationIdAndParentIdAndParentDocClassLikeIgnoreCase(Long applicationId, Long parentId, String parentClass);
	
	long countByApplicationIdAndParentId(Long applicationId, Long parentId);
	
	@Transactional
	@EntityGraph(value = "documentImage.bringimage" , type=EntityGraphType.FETCH)
	@Query(value="select d from DocumentImage d where d.applicationId=:applicationId and d.parentId=:parentId and d.parentDocClass=:parentDocClass and d.source=:source")
	Page<DocumentImage> findOneByApplicationIdAndParentIdAndParentDocClassAndSource(@Param("applicationId")Long applicationId, @Param("parentId")Long parentId, @Param("parentDocClass")String parentDocClass,@Param("source") DocumentImage.Source source, Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE DocumentImage e " + 
            		"SET e.parentId = :newParentId " +
            		"WHERE e.applicationId = :applicationId AND e.parentId = :oldParentId")
	void updateParentId(
			@Param("applicationId") Long applicationId,
			@Param("oldParentId") Long oldParentId,
			@Param("newParentId") Long newParentId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE from DocumentImage e " + 
            		"WHERE e.applicationId = :applicationId AND e.parentId = :parentId and e.parentDocClass = :parentClass")
	void deleteAllByApplicationIdAndParentIdAndParentDocClass(
			@Param("applicationId") Long applicationId,
			@Param("parentId") Long parentId,
			@Param("parentClass") String parentClass);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE from DocumentImage e " + 
            		"WHERE e.applicationId = :applicationId AND e.parentId = :parentId and e.parentDocClass = :parentClass and e.source= :source")
	void deleteAllByApplicationIdAndParentIdAndParentDocClassAndSource(
			@Param("applicationId") Long applicationId,
			@Param("parentId") Long parentId,
			@Param("parentClass") String parentClass,
			@Param("source") Source source);
	
	@Query(value = "select nextval('myc.hibernate_sequence')",
		   nativeQuery = true)
	public long getNextEntityIdForNew();

	@EntityGraph(value = "documentImage.noJoins" , type=EntityGraphType.FETCH)
	@Query(value="select d from DocumentImage d where d.applicationId=:applicationId and d.name like :name")
	public List<DocumentImage> findByNameLikeAndApplicationId(@Param("name")String name,@Param("applicationId") Long applicationId);
	
	@EntityGraph(value = "documentImage.bringimage" , type=EntityGraphType.FETCH)
	@Query(value="select d from DocumentImage d where d.applicationId=:applicationId and d.name=:name")
	public DocumentImage findByNameAndApplicationId(@Param("name")String name,@Param("applicationId") Long applicationId);
	
	@EntityGraph(value = "documentImage.bringimage" , type=EntityGraphType.FETCH)
	@Query(value="select d from DocumentImage d where d.applicationId=:applicationId and d.id=:id")
	public DocumentImage findById(@Param("applicationId") Long applicationId, @Param("id") Long id);

}
