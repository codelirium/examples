package io.codelirium.examples.rabbitmq.messaging.strategy;

import io.codelirium.examples.rabbitmq.messaging.producer.MessageProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public final class MessageSubmissionStrategies {

	private static final int TERM_WAIT_MSEC = 2000;
	private static final int PRODUCER_POOL_SIZE = 4;

	private final RabbitTemplate rabbitTemplate;


	@Inject
	public MessageSubmissionStrategies(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}


	public BatchedMessageSubmissionStrategy strictMessageOrderSubmissionStrategy() {
		return new StrictOrderSubmissionStrategy();
	}

	public BatchedMessageSubmissionStrategy unknownMessageOrderSubmissionStrategy() {
		return new UnknownOrderSubmissionStrategy();
	}


	@Immutable
	public final class StrictOrderSubmissionStrategy implements BatchedMessageSubmissionStrategy {

		private ExecutorService submitter;


		@Override
		public void submit(final long batchSize) throws InterruptedException {

			Assert.notNull(batchSize);

			new MessageProducer(rabbitTemplate).produceMessages(getSubmitter(), batchSize);

			getSubmitter().shutdown();
			getSubmitter().awaitTermination(TERM_WAIT_MSEC, TimeUnit.MILLISECONDS);
			submitter = null;
		}

		@Override
		public ExecutorService getSubmitter() {
			if (submitter == null) {
				submitter = Executors.newSingleThreadExecutor();
			}

			return submitter;
		}
	}

	@Immutable
	public final class UnknownOrderSubmissionStrategy implements BatchedMessageSubmissionStrategy {

		private ExecutorService submitter;


		@Override
		public void submit(final long batchSize) throws InterruptedException {

			Assert.notNull(batchSize);

			new MessageProducer(rabbitTemplate).produceMessages(getSubmitter(), batchSize);

			getSubmitter().shutdown();
			getSubmitter().awaitTermination(TERM_WAIT_MSEC, TimeUnit.MILLISECONDS);
			submitter = null;
		}

		@Override
		public ExecutorService getSubmitter() {
			if (submitter == null) {
				submitter = Executors.newFixedThreadPool(PRODUCER_POOL_SIZE);
			}

			return submitter;
		}
	}
}
