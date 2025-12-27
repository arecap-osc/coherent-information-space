package org.hkrdi.eden.ggm.repository.ggm;

import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDataRepository extends PagingAndSortingRepository<ApplicationData, Long> {

    List<ApplicationData> findAllByApplicationIdAndDataMapIdIn(Long applicationId, List<Long> dataMapIds);
    
    List<ApplicationData> findAllByApplicationIdAndNetwork(Long applicationId, String network);
    
    List<ApplicationData> findAllByApplicationIdAndNetworkAndClusterIndexAndAddressIndex(Long applicationId, String network, Long clusterIndex, Long addressIndex);
    
    List<ApplicationData> findAllByApplicationIdAndNetworkInAndSyntaxContainingIgnoreCase(Long applicationId, List<String> networks, String syntax);
    
    List<ApplicationData> findAllByApplicationIdAndNetworkInAndSemanticContainingIgnoreCase(Long applicationId, List<String> networks, String semantic);
    
    List<ApplicationData> findAllByApplicationIdAndNetworkInAndAddressIndex(Long applicationId, List<String> networks, Long addressIndex);
    
    List<ApplicationData> findAllByApplicationIdAndNetworkInAndToAddressIndex(Long applicationId, List<String> networks, Long toAddressIndex);

    ApplicationData findByApplicationIdAndDataMapId(Long applicationId, Long dataMapId);

}
