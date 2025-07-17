package cl.tenpo.usecases;

import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.entities.BankTransactionRequestEntity;
import cl.tenpo.shared.pagination.PageResult;

public interface TransactionUseCase {
    PageResult<BankTransactionEntity> searchTransactions(BankTransactionRequestEntity request);

    PageResult<BankTransactionEntity> searchTransactionsWithoutFilter(BankTransactionRequestEntity request);
}
