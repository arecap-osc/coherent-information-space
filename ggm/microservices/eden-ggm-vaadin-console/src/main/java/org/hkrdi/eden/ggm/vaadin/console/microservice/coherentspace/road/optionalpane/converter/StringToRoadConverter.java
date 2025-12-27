package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity.Road;

public class StringToRoadConverter implements Converter<Road, String> {
	@Override
	public Result<String> convertToModel(Road fieldValue, ValueContext context) {
	    // Produces a converted value or an error
	    
		if (fieldValue == null) {
			return Result.ok("");
		}
		return Result.ok(fieldValue.getRoad());//.stream().map(l->String.valueOf(l)).collect(Collectors.joining(",")));
	}
	
	@Override
	public Road convertToPresentation(String longList, ValueContext context) {
		Road result = new Road();
		
//		if (longList == null || longList.trim().equals("")) {
			result.setRoad(longList);
//		}else {
//			return longList.stream().map(l->String.valueOf(l)).collect(Collectors.joining(","));
//		}
		return result;
	}
}
