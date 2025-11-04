package clarity.infrastructure.websocket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clarity.infrastructure.config.ObjectMapperConfig;
import clarity.infrastructure.websocket.JsonTextWebSocketHandler.DomainEventMessage;
import clarity.management.FixtureBrokerAddedEvent;
import clarity.management.events.BrokerAddedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
class SimpleWebSocketHandlerTest {

  @Mock WebSocketSession session;
  @Captor ArgumentCaptor<TextMessage> message;

  @Test
  void convertCamelToKebabCase() {
    assertThat(DomainEventMessage.toKebabCase("OneFineDayAhead")).isEqualTo("one-fine-day-ahead");
  }

  @Test
  void messageIsSentAsJson() throws Exception {

    var sut = new JsonTextWebSocketHandler(new ObjectMapperConfig().objectMapper());

    when(session.getId()).thenReturn(UUID.randomUUID().toString());
    sut.afterConnectionEstablished(session);

    BrokerAddedEvent event = FixtureBrokerAddedEvent.random().get();
    sut.on(event);

    verify(session).sendMessage(message.capture());

    JSONAssert.assertEquals(
        """
        {
          event: 'broker-added-event',
          uuid: '%s',
          timestamp: '%s'
        }
        """
            .formatted(event.uuid(), event.timestamp()),
        message.getValue().getPayload(),
        true);
  }
}
