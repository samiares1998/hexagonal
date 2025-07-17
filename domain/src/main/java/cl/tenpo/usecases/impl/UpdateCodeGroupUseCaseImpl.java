package cl.tenpo.usecases.impl;

import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.usecases.UpdateCodeGroupUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UpdateCodeGroupUseCaseImpl implements UpdateCodeGroupUseCase {
  private final CodeGroupPort codeGroupPort;

  @Override
  public void update(UUID id, String name, String email) {
    if (codeGroupPort.finById(id) == null) {
      throw new TenpoBusinessException("invalid.group.id", "codigo del grupo no existe");
    }
    codeGroupPort.update(id, name, email);
  }
}
