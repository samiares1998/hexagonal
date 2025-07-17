package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.CodeCreateUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CodeCreateUseCaseImpl implements CodeCreateUseCase {

  private final CodePort codePort;
  private final CodeGroupPort codeGroupPort;

  @Override
  public CodeEntity create(CodeEntity codeEntity, String email) {

    if (codeEntity == null || email == null || email.isBlank()) {
      throw new TenpoBusinessException("invalid.input", "codeEntity no puede estar vacio");
    }

    if (codeGroupPort.finById(codeEntity.getGroupId()) == null) {
      throw new TenpoBusinessException("code.group.not.exist", "No existe el id del grupo");
    }

    codePort
        .findbyMambuId(codeEntity.getMambuIdCode())
        .ifPresent(
            c -> {
              throw new TenpoBusinessException(
                  "code.exist",
                  "Ya existe un código con el mambu id: " + codeEntity.getMambuIdCode());
            });

    return codePort.create(codeEntity, email);
  }
}
