package org.hkrdi.eden.ggm.provider.conversion;

import org.hkrdi.eden.ggm.algebraic.Connector;
import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.TrivalentLogicType;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.repository.DataMapRepository;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class ConvertorService {

	private static Map<String, Long> map = new ConcurrentHashMap<String, Long>();
	
	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private DataMapRepository dataMapRepo; 

	private void convertToDataMap(String network, List<HexavalentLogic> logics, Long resolution) {

		logics.parallelStream().forEach(logic -> {
			convertToDataMap(network, logic, TrivalentLogicType.COGNITIVE_OUTER, logic.getCognitiveOuterVertices(), resolution);
			convertToDataMap(network, logic, TrivalentLogicType.SOCIAL_OUTER, logic.getSocialOuterVertices(), resolution);
			convertToDataMap(network, logic, TrivalentLogicType.COGNITIVE_INNER, logic.getCognitiveInnerVertices(), resolution);
			convertToDataMap(network, logic, TrivalentLogicType.SOCIAL_INNER, logic.getSocialInnerVertices(), resolution);
		});
	}

	private void convertToDataMap(String network, HexavalentLogic logic, TrivalentLogicType trivalentLogicType, List<Vertex> vertices, Long resolution) {
		vertices.parallelStream().forEach(atVertex -> logic.getConnectors().parallelStream()
				.filter(connector -> connector.getHead().getIndex() == atVertex.getIndex()).
						map(Connector::getTail).forEach(toVertex -> convertAndInsertToDataMap(network, logic, trivalentLogicType, atVertex, toVertex, resolution)));
	}

	private void convertAndInsertToDataMap(String network, HexavalentLogic logic, TrivalentLogicType trivalentLogicType, Vertex atVertex, Vertex toVertex, Long resolution) {
		DataMap dataMap = new DataMap();
		dataMap.setNetwork(network);
		dataMap.setClusterIndex(new Long(logic.getIndex()));
		dataMap.setClusterType(logic.getClusterType().name());
		dataMap.setX(toLongAddress(logic.getCenter().getX()));
		dataMap.setY(toLongAddress(logic.getCenter().getY()));
		dataMap.setResolution(resolution);
		dataMap.setAtX(toLongAddress(atVertex.getPoint().getX()));
		dataMap.setAtY(toLongAddress(atVertex.getPoint().getY()));
		dataMap.setToX(toLongAddress(toVertex.getPoint().getX()));
		dataMap.setToY(toLongAddress(toVertex.getPoint().getY()));
		dataMap.setAddressIndex(new Long(atVertex.getIndex()));
		dataMap.setToAddressIndex(new Long(toVertex.getIndex()));
		dataMap.setTrivalentLogic(atVertex.getType().name());
		dataMap.setTrivalentLogicType(trivalentLogicType.name());
		dataMap.setToTrivalentLogic(toVertex.getType().name());
		dataMap.setToTrivalentLogicType(getTrivalentLogicType(logic, toVertex));
		findAndSaveDataMap(dataMap);
	}

	private String getTrivalentLogicType(HexavalentLogic logic, Vertex vertex) {
		if (logic.getCognitiveOuter().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
			return logic.getCognitiveOuter().getType().name();
		}
		if (logic.getSocialOuter().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
			return logic.getSocialOuter().getType().name();
		}
		if (logic.getCognitiveInner().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
			return logic.getCognitiveInner().getType().name();
		}
		if (logic.getSocialInner().getVertices().parallelStream().filter(v -> v.getIndex() == vertex.getIndex()).count() == 1) {
			return logic.getSocialInner().getType().name();
		}
		return "";
	}

	private void findAndSaveDataMap(DataMap dataMap) {
		dataMapRepo.save(dataMap);
	}

	public void convert(NettingType nettingType, int deep, List<HexavalentLogic> logics, Long resolution){
		AtomicInteger verticesNo = new AtomicInteger();
		logics.forEach(p->verticesNo.addAndGet(p.getVertices().size()));
		
		AtomicInteger connectorsNo = new AtomicInteger();
		logics.forEach(p->connectorsNo.addAndGet(p.getConnectors().size()));
		
		long time = System.currentTimeMillis();
		System.out.println("Started "+nettingType+"::"+deep+" for clusters="+logics.stream().count()+" nodes="+ verticesNo.get()+ " connectors="+connectorsNo.get());

		convertToDataMap(nettingType + "::" + deep, logics, resolution);
		
		jdbc.execute("analyze");
		
		System.out.println("Generated "+nettingType+"::"+deep+" in "+(System.currentTimeMillis()-time)/1000+"s");
	}
	

	public Long toLongAddress(double address) {
		return new Long(Math.round(address*10_000_000_000L));
	}


}
