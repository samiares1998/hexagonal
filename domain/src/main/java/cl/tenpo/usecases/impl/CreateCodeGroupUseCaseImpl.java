package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.CreateCodeGroupUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateCodeGroupUseCaseImpl implements CreateCodeGroupUseCase {

  private final CodeGroupPort codeGroupPort;

  @Override
  public CodeGroupEntity create(String name, String email) {
    if (codeGroupPort.existsByName(name)) {
      throw new TenpoBusinessException("code.group.exists","Ya existe un grupo con el nombre: " + name);
    }
    return codeGroupPort.createCodeGroup(name, email);
  }
}
