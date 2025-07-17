package cl.tenpo.usecases;

import cl.tenpo.entities.CodeEntity;

public interface UpdateCodeByMambuIdUseCase {
  void update(int mambuId, CodeEntity codeDTO, String email);
}
