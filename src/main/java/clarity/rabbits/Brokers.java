package clarity.rabbits;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class Brokers {

  private static final Logger LOG = LoggerFactory.getLogger(Brokers.class);

  private final BrokerRepository brokerRepository;
  private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  public Brokers(BrokerRepository brokerRepository) {
    this.brokerRepository = brokerRepository;
  }

  @GetMapping("/sse/v0/brokers")
  public SseEmitter subscribe() {

    SseEmitter emitter = new SseEmitter();
    this.emitters.add(emitter);

    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    emitter.onError((e) -> emitters.remove(emitter));

    LOG.info("Added emitter {}", emitter);

    return emitter;
  }

  @Scheduled(fixedDelayString = "10s")
  public void heartbeat() {
    emit("tic-toc");
  }

  @Async
  @EventListener
  public void on(BrokerAddedEvent event) {
    emit(event.toString());
  }

  @Async
  @EventListener
  public void on(BrokerUpdatedEvent event) {
    emit(event.toString());
  }

  protected void emit(String event) {
    final List<SseEmitter> deadEmitters = new ArrayList<>();
    emitters.forEach(
        emitter -> {
          try {
            emitter.send(SseEmitter.event().name("broker").data(event));
            LOG.info("Sent event {}", event);
          } catch (Exception e) {
            deadEmitters.add(emitter);
          }
        });
    emitters.removeAll(deadEmitters);
  }

  @GetMapping(path = "/api/v0/brokers")
  public BrokersDto fetch() {
    return new BrokersDto(brokerRepository.findAll());
  }

  @PostMapping(path = "/api/v0/brokers")
  public void save(BrokerDto broker) {
    brokerRepository.save(broker);
  }

  record BrokersDto(List<Broker> elements) {}

  record BrokerDto(String host, int port, String username, String password, boolean ssl) {}
}
