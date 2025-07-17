package cl.tenpo.usecases.impl;

import cl.tenpo.factoryPattern.BankTransactionFactory;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.specificationPattern.CodeGroupSpecification;
import cl.tenpo.specificationPattern.DateRangeSpecification;
import cl.tenpo.specificationPattern.TransactionSpecification;
import cl.tenpo.specificationPattern.TypeSpecification;
import cl.tenpo.entities.*;
import cl.tenpo.ports.CodePort;
import cl.tenpo.ports.HoldTransactionPort;
import cl.tenpo.ports.TransactionPort;
import cl.tenpo.usecases.TransactionUseCase;
import cl.tenpo.valueObjects.DateRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TransactionUseCaseImpl implements TransactionUseCase {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 0;
    private static final String UNCONFIGURED_CODE_GROUP = "CodeGroup no configurado";

    private final TransactionPort transactionPort;
    private final HoldTransactionPort holdTransactionPort;
    private final CodePort codePort;

    @Override
    public PageResult<BankTransactionEntity> searchTransactions(BankTransactionRequestEntity request) {
        try {
            if (request.getHolderId() == null) return null;
            PageResult pageResult = new PageResult();
            List<BankTransactionEntity> delorean = fetchAllDeloreanTransactions(request,pageResult);
            enrichWithCodeDescriptions(delorean);

            List<BankTransactionEntity> hold = fetchNonDuplicatedHoldTransactions(request.getAccountId(), delorean);

            List<BankTransactionEntity> all = new ArrayList<>(delorean);
            all.addAll(hold);

            List<TransactionSpecification> specs = List.of(
                    new TypeSpecification(request.getType()),
                    new CodeGroupSpecification(request.getCodeGroups()),
                    new DateRangeSpecification(new DateRange(
                            request.getStartDate() != null ? request.getStartDate().toLocalDate() : null,
                            request.getEndDate() != null ? request.getEndDate().toLocalDate() : null))
            );

            List<BankTransactionEntity> content = all.stream()
                    .filter(tx -> specs.stream().allMatch(spec -> spec.isSatisfiedBy(tx)))
                    .toList();
            pageResult.setContent(content);

            return pageResult;

        } catch (Exception e) {
            log.error(
                    "Error in searchTransactions for holderId {}: {}",
                    request.getHolderId(),
                    e.getMessage(),
                    e);
            throw e;
        }
    }

    @Override
    public PageResult<BankTransactionEntity> searchTransactionsWithoutFilter(BankTransactionRequestEntity request) {
        log.info("[getSearchTransactions] Start - holderId: {}", request.getHolderId());

        try {
            if (request.getHolderId() == null) return null;
            PageResult pageResult = new PageResult();
            List<BankTransactionEntity> delorean = fetchAllDeloreanTransactions(request,pageResult);
            enrichWithCodeDescriptions(delorean);
            if (delorean.isEmpty()) {
                log.info(
                        "[getSearchTransactions] No transactions found for holderId: {}",
                        request.getHolderId());
            } else {
                log.info(
                        "[getSearchTransactions] Returning {} transactions for holderId: {}",
                        delorean.size(),
                        request.getHolderId());
            }
            pageResult.setContent(delorean);
            return pageResult;
        } catch (Exception e) {
            log.error(
                    "[getSearchTransactions] Error while processing transactions for holderId {}: {}",
                    request.getHolderId(),
                    e.getMessage(),
                    e);
            throw e;
        }

    }

    private List<BankTransactionEntity> fetchAllDeloreanTransactions(BankTransactionRequestEntity request, PageResult pageResult) {
        try {
            var req = request.toBuilder()
                    .page(request.getPage() != null && request.getPage() != 0 ? request.getPage() : DEFAULT_PAGE)
                    .size(request.getSize() != null && request.getSize() != 0 ? request.getSize() : DEFAULT_PAGE_SIZE)
                    .build();

            var pageResponse = transactionPort.getTransactionsByHolderID(req);

            if (pageResponse == null || pageResponse.getContent() == null) {
                return Collections.emptyList();
            }

            List<SearchTransactionContent> content = pageResponse.getContent();

            if (content.isEmpty()) return Collections.emptyList();

            pageResult.setTotalPages(pageResponse.getTotalElements());
            pageResult.setPage(pageResponse.getPage());
            pageResult.setSize(pageResponse.getSize());
            pageResult.setTotalElements(pageResponse.getTotalElements());

            return content.stream().map(
                    BankTransactionFactory::fromSearchContent
            ).collect(Collectors.toList());


        } catch (Exception e) {
            log.warn("[fetchAllDeloreanTransactions] Error fetching page {}: {}", 0, e.getMessage());

            log.warn("[fetchAllDeloreanTransactions] No transactions could be fetched due to repeated errors.");
            return Collections.emptyList();
        }
    }

    private void enrichWithCodeDescriptions(List<BankTransactionEntity> txs) {
        List<CodeEntity> codeGroups = codePort.findCodeGroups();
        txs.forEach(tx -> {
            String group = codeGroups.stream()
                    .filter(cg -> String.valueOf(cg.getMambuIdCode()).equals(tx.getExternalId()))
                    .map(CodeEntity::getGroupName)
                    .findFirst()
                    .orElse(UNCONFIGURED_CODE_GROUP);
            tx.setCodeDescription(group);
        });
    }

    private List<BankTransactionEntity> fetchNonDuplicatedHoldTransactions(UUID accountId, List<BankTransactionEntity> existing) {
        List<HoldTransactionEntity> holds = holdTransactionPort.getHoldTransactions(accountId);
        Set<String> existingIds = existing.stream().map(BankTransactionEntity::getExternalId).collect(Collectors.toSet());
        return holds.stream()
                .map(BankTransactionFactory::fromHoldTransaction)
                .filter(tx -> !existingIds.contains(tx.getExternalId()))
                .toList();
    }
}
