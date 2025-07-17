package cl.tenpo.specificationPattern;

import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.valueObjects.DateRange;

public class DateRangeSpecification implements TransactionSpecification {
    private final DateRange range;

    public DateRangeSpecification(DateRange range) {
        this.range = range;
    }

    @Override
    public boolean isSatisfiedBy(BankTransactionEntity tx) {
        return range == null || range.includes(tx.getDate().toLocalDate());
    }
}
