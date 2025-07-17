package cl.tenpo.ports;


import cl.tenpo.entities.CodeEntity;
import cl.tenpo.shared.pagination.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodePort {
  CodeEntity create(CodeEntity codeEntity, String email);

  Optional<CodeEntity> findbyMambuId(int mambuIdCode);

  List<CodeEntity> findbyGroupId(UUID id);

  void update(int mambuId, CodeEntity codeDTO, String email);

  void delete(UUID codeId, String email);

  CodeEntity findCodeById(UUID codeId);

  PageResult<CodeEntity> getAll(int page, int size, String name, String groupName);

  List<CodeEntity> findCodeGroups();
}
