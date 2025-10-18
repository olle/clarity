package clarity.ws;

import clarity.rabbits.BrokerAddedEvent;
import clarity.rabbits.BrokerUpdatedEvent;
import clarity.utils.Loggable;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
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
class SimpleTextWebSocketHandler extends TextWebSocketHandler implements Loggable {

  private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

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
    sendToAllSessions(new TextMessage("now " + Instant.now()));
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

  @EventListener({BrokerAddedEvent.class, BrokerUpdatedEvent.class})
  public void on(Object event) {
    sendToAllSessions(new TextMessage(event.toString()));
  }
}
