package cl.tenpo.config;

import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.ports.CodePort;
import cl.tenpo.ports.HoldTransactionPort;
import cl.tenpo.ports.TransactionPort;
import cl.tenpo.usecases.*;
import cl.tenpo.usecases.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class UseCaseConfig {
    private final CodePort codePort;
    private final CodeGroupPort codeGroupPort;
    private final TransactionPort transactionPort;
    private final HoldTransactionPort holdTransactionPort;

    @Bean
    public AllCodesUseCase allCodesUseCase() {
        return new AllCodesUseCaseImpl(codePort);
    }

    @Bean
    public CodeCreateUseCase codeCreateUseCase() {
        return new CodeCreateUseCaseImpl(codePort,codeGroupPort);
    }

    @Bean
    public CodeGroupUseCase codeGroupUseCase() {
        return new CodeGroupUseCaseImpl(codePort);
    }

    @Bean
    public CreateCodeGroupUseCase createCodeGroupUseCase() {
        return new CreateCodeGroupUseCaseImpl(codeGroupPort);
    }

    @Bean
    public DeleteCodeGroupUseCase deleteCodeGroupUseCase() {
        return new DeleteCodeGroupUseCaseImpl(codeGroupPort,codePort);
    }

    @Bean
    public DeleteCodeUseCase deleteCodeUseCase() {
        return new DeleteCodeUseCaseImpl(codePort);
    }

    @Bean
    public FindAllCodeGroupsUseCase findAllCodeGroupsUseCase() {
        return new FindAllCodeGroupsUseCaseImpl(codeGroupPort);
    }

    @Bean
    public FindByIdCodeGroupUseCase findByIdCodeGroupUseCase() {
        return new FindByIdCodeGroupUseCaseImpl(codeGroupPort);
    }

    @Bean
    public FindByMambuIdUseCase findByMambuIdUseCase() {
        return new FindByMambuIdUseCaseImpl(codePort);
    }

    @Bean
    public GetAllGroupNamesUseCase getAllGroupNamesUseCase() {
        return new GetAllGroupNamesUseCaseImpl(codeGroupPort);
    }

    @Bean
    public TransactionUseCase transactionUseCase() {
        return new TransactionUseCaseImpl(transactionPort,holdTransactionPort,codePort);
    }

    @Bean
    public UpdateCodeByMambuIdUseCase updateCodeByMambuIdUseCase() {
        return new UpdateCodeByMambuIdUseCaseImpl(codePort);
    }

    @Bean
    public UpdateCodeGroupUseCase updateCodeGroupUseCase() {
        return new UpdateCodeGroupUseCaseImpl(codeGroupPort);
    }

}
