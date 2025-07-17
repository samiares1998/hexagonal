package cl.tenpo.entities;

import cl.tenpo.enums.EnumCurrency;
import cl.tenpo.enums.TransactionStatus;
import cl.tenpo.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BankTransactionEntity {
  private UUID id;
  private LocalDateTime date;
  private String codeDescription;
  private String description;
  private TransactionType type;
  private Double amount;
  private String currency;
  private EnumCurrency currencyCode;
  private TransactionStatus status;
  private String externalId;
}
