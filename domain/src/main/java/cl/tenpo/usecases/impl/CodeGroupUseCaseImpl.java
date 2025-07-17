package cl.tenpo.usecases.impl;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.ports.CodePort;
import cl.tenpo.usecases.CodeGroupUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CodeGroupUseCaseImpl implements CodeGroupUseCase {

  private final CodePort codePort;

  @Override
  public List<CodeEntity> getCodeGroups() {
    List<CodeEntity> tmp = null;
    try {
      tmp = codePort.findCodeGroups();
    } catch (Exception e) {
      log.error("ERROR CodeGroup UseCase {}", e.getMessage());
    }
    return tmp;
  }
}
