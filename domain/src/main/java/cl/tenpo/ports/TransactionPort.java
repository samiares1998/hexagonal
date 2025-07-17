package cl.tenpo.ports;

import cl.tenpo.entities.BankTransactionRequestEntity;
import cl.tenpo.entities.SearchTransactionEntity;

public interface TransactionPort {
  SearchTransactionEntity getTransactionsByHolderID(BankTransactionRequestEntity request);
}
