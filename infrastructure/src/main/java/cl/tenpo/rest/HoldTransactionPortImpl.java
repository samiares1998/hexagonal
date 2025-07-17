package cl.tenpo.rest;

import cl.tenpo.entities.HoldTransactionEntity;
import cl.tenpo.exception.TenpoException;
import cl.tenpo.ports.HoldTransactionPort;
import cl.tenpo.rest.dto.BankingApi;
import cl.tenpo.rest.dto.HoldTransactionDTO;
import cl.tenpo.rest.mapper.HoldTransactionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static cl.tenpo.exception.ErrorCode.ERROR_CONSUMING_REQUEST;

@Slf4j
@AllArgsConstructor
@Component
public class HoldTransactionPortImpl implements HoldTransactionPort {

  private final RestTemplate restTemplate;
  private final BankingApi props;

  @Override
  public List<HoldTransactionEntity> getHoldTransactions(UUID holderId) {
    String formattedUrl = String.format(props.getGetHoldTransaction(), holderId);

    URI url = UriComponentsBuilder.fromUri(URI.create(formattedUrl)).build().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set(props.getTransactionHoldApiKeyName(), props.getTransactionHoldApiKey());

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    try {
      ResponseEntity<List<HoldTransactionDTO>> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        List<HoldTransactionDTO> dtoList = response.getBody();
        log.info("[getHoldTransactions] Successfully fetched {} transactions", dtoList.size());
        return dtoList.stream().map(HoldTransactionMapper::toDomain).toList();
      }

      log.error("[getHoldTransactions] Unexpected response. Status: {}", response.getStatusCode());
      throw new TenpoException(
          HttpStatus.BAD_REQUEST, ERROR_CONSUMING_REQUEST, "Unexpected response");

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      log.error("Error status {} body {}", e.getStatusCode(), e.getResponseBodyAsString());
      throw new TenpoException(
          HttpStatus.BAD_REQUEST,
          ERROR_CONSUMING_REQUEST,
          e.getResponseBodyAsString().replaceAll("[{}\"]", ""));
    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage());
      throw new TenpoException("Unexpected error during getHoldTransactions", e);
    }
  }
}
