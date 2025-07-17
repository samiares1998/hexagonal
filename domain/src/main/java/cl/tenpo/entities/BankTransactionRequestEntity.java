package cl.tenpo.entities;

import cl.tenpo.enums.TransactionStatus;
import cl.tenpo.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class BankTransactionRequestEntity {

  private UUID holderId;
  private UUID accountId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private TransactionType type;
  private TransactionStatus status;
  private String[] codeGroups;
  private Integer page;
  private Integer size;
}
