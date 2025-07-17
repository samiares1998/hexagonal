package cl.tenpo.rest.dto;

import cl.tenpo.enums.TransactionStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HoldTransactionDTO {
  private Boolean advice;
  private Double amount;
  private String cardToken;
  private LocalDateTime creationDate;
  private String creditDebitIndicator;
  private String currencyCode;
  private Integer customExpirationPeriod;
  private Integer exchangeRate;
  private String externalReferenceId;
  private Integer originalAmount;
  private String originalCurrency;
  private Boolean partial;
  private String referenceDateForExpiration;
  private String source;
  private TransactionStatus status;
  private String merchantName;
}
