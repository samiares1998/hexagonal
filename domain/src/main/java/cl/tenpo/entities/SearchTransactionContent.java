package cl.tenpo.entities;

import cl.tenpo.enums.TransactionStatus;
import cl.tenpo.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SearchTransactionContent {
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
    private SearchTransactionContentInvoiceEntity invoice;
    private UUID referenceId;
    private LocalDateTime date;
    private String codeDescription;
    private String externalId;
}
