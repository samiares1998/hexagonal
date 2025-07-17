package cl.tenpo.specificationPattern;

import cl.tenpo.entities.BankTransactionEntity;

import java.util.Arrays;
import java.util.List;

public class CodeGroupSpecification implements TransactionSpecification {
    private final List<String> codeGroups;

    public CodeGroupSpecification(String[] codeGroups) {
        this.codeGroups = codeGroups == null ? null : Arrays.asList(codeGroups);
    }

    @Override
    public boolean isSatisfiedBy(BankTransactionEntity tx) {
        return codeGroups == null || codeGroups.contains(tx.getCodeDescription());
    }
}
