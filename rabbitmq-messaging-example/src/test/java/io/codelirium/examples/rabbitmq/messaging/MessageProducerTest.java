package io.codelirium.examples.rabbitmq.messaging;

import io.codelirium.examples.rabbitmq.messaging.producer.MessageProducer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;;


@RunWith(MockitoJUnitRunner.class)
public class MessageProducerTest {

	private static final int MSG_TOTAL_COUNT = 10;

	@Mock
	private ExecutorService submitter;

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private MessageProducer messageProducer;


	@Before
	public void setUp() {
		initMocks(this);
	}


	@Test
	public void testThatProducerProcessesMessages() {

		when(submitter.submit((Callable<?>) any(Callable.class))).thenReturn(null);

		messageProducer.produceMessages(submitter, MSG_TOTAL_COUNT);

		verify(submitter, times(MSG_TOTAL_COUNT)).submit((Callable<?>) any(Callable.class));
	}

	@Test
	public void testThatProducerSubmitsMessages() throws InterruptedException {

		doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Callable.class));

		messageProducer.produceMessages(Executors.newSingleThreadExecutor(), MSG_TOTAL_COUNT);

		Thread.sleep(500);

		verify(rabbitTemplate, times(MSG_TOTAL_COUNT)).convertAndSend(anyString(), any(Callable.class));
	}
}