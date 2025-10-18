package clarity.infrastructure.websocket;

import clarity.brokers.BrokerAddedEvent;
import clarity.brokers.BrokerRemovedEvent;
import clarity.brokers.BrokerUpdatedEvent;
import clarity.infrastructure.DomainEvent;
import clarity.infrastructure.utils.Loggable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
class SimpleWebSocketHandler extends TextWebSocketHandler implements Loggable {

  private final ObjectMapper objectMapper;

  private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

  public SimpleWebSocketHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    synchronized (sessions) {
      sessions.put(session.getId(), session);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    removeSession(session);
  }

  private void removeSession(WebSocketSession session) {
    synchronized (sessions) {
      if (sessions.remove(session.getId()) != null) {
        logger().info("REMOVED {}", session);
      }
      if (sessions.entrySet().removeIf(entry -> !entry.getValue().isOpen())) {
        logger().info("REMOVED!!");
      }
    }
  }

  @Scheduled(fixedDelayString = "30s")
  public void sendPingMessage() {
    sendToAllSessions(new PingMessage());
  }

  @Scheduled(fixedDelayString = "11s")
  public void debug() {
    sendToAllSessions(
        toTextMessage(new DomainEventMessage("now", UUID.randomUUID(), Instant.now())));
  }

  private void sendToAllSessions(WebSocketMessage<?> message) {
    sessions.values().stream()
        .flatMap(session -> sendToSession(message, session).stream())
        .forEach(this::removeSession);
  }

  private Optional<WebSocketSession> sendToSession(
      WebSocketMessage<?> message, WebSocketSession session) {
    try {
      logger().info("Sending {} to {}", message, session);
      session.sendMessage(message);
      return Optional.empty();
    } catch (IOException ex) {
      logger().error("Failed to send %s to  %s".formatted(message, session), ex);
      return Optional.of(session);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    logger().info("Received {} from {}", message, session);
  }

  @Override
  protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    logger().info("Received {} from {}", message, session);
  }

  @EventListener({BrokerAddedEvent.class, BrokerUpdatedEvent.class, BrokerRemovedEvent.class})
  public void on(DomainEvent event) {
    sendToAllSessions(toTextMessage(DomainEventMessage.from(event)));
  }

  private TextMessage toTextMessage(DomainEventMessage message) {
    return message.toJsonTextMessage(objectMapper);
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  record DomainEventMessage(String event, UUID uuid, Instant timestamp) implements Loggable {

    public static DomainEventMessage from(DomainEvent event) {
      return new DomainEventMessage(
          toKebabCase(event.getClass().getSimpleName()), event.uuid(), event.timestamp());
    }

    protected static String toKebabCase(String value) {
      return value
          .replaceAll("([A-Z])(?=[A-Z])", "$1-")
          .replaceAll("([a-z])([A-Z])", "$1-$2")
          .toLowerCase();
    }

    public TextMessage toJsonTextMessage(ObjectMapper objectMapper) {
      try {
        return new TextMessage(objectMapper.writeValueAsBytes(this));
      } catch (JsonProcessingException ex) {
        logger().error("Failed to create JSON text message.", ex);
        throw new RuntimeException(ex);
      }
    }
  }
}
