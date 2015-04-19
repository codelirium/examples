package io.codelirium.examples.facade.service.core;

import org.springframework.stereotype.Service;

@Service(value = NightGreetingsServiceImpl.BEAN_NAME)
public class NightGreetingsServiceImpl implements GreetingsService {

	public final static String BEAN_NAME = "nightGreetingsService";

	@Override
	public String getGreetings(final String name) {
		return "Good night " + name + "!";
	}

	@Override
	public boolean isActive() {
		return Boolean.TRUE;
	}
}