package clarity.brokers.api;

import clarity.brokers.BrokerNotFoundException;
import clarity.brokers.domain.RabbitMqBroker;
import clarity.infrastructure.api.NotFoundException;
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

  private final AccessUseCase access;
  private final ManageUseCase manage;
  private final ActivateUseCase activate;

  public BrokersController(AccessUseCase access, ManageUseCase manage, ActivateUseCase activate) {
    this.access = access;
    this.manage = manage;
    this.activate = activate;
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
      @PathVariable(name = "id") UUID id, @RequestBody ActivateBrokerRequest request) {
    try {
      activate.execute(request.validate(id).id());
    } catch (BrokerNotFoundException ex) {
      throw new NotFoundException(ex);
    }
  }
}
