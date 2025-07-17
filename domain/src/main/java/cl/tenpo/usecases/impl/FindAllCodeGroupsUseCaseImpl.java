package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.FindAllCodeGroupsUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindAllCodeGroupsUseCaseImpl implements FindAllCodeGroupsUseCase {
  private final CodeGroupPort codeGroupPort;

  @Override
  public PageResult<CodeGroupEntity> find(int page, int size, String name) {
    if (page < 0)
      throw new TenpoBusinessException("invalid.code.groups", "Page number must be >= 0");
    if (size < 1) throw new TenpoBusinessException("invalid.code.groups", "Page size must be >= 1");

    size = Math.min(size, 100);

    PageResult<CodeGroupEntity> result = codeGroupPort.findAll(page, size, name);

    if (result==null) {
      throw new TenpoBusinessException("empty.code.groups", "No se encontraron grupos de codigos");
    }

    return result;
  }
}
