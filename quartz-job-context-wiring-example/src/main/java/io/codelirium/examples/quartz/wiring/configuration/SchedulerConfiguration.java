package io.codelirium.examples.quartz.wiring.configuration;

import io.codelirium.examples.quartz.wiring.core.job.AutowiringSpringBeanJobFactory;

import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {

	@Bean
	public JobFactory autowiringJobFactory(final ApplicationContext applicationContext) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
}
