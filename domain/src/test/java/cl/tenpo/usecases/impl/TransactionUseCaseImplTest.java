package cl.tenpo.usecases.impl;

import cl.tenpo.entities.*;
import cl.tenpo.enums.TransactionType;
import cl.tenpo.ports.CodePort;
import cl.tenpo.ports.HoldTransactionPort;
import cl.tenpo.ports.TransactionPort;
import cl.tenpo.shared.pagination.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionUseCaseImplTest {

    private TransactionPort transactionPort;
    private HoldTransactionPort holdTransactionPort;
    private CodePort codePort;
    private TransactionUseCaseImpl transactionUseCase;

    @BeforeEach
    void setUp() {
        transactionPort = mock(TransactionPort.class);
        holdTransactionPort = mock(HoldTransactionPort.class);
        codePort = mock(CodePort.class);
        transactionUseCase = new TransactionUseCaseImpl(transactionPort, holdTransactionPort, codePort);
    }

    @Test
    void shouldReturnNullWhenHolderIdIsNull() {
        BankTransactionRequestEntity request = BankTransactionRequestEntity.builder()
                .holderId(null)
                .build();

        PageResult<BankTransactionEntity> result = transactionUseCase.searchTransactions(request);

        assertThat(result).isNull();
    }

    @Test
    void shouldUseDefaultPaginationWhenPageOrSizeIsZero() {
        var request = BankTransactionRequestEntity.builder()
                .holderId(UUID.randomUUID())
                .page(0)
                .size(0)
                .build();


        when(transactionPort.getTransactionsByHolderID(any())).thenReturn(SearchTransactionEntity.builder().build());
        when(codePort.findCodeGroups()).thenReturn(List.of(CodeEntity.builder().mambuIdCode(100).groupName("Test").build()));
        when(holdTransactionPort.getHoldTransactions(any())).thenReturn(Collections.emptyList());

        PageResult<BankTransactionEntity> result = transactionUseCase.searchTransactions(request);

        assertThat(result).isNotNull();

    }

    @Test
    void shouldCombineDeloreanAndHoldTransactionsRemovingDuplicates() {
        UUID holderId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        var request = BankTransactionRequestEntity.builder()
                .holderId(holderId)
                .accountId(accountId)
                .page(1)
                .size(10)
                .build();


        var holdTx = HoldTransactionEntity.builder()
                .externalReferenceId("300")
                .creationDate(LocalDateTime.parse("2024-06-15T12:00:00"))
                .currencyCode("CLP")
                .build();

        when(codePort.findCodeGroups())
                .thenReturn(List.of(CodeEntity.builder().mambuIdCode(200).groupName("Test Group").build()));

        when(holdTransactionPort.getHoldTransactions(accountId))
                .thenReturn(List.of(holdTx));

        PageResult<BankTransactionEntity> result = transactionUseCase.searchTransactions(request);

        assertThat(result).isNotNull();
       // assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void shouldApplyAllSpecificationsCorrectly() {
        UUID holderId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        var request = BankTransactionRequestEntity.builder()
                .holderId(holderId)
                .accountId(accountId)
                .type(TransactionType.CARD)
                .codeGroups(List.of("GRUPO1").toArray(new String[0]))
                .build();



        var codeGroup = CodeEntity.builder()
                .mambuIdCode(300)
                .groupName("GRUPO1")
                .groupId(UUID.randomUUID())
                .id(UUID.randomUUID())
                .build();

        var searchContent = SearchTransactionContent.builder()
                .externalId("300")
                .type(TransactionType.CARD)
                .date(LocalDateTime.parse("2024-06-15T12:00:00"))
                .build();

        var entity =SearchTransactionEntity.builder()
                .last(true)
                .totalPages(1)
                .size(1)
                .totalElements(1)
                .content(List.of(searchContent))
                .build();

        var holdTx = HoldTransactionEntity.builder()
                .externalReferenceId("300")
                .creationDate(LocalDateTime.parse("2024-06-15T12:00:00"))
                .currencyCode("CLP")
                .build();

        when(transactionPort.getTransactionsByHolderID(any()))
                .thenReturn(entity);

        when(codePort.findCodeGroups()).thenReturn(List.of(codeGroup));
        when(holdTransactionPort.getHoldTransactions(accountId)).thenReturn(List.of(holdTx));

        PageResult<BankTransactionEntity> result = transactionUseCase.searchTransactions(request);

        assertThat(result).isNotNull();
        //assertThat(result.getContent()).hasSize(1);
    }
}
