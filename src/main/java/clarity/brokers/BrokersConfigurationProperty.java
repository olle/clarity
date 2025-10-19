package clarity.brokers;

import clarity.brokers.BrokerRepository.BrokerEntity;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clarity")
public class BrokersConfigurationProperty {

  private List<ConfiguredBroker> brokers;

  @Override
  public String toString() {
    return "BrokersConfigiurationProperty [brokers=" + brokers + "]";
  }

  public List<ConfiguredBroker> getBrokers() {
    return brokers;
  }

  public void setBrokers(List<ConfiguredBroker> brokers) {
    this.brokers = brokers;
  }

  public static class ConfiguredBroker {

    private String host;
    private int port = 5672;
    private String username = "guest";
    private String password = "guest";
    private boolean ssl = false;

    @Override
    public String toString() {
      return "ConfiguredBroker [host="
          + host
          + ", port="
          + port
          + ", username="
          + username
          + ", password="
          + password
          + ", ssl="
          + ssl
          + "]";
    }

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public int getPort() {
      return port;
    }

    public void setPort(int port) {
      this.port = port;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public boolean isSsl() {
      return ssl;
    }

    public void setSsl(boolean ssl) {
      this.ssl = ssl;
    }

    public Broker toModel() {
      return BrokerEntity.from(this).toModel();
    }
  }
}
