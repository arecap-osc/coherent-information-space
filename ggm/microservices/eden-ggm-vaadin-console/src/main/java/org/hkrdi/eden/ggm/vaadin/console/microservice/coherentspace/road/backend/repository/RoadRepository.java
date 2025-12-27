package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.repository;

import java.util.List;

import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadRepository extends PagingAndSortingRepository<Road, Long> {
//
//    List<Road> findAllByNetwork(String network);
//
//    Integer countAllByNetwork(String network);
//
//    void deleteAllByNetwork(String network);
//
//    Page<Road> findAllByNetwork(String network, Pageable pageable);
//
//    Page<Road> findAllByNetworkAndTrivalentLogicTypeLike(String network, String trivalentLogicType, Pageable pageable);

//	List<String> findDistinctGroupNameByNetworkAndFractolonIsNotNullOrderByGroupNameAsc(String network);
	
	@Query("SELECT DISTINCT r.groupName from Road r "
            + "WHERE r.network = :network "
            + "AND r.fractolon is not null ")
//            + "ORDER BY r.network, r.orderPosition, r.groupName, r.name, r.road ")
	List<String> findDistinctGroupNameByNetworkAndFractolonIsNotNull(@Param("network") String network);
	
	List<Road> findAllByGroupNameStartsWithIgnoreCaseOrNameStartsWithIgnoreCaseOrderByNetworkAsc(String filterText, String text2);//AndGroupNameAndOrderPositionAsc

	@Query("SELECT r from Road r "
            + "ORDER BY r.network, r.groupName, r.name, r.orderPosition, r.road ")
	List<Road> findAllOrderByNetworkAsc();
	
	List<Road> findAllByNetworkAndGroupNameOrderByNameAsc(String network, String groupName);
	
	List<Road> findAllByNetworkAndGroupNameIsNotNullAndNameIsNotNullOrderByGroupNameAscOrderPositionAscNameAsc(String network);
}