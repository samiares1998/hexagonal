package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.FindByMambuIdUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindByMambuIdUseCaseImpl implements FindByMambuIdUseCase {

  private final CodePort codePort;

  @Override
  public CodeEntity find(int mambuIdCode) {
    return codePort.findbyMambuId(mambuIdCode).orElse(null);
  }
}
