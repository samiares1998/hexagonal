package cl.tenpo.usecases;

import java.util.UUID;

public interface UpdateCodeGroupUseCase {
  void update(UUID id, String name, String email);
}
