package org.hkrdi.eden.ggm.vaadin.console.featureflag;

import java.time.Instant;
import java.util.Date;

import org.ff4j.core.Feature;
import org.springframework.cop.support.BeanUtil;

public enum FeatureFlags {
	
	COHERENT_SPACE_GGM_NAVIGATOR(new Feature("COHERENT_SPACE_GGM_NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-10-22T00:00:00.000Z"))),
	ROAD_NAVIGATOR(new Feature("ROAD_NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-10-22T00:00:00.000Z"))),
	SEMANTIC_MAP_NAVIGATOR(new Feature("SEMANTIC_MAP_NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-10-22T00:00:00.000Z"))),
	FIRST_DEGREE_FEEDBACK_NAVIGATOR(new Feature("FIRST_DEGREE_FEEDBACK_NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-10-22T00:00:00.000Z"))),
	DEDEMAN_LOGO(new Feature("DEDEMAN_LOGO".toLowerCase(), true,""), Date.from(Instant.parse("2019-11-11T00:00:00.000Z"))),
	SV1_MASS_IMPORT_BUTTON(new Feature("SV1_MASS_IMPORT_BUTTON".toLowerCase(), true,""), Date.from(Instant.parse("2019-11-27T00:00:00.000Z"))),
	LANGUAGE_CHOOSER_COMBOBOX(new Feature("LANGUAGE_CHOOSER_COMBOBOX".toLowerCase(), true,""), Date.from(Instant.parse("2019-12-10T00:00:00.000Z"))),
	ROAD_VIEW_NAVIGATOR(new Feature("ROAD_VIEW_NAVIGATOR".toLowerCase(), true,"", "navigator"), Date.from(Instant.parse("2019-11-11T00:00:00.000Z")));

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
