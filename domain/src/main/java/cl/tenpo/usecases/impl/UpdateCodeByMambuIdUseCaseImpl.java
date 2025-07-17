package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.UpdateCodeByMambuIdUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateCodeByMambuIdUseCaseImpl implements UpdateCodeByMambuIdUseCase {

  private final CodePort codePort;

  @Override
  public void update(int mambuId, CodeEntity codeDTO, String email) {
    if(codePort.findbyMambuId(mambuId).isEmpty()){
      throw new TenpoBusinessException("invalid.mambu.id", "codigo mambu no existe");
    }
    codePort.update(mambuId, codeDTO, email);
  }
}
