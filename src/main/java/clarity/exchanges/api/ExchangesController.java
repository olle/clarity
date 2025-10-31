package clarity.exchanges.api;

import clarity.exchanges.ExchangeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangesController {

  private final ExchangeRepository repo;

  public ExchangesController(ExchangeRepository repo) {
    this.repo = repo;
  }

  @GetMapping(path = "/api/v0/exchanges")
  public ExchangesDto fetch() {
    return ExchangesDto.from(repo.findAll());
  }
}
