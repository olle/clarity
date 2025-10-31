package clarity.infrastructure.config;

import clarity.brokers.BrokerRepository;
import clarity.exchanges.ExchangeRepository;
import clarity.infrastructure.repository.InMemoryBrokerRepository;
import clarity.infrastructure.repository.InMemoryExchangeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Repository {

  @Bean
  @ConditionalOnMissingBean
  public BrokerRepository inMemoryBrokerRepository(
      ApplicationEventPublisher applicationEventPublisher) {
    return new InMemoryBrokerRepository(applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  public ExchangeRepository inMemoryExchangeRepository(
      ApplicationEventPublisher applicationEventPublisher) {
    return new InMemoryExchangeRepository(applicationEventPublisher);
  }
}
