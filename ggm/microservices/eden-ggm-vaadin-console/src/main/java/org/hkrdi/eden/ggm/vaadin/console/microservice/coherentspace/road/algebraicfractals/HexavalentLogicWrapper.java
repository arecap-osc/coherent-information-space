package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals;

import org.hkrdi.eden.ggm.algebraic.*;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HexavalentLogicWrapper extends HexavalentLogic{

	private List<Vertex> cached = null;
	
	private Map<Integer, Vertex> vertexesByIndex = null;
	
	private HexavalentLogic hexavalentLogic;
	
	public HexavalentLogicWrapper(HexavalentLogic hexavalentLogic) {
		super();
		this.hexavalentLogic = hexavalentLogic;
		getVertices();
	}

	@Override
	public int getBeltIndex() {
		return hexavalentLogic.getBeltIndex();
	}
	
	@Override
	public Point getCenter() {
		return hexavalentLogic.getCenter();
	}
	
	@Override
	public ClusterType getClusterType() {
		return hexavalentLogic.getClusterType();
	}
	
	@Override
	public TrivalentLogic getCognitiveInner() {
		return hexavalentLogic.getCognitiveInner();
	}
	
	@Override
	public List<Vertex> getCognitiveInnerVertices() {
		return hexavalentLogic.getCognitiveInnerVertices();
	}
	
	@Override
	public TrivalentLogic getCognitiveOuter() {
		return hexavalentLogic.getCognitiveOuter();
	}
	
	@Override
	public List<Vertex> getCognitiveOuterVertices() {
		return hexavalentLogic.getCognitiveOuterVertices();
	}
	
	@Override
	public List<Vertex> getCognitiveVertices() {
		return hexavalentLogic.getCognitiveVertices();
	}
	
	@Override
	public int getColumn() {
		return hexavalentLogic.getColumn();
	}
	
	@Override
	public List<Connector> getConnectors() {
		return hexavalentLogic.getConnectors();
	}
	
	@Override
	public int getIndex() {
		return hexavalentLogic.getIndex();
	}
	
	@Override
	public List<Vertex> getInnerVertices() {
		return hexavalentLogic.getInnerVertices();
	}
	
	@Override
	public List<Connector> getInnerVerticesConnectors() {
		return hexavalentLogic.getInnerVerticesConnectors();
	}
	
	@Override
	public List<Vertex> getOuterVertices() {
		return hexavalentLogic.getOuterVertices();
	}
	
	@Override
	public List<Connector> getOuterVerticesConnectors() {
		return hexavalentLogic.getOuterVerticesConnectors();
	}
	
	@Override
	public int getRow() {
		return hexavalentLogic.getRow();
	}
	
	@Override
	public TrivalentLogic getSocialInner() {
		return hexavalentLogic.getSocialInner();
	}
	
	@Override
	public List<Vertex> getSocialInnerVertices() {
		return hexavalentLogic.getSocialInnerVertices();
	}
	
	@Override
	public TrivalentLogic getSocialOuter() {
		return hexavalentLogic.getSocialOuter();
	}
	
	@Override
	public List<Vertex> getSocialOuterVertices() {
		return hexavalentLogic.getSocialOuterVertices();
	}
	
	
	@Override
	public List<Vertex> getSocialVertices() {
		return hexavalentLogic.getSocialVertices();
	}

	@Override
	public List<Vertex> getVertices() {
		if (cached == null) {
			cached = hexavalentLogic.getVertices();
			vertexesByIndex = cached.stream().collect(Collectors.toMap(v->v.getIndex(), v->v));
		}
		return cached;
	}
	
	public Vertex getVertexAtIndex(int addressIndex) {
		if (cached == null) {
			getVertices();
		}
		return vertexesByIndex.get(addressIndex);
	}
}
