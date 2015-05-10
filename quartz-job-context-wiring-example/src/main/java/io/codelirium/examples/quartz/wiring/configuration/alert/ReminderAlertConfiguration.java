package io.codelirium.examples.quartz.wiring.configuration.alert;

import io.codelirium.examples.quartz.wiring.core.job.alert.ReminderAlertJob;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.spi.JobFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReminderAlertConfiguration {

	public static final String REMINDER_ALERT_JOB_DETAIL_BEAN_NAME = "remiderAlertJobDetail";

	@Bean(name = REMINDER_ALERT_JOB_DETAIL_BEAN_NAME)
	public JobDetail remiderAlertJobDetail(final JobFactory jobFactory) {
		return JobBuilder.newJob(ReminderAlertJob.class)
						.withIdentity(ReminderAlertJob.class.getSimpleName())
						.build();
	}


	public static final String REMINDER_ALERT_TRIGGER_BEAN_NAME = "reminderAlertTrigger";

	@Bean(name = REMINDER_ALERT_TRIGGER_BEAN_NAME)
	public Trigger reminderAlertTrigger() {
		return TriggerBuilder.newTrigger()
									.forJob(ReminderAlertJob.class.getSimpleName())
									.withSchedule(SimpleScheduleBuilder.simpleSchedule()
													.withIntervalInSeconds(5)
													.repeatForever())
									.build();
	}
}
