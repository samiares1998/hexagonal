package cl.tenpo.entities;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class CodeGroupEntity {
  UUID id;
  String codeGroupName;
  List<CodeEntity> codes;
}
