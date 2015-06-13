package io.codelirium.examples.rabbitmq.messaging.configuration;

import java.util.concurrent.Executors;

import io.codelirium.examples.rabbitmq.messaging.consumer.MessageConsumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConsumerConfiguration {

	private static final int CONSUMER_POOL_SIZE = 4;


	@Bean
	public MessageListenerAdapter messageListenerAdapter(final MessageConsumer messageConsumer) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(messageConsumer, MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD);

		messageListenerAdapter.setMessageConverter(null);

		return messageListenerAdapter;
	}

	@Bean
	public SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory, final MessageListenerAdapter messageListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(RabbitMQMainConfiguration.QUEUE_NAME);
		container.setMessageListener(messageListenerAdapter);
		container.setMaxConcurrentConsumers(CONSUMER_POOL_SIZE);
		container.setTaskExecutor(Executors.newFixedThreadPool(CONSUMER_POOL_SIZE));

		return container;
	}
}
