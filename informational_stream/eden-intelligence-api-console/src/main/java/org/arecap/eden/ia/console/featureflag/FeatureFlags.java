package org.arecap.eden.ia.console.featureflag;

import org.arecap.eden.ia.console.boot.BeanUtil;
import org.ff4j.core.Feature;

import java.time.Instant;
import java.util.Date;

public enum FeatureFlags {
	
	NAVIGATOR(new Feature("NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-12-21T00:00:00.000Z"))),
	NL_FEATURE_ROUTE(new Feature("NL_FEATURE_ROUTE".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-12-21T00:00:00.000Z"))),
	NL_SIGNAL_ROUTE(new Feature("NL_SIGNAL_ROUTE".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-12-21T00:00:00.000Z"))),
	LANGUAGE_CHOOSER_COMBOBOX(new Feature("LANGUAGE_CHOOSER_COMBOBOX".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-12-21T00:00:00.000Z")));

	FeatureFlags(Feature featureFlag, Date creationDate) {
		this.featureFlag = featureFlag;
		this.creationDate = creationDate;
	}
	
	private Date creationDate;
	private final Feature featureFlag;
	private boolean checkedIfPresent = false;

	public boolean check() {
		return BeanUtil.getBean(FeatureFlagsService.class).check(this);
	}
	
	public void executeIfChecked(Runnable runnable) {
		if (check()) {
			runnable.run();
		}
	}

	public boolean isCheckedIfPresent() {
		return checkedIfPresent;
	}

	public void setCheckedIfPresent(boolean checkedIfPresent) {
		this.checkedIfPresent = checkedIfPresent;
	}

	public Feature getFeatureFlag() {
		return featureFlag;
	}
	
}
