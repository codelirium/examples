package io.codelirium.examples.quartz.wiring.core.manager;

import io.codelirium.examples.quartz.wiring.core.manager.exception.CannotInitializeSchedulerException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(SchedulerManager.BEAN_NAME)
public class SchedulerManager {

	public static final String BEAN_NAME = "schedulerManager";

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerManager.class);

	private Scheduler scheduler;
	private final JobFactory jobFactory;
	private final JobDetail reminderAlertJobDetail;
	private final Trigger reminderAlertTrigger;


	@Inject
	public SchedulerManager(final JobFactory jobFactory, 
							final JobDetail reminderAlertJobDetail,
							final Trigger reminderAlertTrigger) {
		this.scheduler = null;
		this.jobFactory = jobFactory;
		this.reminderAlertJobDetail = reminderAlertJobDetail;
		this.reminderAlertTrigger = reminderAlertTrigger;
	}


	@PostConstruct
	private void init() throws CannotInitializeSchedulerException {

		try {
			scheduler = new StdSchedulerFactory().getScheduler();

			scheduler.setJobFactory(jobFactory);
			scheduler.start();
			scheduler.scheduleJob(reminderAlertJobDetail, reminderAlertTrigger);

			LOGGER.debug("The reminder alert job has been scheduled successfully.");
		} catch (Throwable cause) {
			LOGGER.error(cause.getMessage(), cause);
			throw new CannotInitializeSchedulerException(cause.getMessage(), cause);
		}
	}

	public Scheduler getScheduler() {
		return scheduler;
	}
}
