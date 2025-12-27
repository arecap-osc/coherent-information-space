package org.hkrdi.eden.ggm.media.boot;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {
    @Value("${env}")
    private String environment;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> getMetricsRegistryCustomizer() {
        return registry -> registry.config().commonTags("env", environment, "service", "eden-ggm-media-api");
    }

    //    pentru metrici custom cu @Timed pe metoda
    @Bean
    public TimedAspect getTimedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
