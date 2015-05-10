package io.codelirium.examples.quartz.wiring;

import io.codelirium.examples.quartz.wiring.configuration.alert.ReminderAlertConfiguration;
import io.codelirium.examples.quartz.wiring.core.manager.SchedulerManager;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationSchedulerTests {

	@Inject
	private ApplicationContext appContext;

	private SchedulerManager schedulerManager = null;
	private JobDetail reminderAlertJobDetail = null;
	private Trigger reminderAlertTrigger = null;

	@Before
	public void setUp() throws Exception{
		schedulerManager = getSchedulerManager();
		reminderAlertJobDetail = getReminderAlertJobDetail();
		reminderAlertTrigger = getReminderAlertTrigger();
	}

	@Test
	public void testThatSchedulerManagerIsAutoWired() {

		BeansException beansException = null;

		try {
			schedulerManager = getSchedulerManager();
		} catch (BeansException e) {
			beansException = e;
		}

		assertThat(beansException, is(nullValue()));
		assertThat(schedulerManager, is(not(nullValue())));
	}

	@Test
	public void testThatSchedulerIsActive() throws Exception {
		assumeThat(schedulerManager, is(not(nullValue())));

		assertThat(schedulerManager.getScheduler(), is(not(nullValue())));
		assertTrue(schedulerManager.getScheduler().isStarted());
	}

	@Test
	public void testThatReminderAlertJobIsAutoWired() {

		BeansException beansException = null;

		try {
			reminderAlertJobDetail = getReminderAlertJobDetail();
		} catch (BeansException e) {
			beansException = e;
		}

		assertThat(beansException, is(nullValue()));
		assertThat(reminderAlertJobDetail, is(not(nullValue())));
	}

	@Test
	public void testThatReminderAlertJobIsScheduled() throws Exception {
		assumeThat(schedulerManager, is(not(nullValue())));
		assumeThat(schedulerManager.getScheduler(), is(not(nullValue())));
		assumeThat(reminderAlertJobDetail, is(not(nullValue())));

		assertTrue(schedulerManager.getScheduler().checkExists(reminderAlertJobDetail.getKey()));
	}

	@Test
	public void testThatReminderAlertTriggerIsAutoWired() {

		BeansException beansException = null;

		try {
			reminderAlertTrigger = getReminderAlertTrigger();
		} catch (BeansException e) {
			beansException = e;
		}

		assertThat(beansException, is(nullValue()));
		assertThat(reminderAlertTrigger, is(not(nullValue())));
	}

	@Test
	public void testThatReminderAlertTriggerIsSet() throws Exception {
		assumeThat(schedulerManager, is(not(nullValue())));
		assumeThat(schedulerManager.getScheduler(), is(not(nullValue())));
		assumeThat(reminderAlertTrigger, is(not(nullValue())));

		assertTrue(schedulerManager.getScheduler().checkExists(reminderAlertTrigger.getKey()));
	}


	private SchedulerManager getSchedulerManager() throws BeansException {
		return (SchedulerManager) appContext.getBean(SchedulerManager.BEAN_NAME);
	}

	private JobDetail getReminderAlertJobDetail() throws BeansException {
		return (JobDetail) appContext.getBean(ReminderAlertConfiguration.REMINDER_ALERT_JOB_DETAIL_BEAN_NAME);
	}

	private Trigger getReminderAlertTrigger() throws BeansException {
		return (Trigger) appContext.getBean(ReminderAlertConfiguration.REMINDER_ALERT_TRIGGER_BEAN_NAME);
	}
}
