package demo;

import clarity.infrastructure.utils.Utils;
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

@Profile("demo")
@EnableRabbit
@Configuration
@ComponentScan
public class Demo {

  @Autowired RabbitTemplate rabbitTemplate;

  @EventListener
  public void on(ApplicationReadyEvent event) {
    rabbitTemplate.send(
        "demo", "demo.123", MessageBuilder.withBody(Utils.toBytes("Hello World!")).build());
  }

  @Bean
  TopicExchange demoTopicExchange() {
    return new TopicExchange("demo");
  }

  @Bean
  Queue demoQueue() {
    return new Queue("demo");
  }

  @Bean
  Declarable demoQueueBinding() {
    return BindingBuilder.bind(demoQueue()).to(demoTopicExchange()).with("demo");
  }
}
