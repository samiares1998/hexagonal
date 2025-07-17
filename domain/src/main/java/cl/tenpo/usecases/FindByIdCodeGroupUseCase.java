package cl.tenpo.usecases;

import cl.tenpo.entities.CodeGroupEntity;

import java.util.UUID;

public interface FindByIdCodeGroupUseCase {
  CodeGroupEntity findById(UUID id);
}
