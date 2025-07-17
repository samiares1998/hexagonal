package cl.tenpo.usecases;

import java.util.UUID;

public interface DeleteCodeGroupUseCase {
  void delete(UUID id, String email);
}
