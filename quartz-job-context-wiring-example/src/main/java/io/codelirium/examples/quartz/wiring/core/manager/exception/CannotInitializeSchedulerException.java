package io.codelirium.examples.quartz.wiring.core.manager.exception;

public class CannotInitializeSchedulerException extends RuntimeException {

	private static final long serialVersionUID = -1947490878931949284L;

	public CannotInitializeSchedulerException(final String message) {
		super(message);
	}

	public CannotInitializeSchedulerException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
