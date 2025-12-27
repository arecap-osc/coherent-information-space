package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator;

import java.util.Arrays;
import java.util.Set;

import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

public class NodesInNetworkStringValidator extends AbstractValidator<String> {
	private Set<NodeBean> networkNodes;
	private String network;
	boolean isValid = true;
	String badNodes = "";

	public NodesInNetworkStringValidator(String errorMessage) {
		super(errorMessage);
	}
	
	public void setup(Set<NodeBean> networkNodes, String network) {
		this.networkNodes = networkNodes;
		this.network = network;
	}

	@Override
	public ValidationResult apply(String value, ValueContext context) {
		if (value == null || "".equals(value)) {
            return toResult(value, true);
        }
		
		if (networkNodes == null || network == null) {
			return ValidationResult.ok();
//			return ValidationResult.error("Trebuie selectata o retea");
		}
		
		isValid = true;
		badNodes = "";
		Arrays.asList(value.split("\\s*,\\s*")).forEach(adr -> {
			if (!networkNodes.contains(new NodeBean(network, Long.valueOf(adr)))) {
				isValid = false;
				badNodes += ("".equals(badNodes)?"":",")+adr;
			}
		});
		if (isValid) {
			return ValidationResult.ok();
		}else {
			return ValidationResult.error(badNodes+" nu "+ (badNodes.indexOf(',')>0?"sunt":"este")+" in retea");
		}
	}

}
