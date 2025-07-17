package cl.tenpo.controller.mapper;

import cl.tenpo.controller.dto.BankTransactionRequestDTO;
import cl.tenpo.controller.dto.BankTransactionResponseDTO;
import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.entities.BankTransactionRequestEntity;
import cl.tenpo.shared.pagination.PageResult;
import java.util.List;
import java.util.stream.Collectors;

public class BankingMapper {

    public static BankTransactionRequestEntity dtoToEntity(BankTransactionRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return BankTransactionRequestEntity.builder()
                .holderId(dto.getHolderId())
                .accountId(dto.getAccountId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .type(dto.getType())
                .status(dto.getStatus())
                .codeGroups(dto.getCodeGroups())
                .page(dto.getPage())
                .build();
    }

    public static BankTransactionResponseDTO entityToDto(BankTransactionEntity entity){
        if(entity==null){
            return null;
        }
        return BankTransactionResponseDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .codeDescription(entity.getCodeDescription())
                .description(entity.getDescription())
                .type(entity.getType())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .currencyCode(entity.getCurrencyCode())
                .status(entity.getStatus())
                .externalId(entity.getExternalId())
                .build();
    }

    public static PageResult<BankTransactionResponseDTO> toDtoPage(PageResult<BankTransactionEntity> entityPage) {
        List<BankTransactionResponseDTO> content = entityPage.getContent()
                .stream()
                .map(BankingMapper::entityToDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                content,
                entityPage.getPage(),
                entityPage.getSize(),
                entityPage.getTotalElements()
        );
    }
}
