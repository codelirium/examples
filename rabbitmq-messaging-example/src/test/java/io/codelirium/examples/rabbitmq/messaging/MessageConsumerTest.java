package io.codelirium.examples.rabbitmq.messaging;

import io.codelirium.examples.rabbitmq.messaging.consumer.InspectableMessageConsumer;
import io.codelirium.examples.rabbitmq.messaging.util.MessageUtils;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;

import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


@RunWith(MockitoJUnitRunner.class)
public class MessageConsumerTest {

	private static final int MSG_TOTAL_COUNT = 10;
	private static final String MSG_CONTENT_TYPE = "UTF-8";

	private InspectableMessageConsumer messageConsumer;


	@Before
	public void setUp() {
		this.messageConsumer = new InspectableMessageConsumer(MSG_CONTENT_TYPE, MSG_TOTAL_COUNT);
	}


	@Test
	public void testThatConsumerHandlesIncomingMessages() {

		Collection<Message> messages = MessageUtils.generateMessages(MSG_TOTAL_COUNT);

		messages.forEach(messageConsumer::handleMessage);

		assertThat(messages, hasSize(MSG_TOTAL_COUNT));
		assertThat(Lists.newArrayList(MessageUtils.extractMessageIds(messages)), equalTo(Lists.newArrayList(messageConsumer.getDeduplicatedReceiptOrderCollection())));
	}

}
