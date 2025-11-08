package demo;

import clarity.infrastructure.utils.Utils;
import java.time.Instant;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@Profile("demo")
@EnableRabbit
@Configuration
@ComponentScan
public class Demo {

  private static final String DEMO_EXCHANGE = "demo";

  @Autowired RabbitTemplate rabbitTemplate;

  @EventListener
  public void on(ApplicationReadyEvent event) {
    sendDemoMessage("demo.hello.world", "Hello World!");
  }

  @Scheduled(fixedRateString = "PT11S")
  public void scheduled() {
    sendDemoMessage("demo.hello.scheduled", "It's %s and all is well".formatted(Instant.now()));
  }

  private void sendDemoMessage(String routingKey, String message) {
    rabbitTemplate.send(
        DEMO_EXCHANGE, routingKey, MessageBuilder.withBody(Utils.toBytes(message)).build());
  }

  @Bean
  TopicExchange demoTopicExchange() {
    return new TopicExchange(DEMO_EXCHANGE);
  }

  @Bean
  Queue demoQueue() {
    return new Queue(DEMO_EXCHANGE);
  }

  @Bean
  Declarable demoQueueBinding() {
    return BindingBuilder.bind(demoQueue()).to(demoTopicExchange()).with(DEMO_EXCHANGE);
  }
}
