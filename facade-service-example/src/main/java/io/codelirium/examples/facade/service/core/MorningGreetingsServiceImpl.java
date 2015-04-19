package io.codelirium.examples.facade.service.core;

import org.springframework.stereotype.Service;

@Service(value = MorningGreetingsServiceImpl.BEAN_NAME)
public class MorningGreetingsServiceImpl implements GreetingsService {

	public final static String BEAN_NAME = "morningGreetingsService";

	@Override
	public String getGreetings(final String name) {
		return "Good morning " + name + "!";
	}

	@Override
	public boolean isActive() {
		return Boolean.TRUE;
	}
}