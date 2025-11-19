package ca.bc.gov.nrs.hrs.configuration;

import ca.bc.gov.nrs.hrs.dto.base.CodeDescriptionDto;
import ca.bc.gov.nrs.hrs.dto.base.CodeNameDto;
import ca.bc.gov.nrs.hrs.entity.users.UserPreferenceEntity;
import ca.bc.gov.nrs.hrs.exception.NotFoundGenericException;
import ca.bc.gov.nrs.hrs.exception.RequestException;
import ca.bc.gov.nrs.hrs.exception.RetriableException;
import ca.bc.gov.nrs.hrs.exception.TooManyRequestsException;
import ca.bc.gov.nrs.hrs.exception.UnretriableException;
import ca.bc.gov.nrs.hrs.exception.UserNotFoundException;
import ca.bc.gov.nrs.hrs.provider.B3HeaderForwarder;
import ca.bc.gov.nrs.hrs.provider.JwtForwarderRequestInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestClient;

/**
 * Global Spring configuration for the application.
 *
 * <p>This configuration class registers several shared beans used across the
 * application, including REST clients for external services and a Jackson ObjectMapper. It also
 * registers reflection hints required for native image builds via
 * {@code @RegisterReflectionForBinding} and enables JPA auditing.
 * </p>
 *
 * @since 1.0.0
 */
@Configuration
@RegisterReflectionForBinding({
    CodeDescriptionDto.class,
    CodeNameDto.class,
    UserPreferenceEntity.class,
    NotFoundGenericException.class,
    RequestException.class,
    RetriableException.class,
    TooManyRequestsException.class,
    UnretriableException.class,
    UserNotFoundException.class,
})
@EnableJpaAuditing(auditorAwareRef = "databaseAuditor")
public class GlobalConfiguration {

  /**
   * Builds a {@link RestClient} configured to call legacy backend APIs.
   *
   * <p>This client uses the legacy API base address from {@link ApplicationConfiguration}
   * and sets the content type to {@code application/json}. It applies both the
   * {@link JwtForwarderRequestInitializer} and the {@link B3HeaderForwarder} as request
   * initializers so that JWT forwarding and tracing headers are propagated to legacy services.</p>
   *
   * @param configuration application configuration that provides the legacy API address
   * @param jwtForwarder  a request initializer which forwards JWT credentials to the legacy
   *                      backend
   * @param b3Header      a request initializer that forwards B3 trace headers
   * @return a configured {@link RestClient} for legacy APIs
   */
  @Bean
  public RestClient legacyApi(
      ApplicationConfiguration configuration,
      JwtForwarderRequestInitializer jwtForwarder,
      B3HeaderForwarder b3Header
  ) {
    return RestClient
        .builder()
        .baseUrl(configuration.getLegacyApi().getAddress())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .requestInitializer(jwtForwarder)
        .requestInitializer(b3Header)
        .build();
  }

  /**
   * Provides the application's Jackson {@link ObjectMapper} instance.
   *
   * <p>The {@link Jackson2ObjectMapperBuilder} is used to construct and
   * configure the {@code ObjectMapper} according to any customizations applied to the builder
   * elsewhere in the application context.</p>
   *
   * @param builder the Jackson builder used to create the mapper
   * @return a configured {@link ObjectMapper}
   */
  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    return builder.build();
  }

}
