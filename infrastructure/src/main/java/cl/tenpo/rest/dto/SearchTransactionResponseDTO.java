package cl.tenpo.rest.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchTransactionResponseDTO {

  private int page;
  private int size;
  private SearchTransactionContentDTO[] content;
  private int totalElements;
  private int totalPages;
  private boolean last;
}
