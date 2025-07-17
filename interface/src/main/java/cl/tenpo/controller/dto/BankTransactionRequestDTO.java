package cl.tenpo.controller.dto;

import cl.tenpo.enums.TransactionStatus;
import cl.tenpo.enums.TransactionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankTransactionRequestDTO {

  private @NotNull UUID holderId;
  private @NotNull UUID accountId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private TransactionType type;
  private TransactionStatus status;
  private String[] codeGroups;
  private Integer page;
  private Integer size;
}
