package io.codelirium.examples.facade.service;

import io.codelirium.examples.facade.service.core.GreetingsService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.inject.Inject;

@Service(GreetingsFacadeServiceImpl.BEAN_NAME)
public class GreetingsFacadeServiceImpl implements FacadeService<List<String>> {

	public final static String BEAN_NAME = "greetingsFacadeServiceImpl";

	private final GreetingsService morningGreetingsService;
	private final GreetingsService eveningGreetingsService;
	private final GreetingsService nightGreetingsService;

	@Inject
	public GreetingsFacadeServiceImpl(final GreetingsService morningGreetingsService, final GreetingsService eveningGreetingsService, final GreetingsService nightGreetingsService) {
		this.morningGreetingsService = morningGreetingsService;
		this.eveningGreetingsService = eveningGreetingsService;
		this.nightGreetingsService = nightGreetingsService;
	}

	@Override
	public List<String> delegate(final int argsLength, final Object... args) throws IllegalArgumentException {

		if (args.length != argsLength) {
			throw new IllegalArgumentException(MESSAGE_INVALID_INPUT_ARG_LENGTH);
		}

		final String name;
		try {
			// Although we can pass through counts and objects, we cannot pass type definitions.
			// Maybe another blog post on the use of the Adapter pattern for dynamic type casting
			// like behavior?
			name = (String) args[0];
		} catch (Throwable cause) {
			throw new IllegalArgumentException(MESSAGE_INVALID_INPUT_ARG_TYPES);
		}

		List<String> greetings = Lists.newArrayListWithExpectedSize(3);

		greetings.add(morningGreetingsService.getGreetings(name));
		greetings.add(eveningGreetingsService.getGreetings(name));
		greetings.add(nightGreetingsService.getGreetings(name));

		return greetings;
	}

	@Override
	public Boolean areDelegateServicesActive() {
		return (morningGreetingsService.isActive() && eveningGreetingsService.isActive() && nightGreetingsService.isActive());
	}
}
