package cl.tenpo.usecases;

import cl.tenpo.entities.CodeEntity;

public interface FindByMambuIdUseCase {
  CodeEntity find(int mambuIdCode);
}
