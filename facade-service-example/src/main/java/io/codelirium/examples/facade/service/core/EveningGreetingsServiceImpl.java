package io.codelirium.examples.facade.service.core;

import org.springframework.stereotype.Service;

@Service(EveningGreetingsServiceImpl.BEAN_NAME)
public class EveningGreetingsServiceImpl implements GreetingsService {

	public final static String BEAN_NAME = "eveningGreetingsService";

	@Override
	public String getGreetings(final String name) {
		return "Good evening " + name + "!";
	}

	@Override
	public boolean isActive() {
		return Boolean.TRUE;
	}
}
