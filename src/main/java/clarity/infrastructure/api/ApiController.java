package clarity.infrastructure.api;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ApiController {

  @Autowired ApplicationContext context;
  @Autowired BeanFactory factory;

  @GetMapping("/api")
  public Map<String, Object> api(HttpServletRequest request) {

    var baseURI =
        ServletUriComponentsBuilder.fromRequest(request)
            .replacePath(null)
            .replaceQuery(null)
            .build()
            .toUriString();

    var controllers = context.getBeansWithAnnotation(RestController.class);

    for (Entry<String, Object> entry : controllers.entrySet()) {

      var beanName = entry.getKey();
      var beanInstance = entry.getValue();
      var beanMethods = beanInstance.getClass().getDeclaredMethods();

      var map = new LinkedHashMap<>();

      map.put("class", (Class<?>) factory.getType(beanName));
      map.put("endpoints", parseEndpoints(beanMethods, baseURI));

      entry.setValue(map);
    }

    return Map.of("controllers", controllers);
  }

  private List<Endpoint> parseEndpoints(Method[] methods, String baseURI) {

    List<Endpoint> endpoints = new ArrayList<>();

    for (Method method : methods) {

      if (method.isAnnotationPresent(GetMapping.class)) {
        endpoints.add(parseGetEndpoint(method, baseURI, method.getAnnotation(GetMapping.class)));

      } else if (method.isAnnotationPresent(PutMapping.class)) {
        endpoints.add(parsePutEndpoint(method, baseURI, method.getAnnotation(PutMapping.class)));

      } else if (method.isAnnotationPresent(PostMapping.class)) {
        endpoints.add(parsePostEndpoint(method, baseURI, method.getAnnotation(PostMapping.class)));

      } else if (method.isAnnotationPresent(DeleteMapping.class)) {
        endpoints.add(
            parseDeleteEndpoint(method, baseURI, method.getAnnotation(DeleteMapping.class)));
      }
    }

    return endpoints;
  }

  private Endpoint parseGetEndpoint(Method method, String baseURI, GetMapping annotation) {
    return new Endpoint(
        "api".equals(method.getName()) ? "self" : method.getName(),
        formatAbsolutePath(
            "%s/%s", baseURI, parsePathAndValueArrays(annotation.path(), annotation.value())));
  }

  private Endpoint parsePutEndpoint(Method method, String baseURI, PutMapping annotation) {
    return parsePutPostDeleteEndpoint(
        method, baseURI, "PUT", parsePathAndValueArrays(annotation.path(), annotation.value()));
  }

  private Endpoint parsePostEndpoint(Method method, String baseURI, PostMapping annotation) {
    return parsePutPostDeleteEndpoint(
        method, baseURI, "POST", parsePathAndValueArrays(annotation.path(), annotation.value()));
  }

  private Endpoint parseDeleteEndpoint(Method method, String baseURI, DeleteMapping annotation) {
    return parsePutPostDeleteEndpoint(
        method, baseURI, "DELETE", parsePathAndValueArrays(annotation.path(), annotation.value()));
  }

  private Endpoint parsePutPostDeleteEndpoint(
      Method method, String baseURI, String httpMethod, String path) {
    return new Endpoint(
        method.getName(), formatAbsolutePath("[" + httpMethod + "] %s/%s", baseURI, path));
  }

  private String formatAbsolutePath(String template, String baseURI, String path) {
    return template.formatted(baseURI, path).replace("//", "/").replace(":/", "://");
  }

  private String parsePathAndValueArrays(String[] path, String[] value) {

    Stream<String> s1 = List.of(path).stream();
    Stream<String> s2 = List.of(value).stream();

    return Stream.concat(s1, s2).filter(StringUtils::hasText).distinct().findFirst().orElse("");
  }

  record Endpoint(String rel, String url) {}
}
