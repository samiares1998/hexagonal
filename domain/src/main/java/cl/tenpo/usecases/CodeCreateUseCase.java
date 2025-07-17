package cl.tenpo.usecases;

import cl.tenpo.entities.CodeEntity;

public interface CodeCreateUseCase {
  CodeEntity create(CodeEntity codeEntity, String email);
}
