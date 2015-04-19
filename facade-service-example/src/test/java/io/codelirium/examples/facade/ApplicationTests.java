package io.codelirium.examples.facade;

import io.codelirium.examples.facade.service.FacadeService;
import io.codelirium.examples.facade.service.GreetingsFacadeServiceImpl;
import io.codelirium.examples.facade.service.core.EveningGreetingsServiceImpl;
import io.codelirium.examples.facade.service.core.GreetingsService;
import io.codelirium.examples.facade.service.core.MorningGreetingsServiceImpl;
import io.codelirium.examples.facade.service.core.NightGreetingsServiceImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private FacadeService<List<String>> greetingsFacadeService;
	private GreetingsService morningGreetingsService;
	private GreetingsService eveningGreetingsService;
	private GreetingsService nightGreetingService;

	@Before
	public void setUp() {
		morningGreetingsService = new MorningGreetingsServiceImpl();
		eveningGreetingsService = new EveningGreetingsServiceImpl();
		nightGreetingService = new NightGreetingsServiceImpl();

		greetingsFacadeService = new GreetingsFacadeServiceImpl(morningGreetingsService, eveningGreetingsService, nightGreetingService);
	}

	@Test
	public void testThatFacadeServiceIsActive() {
		assertThat(greetingsFacadeService.areDelegateServicesActive(), is(instanceOf(Boolean.class)));
		assertThat(greetingsFacadeService.areDelegateServicesActive(), equalTo(Boolean.TRUE));
	}

	@Test
	public void testThatFacadeServiceThrowsExceptionForInvalidArgLength() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage(FacadeService.MESSAGE_INVALID_INPUT_ARG_LENGTH);
	
		greetingsFacadeService.delegate(2, getGreetingName());
	}

	@Test
	public void testThatFacadeServiceThrowsExceptionForInvalidArgType() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage(FacadeService.MESSAGE_INVALID_INPUT_ARG_TYPES);
	
		greetingsFacadeService.delegate(1, notAString());
	}
	
	@Test
	public void testThatFacadeServiceDelegationResultTypeIsAList() {
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), is(instanceOf(List.class)));
	}

	@Test
	public void testThatFacadeServiceDelegationResultIsNotEmpty() {
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), is(not(empty())));
	}
	@Test
	public void testThatFacadeServiceDelegationResultContainsThreeGreetings() {
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), hasSize(3));
	}

	@Test
	public void testThatFacadeServiceDelegationResultContainsTheExpectedGreetings() {
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), hasItem(morningGreetingsService.getGreetings(getGreetingName())));
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), hasItem(eveningGreetingsService.getGreetings(getGreetingName())));
		assertThat(greetingsFacadeService.delegate(1, getGreetingName()), hasItem(nightGreetingService.getGreetings(getGreetingName())));
	}

	private String getGreetingName() {
		return "codelirium";
	}

	private int notAString() {
		return 12345;
	}
}
