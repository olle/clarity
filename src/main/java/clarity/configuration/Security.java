package clarity.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {}

@Configuration
class Production {

  private final Environment env;

  public Production(Environment env) {
    this.env = env;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    if (isDevProfileActive()) {
      http.authorizeHttpRequests(auth -> auth.requestMatchers("/login").permitAll());
    }

    String[] whitelist = {"/favicon.ico", "/assets/**"};

    http.authorizeHttpRequests(
        auth -> {
          auth.requestMatchers(whitelist).permitAll();
          auth.requestMatchers("/index.html").permitAll();
          auth.anyRequest().authenticated();
        });

    if (isDevProfileActive()) {
      http.formLogin(withDefaults());
    }

    return http.build();
  }

  private boolean isDevProfileActive() {
    return env.acceptsProfiles(Profiles.of("dev"));
  }
}

@Profile("dev")
@Configuration
class Development {
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    record UserRolePair(String userNameAndPassword, String authorities) {}

    return new InMemoryUserDetailsManager(
        Stream.of(new UserRolePair("user", "USER"), new UserRolePair("admin", "ADMIN"))
            .map(
                val ->
                    User.withUsername(val.userNameAndPassword)
                        .password(
                            PasswordEncoderFactories.createDelegatingPasswordEncoder()
                                .encode(val.userNameAndPassword))
                        .authorities(val.authorities)
                        .build())
            .toList());
  }
}
