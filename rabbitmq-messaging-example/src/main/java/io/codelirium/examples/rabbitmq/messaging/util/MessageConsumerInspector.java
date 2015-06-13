package io.codelirium.examples.rabbitmq.messaging.util;

import javax.inject.Inject;

import io.codelirium.examples.rabbitmq.messaging.configuration.RabbitMQInspectableConsumerConfiguration;
import io.codelirium.examples.rabbitmq.messaging.consumer.InspectableMessageConsumer;
import io.codelirium.examples.rabbitmq.messaging.strategy.MessageSubmissionStrategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile(RabbitMQInspectableConsumerConfiguration.PROFILE_NAME)
public class MessageConsumerInspector {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSubmissionStrategies.class.getSimpleName());

	private final InspectableMessageConsumer messageConsumer;


	@Inject
	public MessageConsumerInspector(final InspectableMessageConsumer messageConsumer) {
		this.messageConsumer = messageConsumer;
	}


	public void awaitMessageReceiptCompletion() throws InterruptedException {
		messageConsumer.getLatch().await();
	}

	public void displayLastMessageReceiptOrder() throws InterruptedException {
		awaitMessageReceiptCompletion();

		LOGGER.debug("The receipt order was: ");
		messageConsumer.getDeduplicatedReceiptOrderCollection().forEach(itemId -> LOGGER.debug("#{}", itemId));
	}

	public void resetMessageConsumption() throws InterruptedException {
		awaitMessageReceiptCompletion();

		messageConsumer.reset();
	}
}
