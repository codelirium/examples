package io.codelirium.examples.rabbitmq.messaging.producer;

import io.codelirium.examples.rabbitmq.messaging.configuration.RabbitMQMainConfiguration;
import io.codelirium.examples.rabbitmq.messaging.util.MessageUtils;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.LongStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;


public class MessageProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class.getSimpleName());

	private Lock lock;
	private String uniqueId;
	private long messageCounter = 0L;
	private RabbitTemplate rabbitTemplate;


	public MessageProducer(final RabbitTemplate rabbitTemplate) {
		this.lock = new ReentrantLock();
		this.uniqueId = UUID.randomUUID().toString();
		this.messageCounter = 0L;
		this.rabbitTemplate = rabbitTemplate;
	}


	public void produceMessages(final ExecutorService submitter, final long batchSize) {

		Assert.notNull(submitter);
		Assert.notNull(batchSize);

		LongStream.range(0, batchSize).forEachOrdered( itemId -> {

			Callable<Void> submissionTask = () -> {

				lock.lock();

				LOGGER.debug("Sending message [#{}]:[#{}]", itemId, messageCounter);
				rabbitTemplate.convertAndSend(RabbitMQMainConfiguration.QUEUE_NAME, MessageUtils.generateMessage(uniqueId + "-" + itemId));
				messageCounter++;

				lock.unlock();

				return null;
			};

			submitter.submit(submissionTask);
		});
	}
}
