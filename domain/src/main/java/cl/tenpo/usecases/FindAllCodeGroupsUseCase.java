package cl.tenpo.usecases;


import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.shared.pagination.PageResult;

public interface FindAllCodeGroupsUseCase {
  PageResult<CodeGroupEntity> find(int page, int size, String name);
}
