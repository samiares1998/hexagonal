package cl.tenpo.usecases;


import cl.tenpo.entities.CodeEntity;
import cl.tenpo.shared.pagination.PageResult;

public interface AllCodesUseCase {
  PageResult<CodeEntity> getAll(int page, int size, String name, String groupName);
}
