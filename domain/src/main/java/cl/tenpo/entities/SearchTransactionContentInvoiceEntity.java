package cl.tenpo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SearchTransactionContentInvoiceEntity {
  private UUID id;
  private String externalId;
  private String code;
}
