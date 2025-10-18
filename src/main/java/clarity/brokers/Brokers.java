package clarity.brokers;

import clarity.infrastructure.utils.Loggable;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Brokers implements Loggable {

  private final BrokerRepository brokerRepository;

  public Brokers(BrokerRepository brokerRepository) {
    this.brokerRepository = brokerRepository;
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    List<Broker> all = brokerRepository.findAll();
    BrokersDto response = new BrokersDto(all);
    logger().info("Response for fetch {}", response);
    return response;
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(@RequestBody BrokerDto broker) {
    logger().info("Received {}", broker);
    brokerRepository.save(broker);
  }

  @DeleteMapping(path = "/api/v0/brokers/{id}")
  public void delete(@PathVariable(name = "id") UUID id) {
    brokerRepository.deleteById(id);
  }

  record BrokersDto(List<Broker> elements) {}

  record BrokerDto(String host, Integer port, String username, String password, boolean ssl) {}
}
