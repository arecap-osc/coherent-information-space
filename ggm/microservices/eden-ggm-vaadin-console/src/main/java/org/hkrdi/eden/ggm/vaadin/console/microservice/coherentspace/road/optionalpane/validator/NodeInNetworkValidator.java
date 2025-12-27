package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.optionalpane.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;

import java.util.Set;

public class NodeInNetworkValidator extends AbstractValidator<Long> {
	private Set<NodeBean> networkNodes;
	private String network;

	public NodeInNetworkValidator(String errorMessage) {
		super(errorMessage);
	}
	
	public void setup(Set<NodeBean> networkNodes, String network) {
		this.networkNodes = networkNodes;
		this.network = network;
	}

	@Override
	public ValidationResult apply(Long value, ValueContext context) {
		if (value == null) {
            return toResult(value, true);
        }
		if (networkNodes == null || network == null) {
			return ValidationResult.ok();
//			return ValidationResult.error("Trebuie selectata o retea");
		}
        return toResult(value, networkNodes.contains(new NodeBean(network, value)));
	}

}
