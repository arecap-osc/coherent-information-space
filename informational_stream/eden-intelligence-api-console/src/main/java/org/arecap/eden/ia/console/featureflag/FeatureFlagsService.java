package org.arecap.eden.ia.console.featureflag;

import org.ff4j.FF4j;
import org.ff4j.exception.FeatureNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagsService {
	@Autowired
	private FF4j ff4j;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureFlagsService.class);
			
	public boolean check(FeatureFlags featureFlags) {
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Feature checking:" + featureFlags.name());
		if (!featureFlags.isCheckedIfPresent()) {
			if (!ff4j.exist(featureFlags.getFeatureFlag().getUid())) {
				ff4j.createFeature(featureFlags.getFeatureFlag());
				LOGGER.info("Feature created:" + featureFlags.name());
			}
			featureFlags.setCheckedIfPresent(true);
		}
		try {
			boolean result = ff4j.check(featureFlags.getFeatureFlag().getUid());
			if (LOGGER.isDebugEnabled()) LOGGER.debug("Feature verified:" + featureFlags.name()+" on " + result);
			return result;
		}catch(FeatureNotFoundException fnfe){
			ff4j.createFeature(featureFlags.getFeatureFlag());
			LOGGER.info("Feature created :" + featureFlags.name());
			boolean result = ff4j.check(featureFlags.getFeatureFlag().getUid());
			if (LOGGER.isDebugEnabled()) LOGGER.debug("Feature verified:" + featureFlags.name()+" on " + result);
			return result;
		}
	}
}
