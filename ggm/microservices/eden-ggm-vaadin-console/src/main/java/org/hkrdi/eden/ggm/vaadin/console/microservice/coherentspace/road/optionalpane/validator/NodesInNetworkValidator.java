package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.List;
import java.util.Set;

public class NodesInNetworkValidator extends AbstractValidator<List<Long>> {
	private Set<NodeBean> networkNodes;
	private String network;
	boolean isValid = true;
	String badNodes = "";

	public NodesInNetworkValidator(String errorMessage) {
		super(errorMessage);
	}
	
	public void setup(Set<NodeBean> networkNodes, String network) {
		this.networkNodes = networkNodes;
		this.network = network;
	}

	@Override
	public ValidationResult apply(List<Long> value, ValueContext context) {
		if (value == null || value.size()==0) {
            return toResult(value, true);
        }
		
		if (networkNodes == null || network == null) {
			return ValidationResult.ok();
//			return ValidationResult.error("Trebuie selectata o retea");
		}
		
		isValid = true;
		badNodes = "";
		value.stream().forEach(adr -> {
			if (!networkNodes.contains(new NodeBean(network, adr))) {
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
