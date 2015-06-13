package io.codelirium.examples.rabbitmq.messaging.consumer;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;


public class InspectableMessageConsumer extends MessageConsumer {

	private int expectedConsumption;
	private CountDownLatch latch;
	private Collection<String> deduplicatedReceiptOrderCollection;


	public InspectableMessageConsumer(final String contentType, final int expectedConsumption) {
		super(contentType);
		this.expectedConsumption = expectedConsumption;
		this.latch = new CountDownLatch(expectedConsumption);
		this.deduplicatedReceiptOrderCollection = Collections.synchronizedSet(Sets.newLinkedHashSetWithExpectedSize(expectedConsumption));
	}


	@Override
	public void handleMessage(final Message message) {

		Assert.notNull(message);

		super.handleMessage(message);

		latch.countDown();
		deduplicatedReceiptOrderCollection.add(message.getMessageProperties().getMessageId());
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public Collection<String> getDeduplicatedReceiptOrderCollection() {
		return deduplicatedReceiptOrderCollection;
	}

	public void reset() {
		this.latch = new CountDownLatch(expectedConsumption);
		this.deduplicatedReceiptOrderCollection = Collections.synchronizedSet(Sets.newLinkedHashSetWithExpectedSize(expectedConsumption));
	}
}
