package clarity.brokers;

import clarity.infrastructure.utils.Loggable;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BrokerController implements Loggable {

  private final BrokerRepository repo;

  public BrokerController(BrokerRepository repo) {
    this.repo = repo;
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    return BrokersDto.from(repo.findAll());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody BrokerDto newBrokerRequest) {

    Assert.isNull(newBrokerRequest.id(), "Broker id must be null.");

    repo.save(newBrokerRequest.toRabbitMqBroker(BrokerType.MANAGED));
  }

  @PutMapping(path = "/api/v0/brokers/{id}")
  public void update(
      @PathVariable(name = "id") UUID id, @RequestBody BrokerDto updateBrokerRequest) {

    Assert.notNull(id, "Broker id cannot be null.");
    Assert.isTrue(id.equals(updateBrokerRequest.id()), "Must be update for same broker.");

    repo.save(updateBrokerRequest.toRabbitMqBroker());
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {

    repo.deleteById(id);
  }

  record BrokersDto(int count, List<BrokerDto> elements) {

    public static BrokersDto from(List<RabbitMqBroker> all) {
      return new BrokersDto(all.size(), all.stream().map(BrokerDto::from).toList());
    }
  }

  record BrokerDto(
      @Nullable UUID id,
      @Nullable String type,
      String host,
      Integer port,
      String username,
      String password,
      boolean ssl) {

    public static BrokerDto from(RabbitMqBroker broker) {
      return new BrokerDto(
          broker.id(),
          broker.type().toString(),
          broker.properties().host(),
          broker.properties().port(),
          broker.properties().username(),
          broker.properties().password(),
          broker.properties().ssl());
    }

    public RabbitMqBroker toRabbitMqBroker() {
      return toRabbitMqBroker(BrokerType.valueOf(type));
    }

    public RabbitMqBroker toRabbitMqBroker(BrokerType type) {
      return new RabbitMqBroker(id, type)
          .withProperties(
              mapper ->
                  mapper
                      .withHost(host)
                      .withPort(port)
                      .withUsername(username)
                      .withPassword(password)
                      .withSSL(ssl));
    }
  }
}
