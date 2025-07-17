package cl.tenpo.entities;

import cl.tenpo.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HoldTransactionEntity {
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
