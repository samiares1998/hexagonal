package cl.tenpo.ports;

import cl.tenpo.entities.HoldTransactionEntity;

import java.util.List;
import java.util.UUID;

public interface HoldTransactionPort {
  List<HoldTransactionEntity> getHoldTransactions(UUID accountId);
}
