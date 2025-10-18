package clarity.rabbits;

import clarity.utils.Loggable;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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

  record BrokersDto(List<Broker> elements) {}

  record BrokerDto(String host, Integer port, String username, String password, boolean ssl) {}
}
