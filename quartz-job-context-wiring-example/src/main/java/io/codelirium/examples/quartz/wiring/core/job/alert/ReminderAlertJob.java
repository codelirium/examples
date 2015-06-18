package io.codelirium.examples.quartz.wiring.core.job.alert;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReminderAlertJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReminderAlertJob.class);

	public void execute(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
		LOGGER.info("Hello! I am your reminder alert!");
	}
}
