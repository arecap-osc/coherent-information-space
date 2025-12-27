package org.hkrdi.eden.ggm.repository;

import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.entity.AddressIndexProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataMapRepository extends PagingAndSortingRepository<DataMap, Long> {

    List<DataMap> findAllByNetwork(String network);

    Integer countAllByNetwork(String network);

    void deleteAllByNetwork(String network);

    Page<DataMap> findAllByNetwork(String network, Pageable pageable);

    Page<DataMap> findAllByNetworkAndTrivalentLogicTypeLike(String network, String trivalentLogicType, Pageable pageable);

    @Query(value = "select dm.address_index as addressIndex, dm.to_address_index as toAddressIndex \n" +
            "from data_map dm\n" +
            "inner join application_data ad on ad.data_map_id = dm.data_map_id \n" +
            "where dm.network = :network and ad.application_id = :applicationId \n" +
            "and (ad.semantic != '' or ad.semantic_details != '');", nativeQuery = true)
    List<AddressIndexProjection> findAllNodesContainingInformationByNetwork(@Param("network") String network, @Param("applicationId") Long applicationId);

    @Query(value = "select dm.address_index as addressIndex, dm.to_address_index as toAddressIndex \n" +
            "from data_map dm\n" +
            "inner join application_data ad on ad.data_map_id = dm.data_map_id \n" +
            "where dm.network = :network and ad.application_id = :applicationId \n" +
            "and (ad.syntax != '' or ad.syntax_details != '' or ad.verb != '' or ad.verbalization != '' or ad.where_when != '' or ad.who_what != '' or ad.why != '');", nativeQuery = true)
    List<AddressIndexProjection> findAllEdgesContainingInformationByNetwork(@Param("network") String network, @Param("applicationId") Long applicationId);

    Long countAllByNetworkAndClusterIndex(String network, long clusterIndex);
}
