package io.codelirium.examples.rabbitmq.messaging.consumer;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;


public class MessageConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class.getSimpleName());

	private String contentType;


	public MessageConsumer(final String contentType) {
		this.contentType = contentType;
	}


	public void handleMessage(final Message message) {

		Assert.notNull(message);

		try {
			LOGGER.debug("Received message < [#{}]:[{}] >", message.getMessageProperties().getMessageId(), new String(message.getBody(), contentType));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
