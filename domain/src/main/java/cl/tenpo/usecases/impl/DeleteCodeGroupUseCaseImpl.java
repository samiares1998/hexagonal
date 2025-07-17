package cl.tenpo.usecases.impl;


import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.DeleteCodeGroupUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class DeleteCodeGroupUseCaseImpl implements DeleteCodeGroupUseCase {

  private final CodeGroupPort codeGroupPort;
  private final CodePort codePort;

  @Override
  public void delete(UUID id, String email) {
    if (codePort.findbyGroupId(id).isEmpty()) {
      codeGroupPort.finById(id);
      codeGroupPort.delete(id, email);
    } else {
      log.error("Intento de eliminar un grupo con códigos asociados. ID: {}", id);
      throw new TenpoBusinessException(
          "code.group.has.channels",
          "No se puede eliminar el grupo porque contiene códigos asociados.");
    }
  }
}
