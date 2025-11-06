package clarity.infrastructure.config;

import static org.springframework.security.config.Customizer.withDefaults;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

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

    http.csrf(
        csrf ->
            csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));

    if (isDevProfileActive()) {
      http.formLogin(withDefaults());
    }

    return http.build();
  }

  private boolean isDevProfileActive() {
    return env.acceptsProfiles(Profiles.of("dev"));
  }
}

final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
  private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
  private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
    /*
     * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection
     * of the CsrfToken when it is rendered in the response body.
     */
    this.xor.handle(request, response, csrfToken);
    /*
     * Render the token value to a cookie by causing the deferred token to be
     * loaded.
     */
    csrfToken.get();
  }

  @Override
  public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
    String headerValue = request.getHeader(csrfToken.getHeaderName());
    /*
     * If the request contains a request header, use
     * CsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies when
     * a single-page application includes the header value automatically, which was
     * obtained via a cookie containing the raw CsrfToken.
     *
     * In all other cases (e.g. if the request contains a request parameter), use
     * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
     * when a server-side rendered form includes the _csrf request parameter as a
     * hidden input.
     */
    return (StringUtils.hasText(headerValue) ? this.plain : this.xor)
        .resolveCsrfTokenValue(request, csrfToken);
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
