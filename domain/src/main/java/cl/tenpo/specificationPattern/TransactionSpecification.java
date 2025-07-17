package cl.tenpo.specificationPattern;

import cl.tenpo.entities.BankTransactionEntity;

public interface TransactionSpecification {
    boolean isSatisfiedBy(BankTransactionEntity tx);
}
