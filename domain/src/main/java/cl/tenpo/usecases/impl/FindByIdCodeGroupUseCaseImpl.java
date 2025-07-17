package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.FindByIdCodeGroupUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class FindByIdCodeGroupUseCaseImpl implements FindByIdCodeGroupUseCase {
  private final CodeGroupPort codeGroupPort;

  @Override
  public CodeGroupEntity findById(UUID id) {
    CodeGroupEntity result = codeGroupPort.finById(id);
    if (result == null) {
      throw new TenpoBusinessException("code.group.not.found", "No se encontró el grupo con ID: " + id);
    }
    return result;
  }
}
