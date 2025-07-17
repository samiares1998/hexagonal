package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.GetAllGroupNamesUseCase;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetAllGroupNamesUseCaseImpl implements GetAllGroupNamesUseCase {
    private final CodeGroupPort codeGroupPort;
    @Override
    public List<CodeGroupEntity> getAllGroupNames() {
        return codeGroupPort.findAllNames();
    }
}
