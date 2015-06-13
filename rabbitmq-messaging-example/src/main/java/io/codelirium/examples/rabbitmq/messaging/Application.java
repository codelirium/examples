package io.codelirium.examples.rabbitmq.messaging;

import io.codelirium.examples.rabbitmq.messaging.configuration.RabbitMQInspectableConsumerConfiguration;
import io.codelirium.examples.rabbitmq.messaging.strategy.MessageSubmissionStrategies;
import io.codelirium.examples.rabbitmq.messaging.util.MessageConsumerInspector;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(Application.BASE_PACKAGE)
public class Application implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getSimpleName());

	public static final String BASE_PACKAGE = "io.codelirium.examples.rabbitmq.messaging";

	@Inject
	private AnnotationConfigApplicationContext context;

	@Inject
	private MessageSubmissionStrategies messageSubmissionStrategies;

	@Inject
	private MessageConsumerInspector messageConsumerInspector;


	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... args) throws InterruptedException {

		final long batchSize = RabbitMQInspectableConsumerConfiguration.MSG_TOTAL_COUNT;

		LOGGER.info("Total messages per test: {}", batchSize);

		// Slower but with guaranteed message order
		long startTime = System.nanoTime();
		messageSubmissionStrategies.strictMessageOrderSubmissionStrategy().submit(batchSize);
		LOGGER.info("[1 | Ordered]   Production elapsed: {} ms", TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS));
		messageConsumerInspector.awaitMessageReceiptCompletion();
		LOGGER.info("[1 | Ordered]   E2E elapsed: {} ms", TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS));
		messageConsumerInspector.displayLastMessageReceiptOrder();

		LOGGER.debug("");
		messageConsumerInspector.resetMessageConsumption();

		// Faster but with non-guaranteed message order
		startTime = System.nanoTime();
		messageSubmissionStrategies.unknownMessageOrderSubmissionStrategy().submit(batchSize);
		LOGGER.info("[2 | Arbitrary] Production elapsed: {} ms", TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS));
		messageConsumerInspector.awaitMessageReceiptCompletion();
		LOGGER.info("[2 | Arbitrary] E2E elapsed: {} ms", TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS));
		messageConsumerInspector.displayLastMessageReceiptOrder();

		context.close();
		System.exit(0);
	}
}
