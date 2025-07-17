package cl.tenpo.usecases;

import java.util.UUID;

public interface DeleteCodeUseCase {
  void delete(UUID codeId, String email);
}
