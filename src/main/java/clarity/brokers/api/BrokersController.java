package clarity.brokers.api;

import clarity.brokers.BrokerNotFoundException;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.api.NotFoundException;
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
class BrokersController {

  private final AccessBrokers access;
  private final ManageBrokers manage;

  public BrokersController(AccessBrokers access, ManageBrokers manage) {
    this.access = access;
    this.manage = manage;
  }

  @GetMapping(path = "/api/v0/brokers")
  public FetchAllBrokersResponse fetch() {
    List<RabbitMqBroker> brokers = access.findAll();
    return new FetchAllBrokersResponse(
        brokers.size(), brokers.stream().map(BrokerDto::from).toList(), access.findAllRemoved());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody CreateNewManagedBrokerRequest newManagedBrokerRequest) {
    manage.create(newManagedBrokerRequest.toRabbitMqBroker());
  }

  @PutMapping(path = "/api/v0/brokers/{id}")
  public void update(
      @PathVariable(name = "id") UUID id,
      @RequestBody UpdateManagedBrokerRequest updateManagedBrokerRequest) {
    manage.update(updateManagedBrokerRequest.validate(id).toRabbitMqBroker());
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {
    manage.delete(id);
  }

  @PostMapping(path = "/api/v0/brokers/{id}/activate")
  public void activate(
      @PathVariable(name = "id") UUID id, @RequestBody ActivateAndDeactivateBrokerRequest request) {
    try {
      manage.activate(request.validate(id).id());
    } catch (BrokerNotFoundException ex) {
      throw new NotFoundException(ex);
    }
  }

  @PostMapping(path = "/api/v0/brokers/{id}/deactivate")
  public void deactivate(
      @PathVariable(name = "id") UUID id, @RequestBody ActivateAndDeactivateBrokerRequest request) {
    try {
      manage.deactivate(request.validate(id).id());
    } catch (BrokerNotFoundException ex) {
      throw new NotFoundException(ex);
    }
  }
}
