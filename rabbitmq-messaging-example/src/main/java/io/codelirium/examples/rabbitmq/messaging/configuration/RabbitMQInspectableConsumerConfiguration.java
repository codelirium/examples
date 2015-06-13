package io.codelirium.examples.rabbitmq.messaging.configuration;

import io.codelirium.examples.rabbitmq.messaging.consumer.InspectableMessageConsumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile(RabbitMQInspectableConsumerConfiguration.PROFILE_NAME)
public class RabbitMQInspectableConsumerConfiguration {

	public static final String PROFILE_NAME = "withInspectedConsumption";

	public static final int MSG_TOTAL_COUNT = 100000;
	public static final String MSG_CONTENT_TYPE = "UTF-8";


	@Bean
	public InspectableMessageConsumer messageConsumer() {
		return new InspectableMessageConsumer(MSG_CONTENT_TYPE, MSG_TOTAL_COUNT);
	}
}
