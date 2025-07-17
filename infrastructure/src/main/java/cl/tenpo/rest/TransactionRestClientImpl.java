package cl.tenpo.rest;

import cl.tenpo.entities.BankTransactionRequestEntity;
import cl.tenpo.entities.SearchTransactionEntity;
import cl.tenpo.exception.TenpoException;
import cl.tenpo.ports.TransactionPort;
import cl.tenpo.rest.dto.BankingApi;
import cl.tenpo.rest.dto.SearchTransactionResponseDTO;
import cl.tenpo.rest.mapper.SearchTransactionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import static cl.tenpo.exception.ErrorCode.ERROR_CONSUMING_REQUEST;

@Slf4j
@AllArgsConstructor
@Component
public class TransactionRestClientImpl implements TransactionPort {

  private final RestTemplate restTemplate;
  private final BankingApi props;

  @Override
  public SearchTransactionEntity getTransactionsByHolderID(BankTransactionRequestEntity request) {
    URI url = buildUri(request);

    log.info(
        "[getTransactionsByHolderID] Requesting transactions for holderId: {}",
        request.getHolderId());
    logCurlRequest(url);

    try {
      ResponseEntity<SearchTransactionResponseDTO> response =
          restTemplate.exchange(url, HttpMethod.GET, null, SearchTransactionResponseDTO.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        log.info("Transactions fetched successfully for holderId {}", request.getHolderId());
        return SearchTransactionMapper.toDomain(response.getBody());
      } else {
        log.error("Failed to fetch transactions. HTTP Status: {}", response.getStatusCode());
        throw new TenpoException(
                HttpStatus.BAD_REQUEST, "transactions.error", "Error while consuming getTransactionsByHolderID API");
      }

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      log.error(
          "HTTP error: Status Code: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
      throw new TenpoException(
          HttpStatus.BAD_REQUEST,
          ERROR_CONSUMING_REQUEST,
          cleanErrorMessage(e.getResponseBodyAsString()));
    } catch (Exception e) {
      log.error("Unexpected error during transaction request: {}", e.getMessage(), e);
      throw new TenpoException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              "transactions.error",
              "Unexpected error during getTransactionsByHolderID requesto");
    }
  }

  private URI buildUri(BankTransactionRequestEntity request) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(props.getGetTransaction())
            .queryParam("holder_id", request.getHolderId())
            .queryParam("page", request.getPage())
            .queryParam("size", request.getSize());

    if (request.getStatus() != null) {
      builder.queryParam("statuses", request.getStatus());
    }

    return builder.build().toUri();
  }

  private void logCurlRequest(URI uri) {
    String curl = String.format("curl -X GET \"%s\"", uri.toString());
    log.info("[CURL Request] {}", curl);
  }

  private String cleanErrorMessage(String rawMessage) {
    return Objects.requireNonNullElse(rawMessage, "").replaceAll("[{}\"]", "");
  }
}
