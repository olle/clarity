package clarity.brokers.api;

import clarity.brokers.BrokerRepository;
import clarity.brokers.ManageBrokers;
import clarity.brokers.RabbitMqBroker;
import clarity.infrastructure.utils.Loggable;
import java.util.List;
import java.util.UUID;
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
  public FetchAllBrokersResponse fetch() {
    List<RabbitMqBroker> brokers = repo.findAll();
    return new FetchAllBrokersResponse(
        brokers.size(), brokers.stream().map(BrokerDto::from).toList());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody CreateNewManagedBrokerRequest newManagedBrokerRequest) {
    manageBrokers.create(newManagedBrokerRequest.toRabbitMqBroker());
  }

  @PutMapping(path = "/api/v0/brokers/{id}")
  public void update(
      @PathVariable(name = "id") UUID id,
      @RequestBody UpdateManagedBrokerRequest updateManagedBrokerRequest) {
    manageBrokers.update(updateManagedBrokerRequest.validate(id).toRabbitMqBroker());
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {
    manageBrokers.delete(id);
  }
}
