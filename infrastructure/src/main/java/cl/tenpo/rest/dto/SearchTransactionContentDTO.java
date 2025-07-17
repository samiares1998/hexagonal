package cl.tenpo.rest.dto;

import cl.tenpo.enums.TransactionStatus;
import cl.tenpo.enums.TransactionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchTransactionContentDTO {

  private UUID id;
  private UUID holderId;
  private UUID accountId;
  private Double amount;
  private TransactionType type;
  private String currency;
  private String description;
  private Double foreignAmount;
  private String foreignCurrency;
  private String externalTraceId;
  private TransactionStatus status;
  private SearchTransactionContentInvoiceDTO invoice;
  private UUID referenceId;
  private LocalDateTime date;
}
