package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringToListLongConverter implements Converter<String, List<Long>> {
	@Override
	public Result<List<Long>> convertToModel(String fieldValue, ValueContext context) {
	    // Produces a converted value or an error
	    
		if (fieldValue == null || fieldValue.trim().equals("")) {
			return Result.ok(new ArrayList<>());
		}
		List<Long> result = new ArrayList<>();
		for(String value : fieldValue.split("\\s*,\\s*")){
			try {
				result.add(Long.valueOf(value));
	        } catch (NumberFormatException e) {
	            // error is a static helper method that creates a Result
	            return Result.error("Please enter only numbers and commas");
	        }
		}
		return Result.ok(result);
	}
	
	@Override
	public String convertToPresentation(List<Long> longList, ValueContext context) {
	    // Converting to the field type should always succeed,
	    // so there is no support for returning an error Result.
		if (longList == null || longList.size()==0) {
			return "";
		}
	    return longList.stream().map(l->String.valueOf(l)).collect(Collectors.joining(","));
	}
}
