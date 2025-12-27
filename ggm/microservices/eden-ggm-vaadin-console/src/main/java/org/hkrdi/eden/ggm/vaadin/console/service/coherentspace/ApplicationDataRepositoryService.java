package org.hkrdi.eden.ggm.vaadin.console.service.coherentspace;


import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.ApplicationDataRepository;
import org.hkrdi.eden.ggm.repository.ggm.ApplicationRepository;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class ApplicationDataRepositoryService implements CrudRepositoryService<ApplicationData, Long> {

    @Autowired
    private ApplicationDataRepository applicationDataRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CoherentSpaceService coherentSpaceService;


    @Override
    public CrudRepository<ApplicationData, Long> getRepository() {
        return applicationDataRepository;
    }

    @Override
    public ApplicationData save(ApplicationData entity) {
        return save(entity, false);
    }

    public ApplicationData save(ApplicationData entity, boolean forceSemanticSyntaxSave) {
        ApplicationData applicationData = applicationDataRepository.findById(entity.getId()).get();
        DataMap dataMap = coherentSpaceService.getNetworkDataMaps(entity.getNetwork()).stream()
                .filter(DataMapFilterUtil.byId(entity.getDataMapId())).findFirst().get();
        entity.setWells(null);
        entity.setRoutes(null);

        if (nodeSemanticChanged(entity, applicationData) || forceSemanticSyntaxSave) {
            entity.setWells(saveSemantic(entity.getApplication().getId(), entity, dataMap.getAtX(), dataMap.getAtY()));
        } else if (syntaxChanged(entity, applicationData) || phraseAnalysisChanged(entity, applicationData) || forceSemanticSyntaxSave) {
            entity.setRoutes(saveSyntaxAndPhraseAnalysis(entity.getApplication().getId(), entity, dataMap.getAtX(), dataMap.getAtY(), dataMap.getToX(), dataMap.getToY()));
        }

        return entity;
    }

    private boolean syntaxChanged(ApplicationData entity, ApplicationData applicationData) {
        return entity.getSyntax().equalsIgnoreCase(applicationData.getSyntax()) == false ||
                Optional.ofNullable(entity.getSyntaxDetails()).orElse("")
                        .equalsIgnoreCase(Optional.ofNullable(applicationData.getSyntaxDetails()).orElse("")) == false;
    }

    private boolean nodeSemanticChanged(ApplicationData entity, ApplicationData applicationData) {
        return entity.getSemantic().equalsIgnoreCase(applicationData.getSemantic()) == false ||
                Optional.ofNullable(entity.getSemanticDetails()).orElse("")
                        .equalsIgnoreCase(Optional.ofNullable(applicationData.getSemanticDetails()).orElse("")) == false;
    }

    private boolean phraseAnalysisChanged(ApplicationData entity, ApplicationData applicationData) {
        return entity.getWhoWhat().equalsIgnoreCase(applicationData.getWhoWhat()) == false ||
                entity.getHow().equalsIgnoreCase(applicationData.getHow()) == false ||
                entity.getWhy().equalsIgnoreCase(applicationData.getWhy()) == false ||
                entity.getWhereWhen().equalsIgnoreCase(applicationData.getWhereWhen()) == false;
    }

    private List<DataMap> saveSyntaxAndPhraseAnalysis(Long applicationId, ApplicationData entity, Long x, Long y, Long tox, Long toy) {
        Application application = applicationRepository.findById(applicationId).get();
        AtomicInteger cntTotal = new AtomicInteger(0);
        List<DataMap> updatedSemantics = new ArrayList<>();
        coherentSpaceService.getAvailableNetworks().stream().forEach(network -> {
            coherentSpaceService
                    .getNetworkDataMaps(network)
                    .stream()
                    .filter(DataMapFilterUtil.byRoute(x, y, tox, toy)).forEach(dataMap -> {
                cntTotal.incrementAndGet();
                ApplicationData applicationData = applicationDataRepository
                        .findByApplicationIdAndDataMapId(application.getId(), dataMap.getId());
                if (applicationData == null) {
                    createAndInsertApplicationData(application, dataMap, entity);
                } else {
                    applicationData.setSemantic(entity.getSemantic());
                    applicationData.setSemanticDetails(entity.getSemanticDetails());
                    applicationData.setSyntax(entity.getSyntax());
                    applicationData.setSyntaxDetails(entity.getSyntaxDetails());
                    applicationData.setVerb(entity.getVerb());
                    applicationData.setConjunction(entity.getConjunction());
                    applicationData.setVerbalization(entity.getVerbalization());
                    applicationData.setPhrase(entity.getPhrase());
                    applicationData.setVerbDetails(entity.getVerbDetails());
                    applicationData.setWhoWhat(entity.getWhoWhat());
                    applicationData.setHow(entity.getHow());
                    applicationData.setWhy(entity.getWhy());
                    applicationData.setWhereWhen(entity.getWhereWhen());
                    applicationDataRepository.save(applicationData);
                }
                updatedSemantics.add(dataMap);
            });
        });
//        System.out.println("total at x:\t"+x+"\ty:\t"+y+"\ttotal cnt:\t"+cntTotal.get());
        return updatedSemantics;
    }

    private List<DataMap> saveSemantic(Long applicationId, ApplicationData entity, Long x, Long y) {
        Application application = applicationRepository.findById(applicationId).get();
        AtomicInteger cntTotal = new AtomicInteger(0);
        List<DataMap> updatedSemantics = new ArrayList<>();
        coherentSpaceService.getAvailableNetworks().stream().forEach(network -> {
            coherentSpaceService
                    .getNetworkDataMaps(network)
                    .stream()
                    .filter(DataMapFilterUtil.byAddressesAt(x, y)).forEach(dataMap -> {
                cntTotal.incrementAndGet();
                ApplicationData applicationData = applicationDataRepository
                        .findByApplicationIdAndDataMapId(application.getId(), dataMap.getId());
                if (applicationData == null) {
                    createAndInsertApplicationData(application, dataMap, entity);
                } else {
                    applicationData.setSemantic(entity.getSemantic());
                    applicationData.setSemanticDetails(entity.getSemanticDetails());
                    applicationDataRepository.save(applicationData);
                }
                updatedSemantics.add(dataMap);
            });
        });
//        System.out.println("total at x:\t"+x+"\ty:\t"+y+"\ttotal cnt:\t"+cntTotal.get());
        return updatedSemantics;
    }

    public List<ApplicationData> findApplicationDataForNetwork(Long applicationId, String network) {
        return applicationDataRepository.findAllByApplicationIdAndNetwork(applicationId, network);
    }

    public List<ApplicationData> findApplicationDataWithoutSave(Long applicationId, String network, Long clusterIndex, Long addressIndex) {
        return applicationDataRepository.findAllByApplicationIdAndNetworkAndClusterIndexAndAddressIndex(applicationId, network, clusterIndex, addressIndex);
    }

    public List<ApplicationData> findByContainingSemanticOrSyntaxIgnoreCase(Long applicationId, List<String> networks, String filter) {
        List<ApplicationData> result = applicationDataRepository.findAllByApplicationIdAndNetworkInAndSemanticContainingIgnoreCase(applicationId, networks, "%" + filter + "%");
        result.addAll(applicationDataRepository.findAllByApplicationIdAndNetworkInAndSyntaxContainingIgnoreCase(applicationId, networks, "%" + filter + "%"));
        return result;
    }

    public List<ApplicationData> findByContainingSyntaxIgnoreCase(Long applicationId, List<String> networks, String filter) {
        return applicationDataRepository.findAllByApplicationIdAndNetworkInAndSyntaxContainingIgnoreCase(applicationId, networks, "%" + filter + "%");
    }

    public List<ApplicationData> findByContainingSemanticIgnoreCase(Long applicationId, List<String> networks, String filter) {
        return applicationDataRepository.findAllByApplicationIdAndNetworkInAndSemanticContainingIgnoreCase(applicationId, networks, "%" + filter + "%");
    }

    public List<ApplicationData> findByNodeIndexIndex(Long applicationId, List<String> networks, String filter) {
        Long index = 0l;
        try {
            index = new Long(filter);
        } catch (NumberFormatException e) {
            //do nothing
        }
        List<ApplicationData> result = applicationDataRepository.findAllByApplicationIdAndNetworkInAndAddressIndex(applicationId, networks, index);
        result.addAll(applicationDataRepository.findAllByApplicationIdAndNetworkInAndToAddressIndex(applicationId, networks, index));
        return result;
    }

    public List<ApplicationData> findByAddressIndex(Long applicationId, List<String> networks, Long addressIndex) {
        return applicationDataRepository.findAllByApplicationIdAndNetworkInAndAddressIndex(applicationId, networks, addressIndex);
    }

    public ApplicationData getApplicationData(Long applicationId, DataMap dataMap) {
        return Optional
                .ofNullable(applicationDataRepository.findByApplicationIdAndDataMapId(applicationId, dataMap.getId()))
                .orElseGet(() -> {
                    Application application = applicationRepository.findById(applicationId).get();
                    return createAndInsertApplicationData(application, dataMap, createEmptyEntity());
                });
    }

    public List<ApplicationData> findAllByApplicationIdAndDataMapIdIn(Long applicationId, List<Long> dataMapIds) {
        return applicationDataRepository.findAllByApplicationIdAndDataMapIdIn(applicationId, dataMapIds);
    }

    private ApplicationData createAndInsertApplicationData(Application application, DataMap dataMap, ApplicationData entity) {
        ApplicationData applicationData = new ApplicationData();
        applicationData.setApplication(application);
        applicationData.setDataMapId(dataMap.getId());
        applicationData.setNetwork(dataMap.getNetwork());
        applicationData.setClusterIndex(dataMap.getClusterIndex());
        applicationData.setAddressIndex(dataMap.getAddressIndex());
        applicationData.setToAddressIndex(dataMap.getToAddressIndex());
        applicationData.setSemantic(entity.getSemantic());
        applicationData.setSemanticDetails(entity.getSemanticDetails());
        applicationData.setSyntax(entity.getSyntax());
        applicationData.setSyntaxDetails(entity.getSyntaxDetails());
        applicationData.setVerb(entity.getVerb());
        applicationData.setConjunction(entity.getConjunction());
        applicationData.setVerbalization(entity.getVerbalization());
        applicationData.setPhrase(entity.getPhrase());
        applicationData.setVerbDetails(entity.getVerbDetails());
        applicationData.setWhoWhat(entity.getWhoWhat());
        applicationData.setHow(entity.getHow());
        applicationData.setWhy(entity.getWhy());
        applicationData.setWhereWhen(entity.getWhereWhen());
        applicationData.setSyntaxDone(false);
        return applicationDataRepository.save(applicationData);
    }

    private ApplicationData createEmptyEntity() {
        ApplicationData applicationData = new ApplicationData();
        applicationData.setSemantic("");
        applicationData.setSyntax("");
        applicationData.setWhoWhat("");
        applicationData.setHow("");
        applicationData.setWhy("");
        applicationData.setWhereWhen("");
        applicationData.setSyntaxDone(false);
        return applicationData;
    }

}
