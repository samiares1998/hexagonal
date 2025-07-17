package cl.tenpo.usecases.impl;

import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.DeleteCodeUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeleteCodeUseCaseImpl implements DeleteCodeUseCase {

  private final CodePort codePort;

  @Override
  public void delete(UUID codeId, String email) {
    if (codeId == null) {
      throw new TenpoBusinessException("code.id.null", "El ID del código no puede ser nulo");
    }
    codePort.findCodeById(codeId);
    codePort.delete(codeId, email);
  }
}
