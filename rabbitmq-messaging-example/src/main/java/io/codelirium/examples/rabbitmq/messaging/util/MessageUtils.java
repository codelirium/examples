package io.codelirium.examples.rabbitmq.messaging.util;

import java.util.Collection;
import java.util.stream.LongStream;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;


public class MessageUtils {

	private MessageUtils() {

	}


	public static Collection<Message> generateMessages(final int batchSize) {

		Assert.notNull(batchSize);

		Collection<Message> messages = Lists.newArrayListWithExpectedSize(batchSize);

		LongStream.range(0, batchSize).forEachOrdered( itemId -> {
			messages.add(generateMessage(String.valueOf(itemId)));
		});

		return messages;
	}

	public static Message generateMessage(final String uniqueItemId) {

		Assert.notNull(uniqueItemId);

		MessageProperties properties = MessagePropertiesBuilder.newInstance()
				.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
				.setMessageId(uniqueItemId)
				.build();

		Message message = MessageBuilder
				.withBody(new String("Payload #" + uniqueItemId).getBytes())
				.andProperties(properties)
				.build();

		return message;
	}

	public static Collection<String> extractMessageIds(final Collection<Message> messages) {

		Assert.notNull(messages);

		Collection<String> msgIds = Lists.newArrayListWithExpectedSize(messages.size());
		messages.forEach( message -> msgIds.add(message.getMessageProperties().getMessageId()));

		return msgIds;
	}
}
