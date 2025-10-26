package clarity.brokers.api;

import clarity.brokers.BrokerRepository;
import clarity.brokers.BrokerType;
import clarity.brokers.ManageBrokers;
import clarity.infrastructure.utils.Loggable;
import java.util.UUID;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BrokersController implements Loggable {

  private final BrokerRepository repo;
  private final ManageBrokers manageBrokers;

  public BrokersController(BrokerRepository repo, ManageBrokers manageBrokers) {
    this.repo = repo;
    this.manageBrokers = manageBrokers;
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    return BrokersDto.from(repo.findAll());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody BrokerDto newBrokerRequest) {
    assertNoId(newBrokerRequest);
    manageBrokers.create(newBrokerRequest.toRabbitMqBroker(BrokerType.MANAGED));
  }

  @PutMapping(path = "/api/v0/brokers/{id}")
  public void update(
      @PathVariable(name = "id") UUID id, @RequestBody BrokerDto updateBrokerRequest) {
    assertId(id);
    assertSameId(id, updateBrokerRequest);
    manageBrokers.update(updateBrokerRequest.toRabbitMqBroker());
  }

  @PostMapping(path = "/api/v0/brokers/{id}/active")
  public void activate(@PathVariable(name = "id") UUID id, BrokerDto activateBrokerRequest) {
    assertId(id);
    assertSameId(id, activateBrokerRequest);
    manageBrokers.activate(activateBrokerRequest.toRabbitMqBroker());
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {
    manageBrokers.delete(id);
  }

  private void assertNoId(BrokerDto newBrokerRequest) {
    Assert.isNull(newBrokerRequest.id(), "Broker id must be null.");
  }

  private void assertId(UUID id) {
    Assert.notNull(id, "Broker id cannot be null.");
  }

  private void assertSameId(UUID id, BrokerDto updateBrokerRequest) {
    Assert.isTrue(id.equals(updateBrokerRequest.id()), "Must be update for same broker.");
  }
}
