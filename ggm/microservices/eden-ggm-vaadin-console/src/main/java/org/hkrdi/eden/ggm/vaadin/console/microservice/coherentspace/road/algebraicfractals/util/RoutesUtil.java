package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;
import org.hkrdi.eden.ggm.algebraic.Vertex;
import org.springframework.data.geo.Point;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RoutesUtil {

	public static List<Point> getAdjacentPoints(HexavalentLogic first, HexavalentLogic second){
		Set<Point> secondVertices = second.getVertices().stream().map(v->v.getPoint()).collect(Collectors.toSet());
		return first.getVertices().stream().filter(v->secondVertices.contains(v.getPoint())).map(v->v.getPoint()).collect(Collectors.toList());
	}
	
	public static List<Vertex> getAdjacentVertex(HexavalentLogic first, HexavalentLogic second){
		Set<Integer> secondVertices = second.getVertices().stream().map(v->v.getIndex()).collect(Collectors.toSet());
		return first.getVertices().stream().filter(v->secondVertices.contains(v.getIndex())).collect(Collectors.toList());
	}
	
	public static Pair<HexavalentLogic, Vertex> getHexavalentLogicContainingAddressIndex(int addressIndex, List<HexavalentLogic> hexavalentLogic) {
		for (HexavalentLogic  hl: hexavalentLogic) {
			Optional<Vertex> found = hl.getVertices().stream().filter(v->v.getIndex() == addressIndex).findFirst();
			if (found.isPresent()) {
				return Pair.of(hl, found.get());
			}
		}
		return null;
	}
	
	
	public static Pair<HexavalentLogic, Vertex> getHexavalentLogicContainingAddressIndex(int addressIndex, List<HexavalentLogic> hexavalentLogic, HexavalentLogic preferredHL) {
		Optional<Vertex> found = preferredHL.getVertices().stream().filter(v->v.getIndex() == addressIndex).findFirst();
		if (found.isPresent()) {
			return Pair.of(preferredHL, found.get());
		}
		return getHexavalentLogicContainingAddressIndex(addressIndex, hexavalentLogic);
	}
	
	public static List<HexavalentLogic> getHexavalentLogicContainingAddressIndex(int addressIndex, Map<Integer, List<HexavalentLogic>> hexavalentLogic) {
		return hexavalentLogic.get(addressIndex);
	}
	
}
