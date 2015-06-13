package io.codelirium.examples.rabbitmq.messaging.strategy;

import java.util.concurrent.ExecutorService;


public interface BatchedMessageSubmissionStrategy {

	void submit(final long batchSize) throws InterruptedException;

	ExecutorService getSubmitter();
}
