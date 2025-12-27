package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator;

import java.util.List;
import java.util.stream.Stream;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

public class ClustersInNetworkValidator extends AbstractValidator<String> {
	private List<ClusterBean> clusters;
	private String network;
	boolean isValid = true;
	String badClusters = "";

	public ClustersInNetworkValidator(String errorMessage) {
		super(errorMessage);
	}
	
	public void setup(List<ClusterBean> clusters, String network) {
		this.clusters = clusters;
		this.network = network;
	}

	@Override
	public ValidationResult apply(String value, ValueContext context) {
		if (value == null || "".equals(value.trim())) {
            return toResult(value, true);
        }
		
		if (clusters == null || network == null) {
			return ValidationResult.ok();
//			return ValidationResult.error("Trebuie selectata o retea");
		}
		
//		//check for numbers
//		List<Long> result = new ArrayList<>();
//		for(String part : value.split("\\s*,\\s*")){
//			try {
//				Long.valueOf(part);
//	        } catch (NumberFormatException e) {
//	            return ValidationResult.error("Please enter only numbers and commas");
//	        }
//		}
//		//
		
		isValid = true;
		badClusters = "";
		try {
			Stream.of(value.split("\\s*,\\s*")).forEach(adr -> {
					if (!clusters.contains(new ClusterBean(network, Long.valueOf(adr)))) {
						isValid = false;
						badClusters += ("".equals(badClusters)?"":",")+adr;
					}
			});
		} catch (NumberFormatException e) {
			return ValidationResult.error("Please enter only numbers and commas");
		}
		if (isValid) {
			return ValidationResult.ok();
		}else {
			return ValidationResult.error(badClusters+" nu "+ (badClusters.indexOf(',')>0?"sunt":"este")+" in retea");
		}
	}

}
