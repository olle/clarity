package clarity.brokers;

import clarity.infrastructure.utils.Loggable;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrokerController implements Loggable {

  private final BrokerRepository repo;

  public BrokerController(BrokerRepository repo) {
    this.repo = repo;
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    return BrokersDto.from(repo.findAll());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody BrokerDto broker) {
    repo.save(broker);
  }

  @PutMapping(path = "/api/v0/brokers/{id}")
  public void update(@PathVariable(name = "id") UUID id, @RequestBody BrokerDto broker) {
    repo.save(id, broker);
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {
    repo.deleteById(id);
  }

  record BrokersDto(int count, List<BrokerDto> elements) {

    public static BrokersDto from(List<Broker> all) {
      return new BrokersDto(all.size(), all.stream().map(BrokerDto::from).toList());
    }
  }

  record BrokerDto(
      @Nullable UUID id, String host, Integer port, String username, String password, boolean ssl) {

    public static BrokerDto from(Broker broker) {
      return new BrokerDto(
          broker.id,
          broker.properties().host(),
          broker.properties().port(),
          broker.properties().username(),
          broker.properties().password(),
          broker.properties().ssl());
    }
  }
}
