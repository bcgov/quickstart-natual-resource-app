package ca.bc.gov.nrs.hrs.provider;

import ca.bc.gov.nrs.hrs.dto.base.CodeDescriptionDto;
import ca.bc.gov.nrs.hrs.dto.search.MyForestClientSearchResultDto;
import ca.bc.gov.nrs.hrs.dto.search.ReportingUnitSearchParametersDto;
import ca.bc.gov.nrs.hrs.dto.search.ReportingUnitSearchResultDto;
import ca.bc.gov.nrs.hrs.util.UriUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Provider that forwards requests to the legacy backend API and adapts
 * responses for use in the newer HRS backend.
 *
 * <p>
 * This component centralizes calls to the legacy API for code lists and
 * search endpoints. It applies resilience patterns (circuit breaker)
 * and contains fallback implementations when the legacy system is
 * unavailable. Static fallback data has been centralized in
 * {@link LegacyApiConstants}.
 * </p>
 */
@Slf4j
@Component
@Observed
public class LegacyApiProvider {

  public static final String FALLBACK_ERROR = "Error occurred while fetching data from {}: {}";
  private final RestClient restClient;

  private static final String PROVIDER = "Legacy API";

  LegacyApiProvider(
      @Qualifier("legacyApi") RestClient legacyApi
  ) {
    this.restClient = legacyApi;
  }

  /**
   * Retrieve district code list from the legacy API.
   *
   * <p>
   * Returns a list of {@link CodeDescriptionDto} representing district codes.
   * </p>
   */
  @CircuitBreaker(name = "breaker", fallbackMethod = "fallbackDistricts")
  @NewSpan
  public List<CodeDescriptionDto> getDistrictCodes() {
    log.info("Starting {} request to /codes/districts", PROVIDER);
    return restClient
        .get()
        .uri("/api/codes/districts")
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });
  }

  @SuppressWarnings("unused")
  private List<CodeDescriptionDto> fallbackDistricts(Throwable throwable) {
    logFallbackError(throwable);
    return LegacyApiConstants.DEFAULT_DISTRICTS;
  }

  // Central helper to log fallback errors which avoids repeated log.error calls
  private void logFallbackError(Throwable throwable) {
    log.error(FALLBACK_ERROR, PROVIDER, throwable == null ? "unknown" : throwable.getMessage());
  }

}
