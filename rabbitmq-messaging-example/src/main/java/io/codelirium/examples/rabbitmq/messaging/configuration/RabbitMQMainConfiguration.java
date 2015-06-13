package io.codelirium.examples.rabbitmq.messaging.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQMainConfiguration {

	public final static String QUEUE_NAME = "codelirium-rabbitmq-messaging-example-queue";
	public final static String EXCHANGE_NAME = "codelirium-rabbitmq-messaging-example-exchange";


	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME, false);
	}

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Binding binding(final Queue queue, final TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with(QUEUE_NAME);
	}
}
