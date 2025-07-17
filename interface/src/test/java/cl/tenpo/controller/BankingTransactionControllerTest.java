package cl.tenpo.controller;

import cl.tenpo.controller.dto.BankTransactionRequestDTO;
import cl.tenpo.controller.dto.BankTransactionResponseDTO;
import cl.tenpo.entities.BankTransactionEntity;
import cl.tenpo.entities.BankTransactionRequestEntity;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.TransactionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BankingTransactionControllerTest {

    @InjectMocks
    private BankingTransactionController bankingTransactionController;

    @Mock
    private TransactionUseCase transactionUseCase;


    @Test
    void testTransaction() {
        // Given
        BankTransactionRequestDTO requestDto =  BankTransactionRequestDTO.builder().build();
        var entity =  BankTransactionEntity.builder().build();
        var entityRequest =  BankTransactionRequestEntity.builder().build();
        PageResult<BankTransactionEntity> entityPage = new PageResult<>(List.of(entity), 1, 1, 1);
        when(transactionUseCase.searchTransactions(entityRequest)).thenReturn(entityPage);

        ResponseEntity<PageResult<BankTransactionResponseDTO>> response = bankingTransactionController.transaction(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }


    @Test
    void testSearchTransactionWithoutFilter() {
        BankTransactionRequestDTO requestDto =  BankTransactionRequestDTO.builder().build();
        var entity =  BankTransactionEntity.builder().build();
        var entityRequest =  BankTransactionRequestEntity.builder().build();
        PageResult<BankTransactionEntity> entityPage = new PageResult<>(List.of(entity), 1, 1, 1);
        when(transactionUseCase.searchTransactionsWithoutFilter(entityRequest)).thenReturn(entityPage);

        ResponseEntity<PageResult<BankTransactionResponseDTO>> response = bankingTransactionController.getSearchTransactions(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}