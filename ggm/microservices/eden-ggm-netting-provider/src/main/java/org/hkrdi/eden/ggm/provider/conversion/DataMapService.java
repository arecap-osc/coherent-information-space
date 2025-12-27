package org.hkrdi.eden.ggm.provider.conversion;


import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.repository.DataMapRepository;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class DataMapService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataMapRepository dataMapRepository;

    private static int JPA_SAVE_ALL_CHUNK = 50;

    @PostConstruct
    protected void cleanDataMap() {
        jdbcTemplate.execute("analyze");
    }

    public DataMapRepository getRepository() {
        return dataMapRepository;
    }

    public void convert(NettingType nettingType, int deep, List<HexavalentLogic> logics, Long resolution) {
        AtomicInteger verticesNo = new AtomicInteger();
        logics.forEach(p -> verticesNo.addAndGet(p.getVertices().size()));

        AtomicInteger connectorsNo = new AtomicInteger();
        logics.forEach(p -> connectorsNo.addAndGet(p.getConnectors().size()));

        long time = System.currentTimeMillis();
        System.out.println("Started " + nettingType + "::" + deep + " for clusters=" + logics.stream().count() + " nodes=" + verticesNo.get() + " connectors=" + connectorsNo.get());

        String networkName = nettingType.name() + "::" + deep;
        List<DataMap> network = DataMapConversionUtil.getNetworkDataMap(logics, networkName, deep, resolution);
        if (dataMapRepository.countAllByNetwork(networkName) > 0) {
            dataMapRepository.deleteAllByNetwork(networkName);
        }
        dataMapRepository.saveAll(network);
//                .stream()
//                .map(dataMap -> dataMapRepository.save(dataMap))
//                .collect(Collectors.toList());
//        network.stream().forEach(dataMap -> dataMapRepository.save(dataMap));
//
//        ListUtils.partition(network, JPA_SAVE_ALL_CHUNK).stream()
//                .forEach(networkChunk -> dataMapRepository.saveAll(networkChunk));

//        convertToDataMap(nettingType + "::" + deep, logics, resolution);

        jdbcTemplate.execute("analyze");

        System.out.println("Generated " + nettingType + "::" + deep + " in " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    public void convert(String netting, int deep, List<HexavalentLogic> logics, Long resolution) {
        AtomicInteger verticesNo = new AtomicInteger();
        logics.forEach(p -> verticesNo.addAndGet(p.getVertices().size()));

        AtomicInteger connectorsNo = new AtomicInteger();
        logics.forEach(p -> connectorsNo.addAndGet(p.getConnectors().size()));

        long time = System.currentTimeMillis();
        System.out.println("Started " + netting + "::" + deep + " for clusters=" + logics.stream().count() + " nodes=" + verticesNo.get() + " connectors=" + connectorsNo.get());

        String networkName = netting + "::" + deep;

        List<DataMap> networkDataMaps = DataMapConversionUtil.getNetworkDataMap(logics, networkName, deep, resolution);
        if (dataMapRepository.countAllByNetwork(networkName) == 0) {
            dataMapRepository.saveAll(networkDataMaps);
        } else {
            Map<Long, List<DataMap>> clusterDataMap = networkDataMaps.stream()
                    .collect(Collectors.groupingBy(DataMap::getClusterIndex, HashMap::new, Collectors.toCollection(ArrayList::new)));

            for (Long clusterIndex : clusterDataMap.keySet()) {
                if (dataMapRepository.countAllByNetworkAndClusterIndex(networkName, clusterIndex) == 0) {
                    dataMapRepository.saveAll(clusterDataMap.get(clusterIndex));
                } else {
                    System.out.println("Database already has cluster:\t" + clusterIndex + "\t on network:\t" + netting + "::" + deep);
                }
            }
        }
        jdbcTemplate.execute("analyze");

        System.out.println("Generated " + netting + "::" + deep + " in " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    public void deleteAlByNetwork(String network) {
        dataMapRepository.deleteAllByNetwork(network);
    }


}
