package cl.tenpo.factoryPattern;

import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.entities.HoldTransactionEntity;
import cl.tenpo.entities.SearchTransactionContent;
import cl.tenpo.enums.EnumCurrency;
import cl.tenpo.enums.TransactionType;

import java.util.UUID;

public class BankTransactionFactory {
    public static BankTransactionEntity fromHoldTransaction(HoldTransactionEntity hold) {
        return BankTransactionEntity.builder()
                .id(UUID.randomUUID())
                .date(hold.getCreationDate())
                .description(hold.getMerchantName())
                .type(TransactionType.CARD)
                .amount(hold.getAmount())
                .currency(hold.getOriginalCurrency())
                .currencyCode(EnumCurrency.valueOf(hold.getCurrencyCode()))
                .status(hold.getStatus())
                .externalId(hold.getExternalReferenceId())
                .build();
    }

    public static BankTransactionEntity fromSearchContent(SearchTransactionContent content) {
        if (content == null) return null;
        String externalId = content.getInvoice() != null ? content.getInvoice().getExternalId() : null;

        return BankTransactionEntity.builder()
                .id(content.getId())
                .date(content.getDate())
                .externalId(externalId)
                .description(content.getDescription())
                .type(content.getType())
                .amount(content.getAmount())
                .currency(content.getCurrency())
                .currencyCode(EnumCurrency.fromCode(Integer.parseInt(content.getCurrency())))
                .status(content.getStatus())
                .build();
    }
}

