package cl.tenpo.specificationPattern;

import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.enums.TransactionType;

public class TypeSpecification implements TransactionSpecification{
    private final TransactionType type;

    public TypeSpecification(TransactionType type) {
        this.type = type;
    }

    @Override
    public boolean isSatisfiedBy(BankTransactionEntity tx) {
        return type == null || tx.getType() == type;
    }
}
