package cl.tenpo.usecases;

import cl.tenpo.entities.CodeGroupEntity;

public interface CreateCodeGroupUseCase {
  CodeGroupEntity create(String name, String email);
}
