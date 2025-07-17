package cl.tenpo.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
@Getter
@Builder
public class SearchTransactionEntity {
    private int page;
    private int size;
    private List<SearchTransactionContent> content;
    private int totalElements;
    private int totalPages;
    private boolean last;
}
