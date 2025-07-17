package cl.tenpo.ports;



import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.shared.pagination.PageResult;

import java.util.List;
import java.util.UUID;

public interface CodeGroupPort {

  CodeGroupEntity createCodeGroup(String name, String email);

  PageResult<CodeGroupEntity> findAll(int page, int size, String name);

  CodeGroupEntity finById(UUID id);

  void update(UUID id, String name, String email);

  void delete(UUID id, String email);

  boolean existsByName(String name);

  List<CodeGroupEntity> findAllNames();
}
