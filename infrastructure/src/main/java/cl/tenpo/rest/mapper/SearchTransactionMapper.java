package cl.tenpo.rest.mapper;


import cl.tenpo.entities.SearchTransactionContent;
import cl.tenpo.entities.SearchTransactionContentInvoiceEntity;
import cl.tenpo.entities.SearchTransactionEntity;
import cl.tenpo.rest.dto.SearchTransactionContentDTO;
import cl.tenpo.rest.dto.SearchTransactionContentInvoiceDTO;
import cl.tenpo.rest.dto.SearchTransactionResponseDTO;
import java.util.Arrays;
import java.util.List;

public class SearchTransactionMapper {
    public static SearchTransactionEntity toDomain(SearchTransactionResponseDTO dto) {
        if (dto == null) return null;

        return SearchTransactionEntity.builder()
                .page(dto.getPage())
                .size(dto.getSize())
                .content(List.of(Arrays.stream(dto.getContent())
                        .map(SearchTransactionMapper::toDomain)
                        .toArray(SearchTransactionContent[]::new))).totalElements(dto.getTotalElements())
                .totalPages(dto.getTotalPages())
                .last(dto.isLast())
                .build();
    }

    public static SearchTransactionContent toDomain(SearchTransactionContentDTO dto) {
        if (dto == null) return null;

        return SearchTransactionContent.builder().
                id(dto.getId()).
                holderId(dto.getHolderId()).
                accountId(dto.getAccountId()).
                amount(dto.getAmount()).
                type(dto.getType()).
                currency(dto.getCurrency()).
                description(dto.getDescription()).
                foreignAmount(dto.getForeignAmount()).
                foreignCurrency(dto.getForeignCurrency()).
                externalTraceId(dto.getExternalTraceId()).
                status(dto.getStatus()).
                invoice(SearchTransactionMapper.toDomain(dto.getInvoice())).
                referenceId(dto.getReferenceId()).
                date(dto.getDate())
                .build();
    }

    public static SearchTransactionContentInvoiceEntity toDomain(SearchTransactionContentInvoiceDTO dto){
        if (dto == null) return null;

        return SearchTransactionContentInvoiceEntity.builder()
                .id(dto.getId())
                .externalId(dto.getExternalId())
                .code(dto.getCode())
                .build();
    }


}
