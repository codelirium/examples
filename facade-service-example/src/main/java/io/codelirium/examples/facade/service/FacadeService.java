package io.codelirium.examples.facade.service;

public interface FacadeService<T> {

	static final String MESSAGE_INVALID_INPUT_ARG_LENGTH = "The required input parameters length does not meet expectations.";
	static final String MESSAGE_INVALID_INPUT_ARG_TYPES = "The required input parameter types do not meet expectations.";

	T delegate(final int argsLength, final Object... args) throws IllegalArgumentException;

	Boolean areDelegateServicesActive();
}
