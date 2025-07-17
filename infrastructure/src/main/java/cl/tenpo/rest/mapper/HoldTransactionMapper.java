package cl.tenpo.rest.mapper;

import cl.tenpo.entities.HoldTransactionEntity;
import cl.tenpo.rest.dto.HoldTransactionDTO;

public class HoldTransactionMapper {
    public static HoldTransactionEntity toDomain(HoldTransactionDTO dto) {
        if (dto == null) return null;

        return HoldTransactionEntity.builder()
                .advice(dto.getAdvice())
                .amount(dto.getAmount())
                .cardToken(dto.getCardToken())
                .creationDate(dto.getCreationDate())
                .creditDebitIndicator(dto.getCreditDebitIndicator())
                .currencyCode(dto.getCurrencyCode())
                .customExpirationPeriod(dto.getCustomExpirationPeriod())
                .exchangeRate(dto.getExchangeRate())
                .externalReferenceId(dto.getExternalReferenceId())
                .originalAmount(dto.getOriginalAmount())
                .originalCurrency(dto.getOriginalCurrency())
                .partial(dto.getPartial())
                .referenceDateForExpiration(dto.getReferenceDateForExpiration())
                .source(dto.getSource())
                .status(dto.getStatus())
                .merchantName(dto.getMerchantName())
                .build();
    }
}
