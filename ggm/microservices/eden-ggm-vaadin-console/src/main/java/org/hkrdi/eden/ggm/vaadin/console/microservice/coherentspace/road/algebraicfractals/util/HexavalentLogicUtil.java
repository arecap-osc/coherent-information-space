package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;

import org.hkrdi.eden.ggm.algebraic.HexavalentLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HexavalentLogicUtil {

	public static Map<Integer, List<HexavalentLogic>> getHexavalentLogicFullSpaceAsMapByNodeIndex(List<HexavalentLogic> hexavalentLogicFullSpace){
		Map<Integer, List<HexavalentLogic>> map = new HashMap<>();
		hexavalentLogicFullSpace.stream().forEach(hl->{
			hl.getVertices().forEach(v-> {
					if (map.get(v.getIndex())==null) { 
						map.put(v.getIndex(),  new ArrayList<>());
					}
					map.get(v.getIndex()).add(hl);
					});
		});
		return map;
	}
}
