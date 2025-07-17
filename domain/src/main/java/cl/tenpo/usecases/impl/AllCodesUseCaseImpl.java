package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exceptions.TenpoBusinessException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.AllCodesUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AllCodesUseCaseImpl implements AllCodesUseCase {
  private final CodePort codePort;

  @Override
  public PageResult<CodeEntity> getAll(int page, int size, String name, String groupName) {
    if (page < 0) throw new TenpoBusinessException("invalid.code", "Page number must be >= 0");
    if (size < 1) throw new TenpoBusinessException("invalid.code", "Page size must be >= 1");
    PageResult<CodeEntity> result = codePort.getAll(page, size, name, groupName);
    size = Math.min(size, 100);
    if (result==null) {
      throw new TenpoBusinessException("empty.codes", "No se encontraron codes de canales");
    }
    return codePort.getAll(page, size, name, groupName);
  }
}
