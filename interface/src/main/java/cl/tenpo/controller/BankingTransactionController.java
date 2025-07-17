package cl.tenpo.controller;

import cl.tenpo.controller.dto.BankTransactionRequestDTO;
import cl.tenpo.controller.dto.BankTransactionResponseDTO;
import cl.tenpo.controller.mapper.BankingMapper;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.TransactionUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/bank")
@AllArgsConstructor
public class BankingTransactionController {

  private final TransactionUseCase transactionUseCase;


  @PostMapping("/transaction")
  public ResponseEntity<PageResult<BankTransactionResponseDTO>> transaction(
          @RequestBody BankTransactionRequestDTO request) {
    /**
     * Obtiene las transacciones del servicio delorean banking/gateway/transaction/search y mapea
     * canal transaccional con DB propia
     */
    PageResult<BankTransactionResponseDTO> response = BankingMapper.toDtoPage(
                             transactionUseCase
                            .searchTransactions(BankingMapper.dtoToEntity(request)));

    return ResponseEntity.ok().body(response);
  }


  @PostMapping("/searchTransaction")
  public ResponseEntity<PageResult<BankTransactionResponseDTO>> getSearchTransactions(
          @RequestBody BankTransactionRequestDTO request) {

    PageResult<BankTransactionResponseDTO> response = BankingMapper.toDtoPage(
             transactionUseCase
            .searchTransactionsWithoutFilter(BankingMapper.dtoToEntity(request)));

    return ResponseEntity.ok().body(response);
  }
}
