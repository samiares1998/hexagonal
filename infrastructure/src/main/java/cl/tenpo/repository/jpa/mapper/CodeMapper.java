package cl.tenpo.repository.jpa.mapper;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.repository.jpa.model.Code;

public class CodeMapper {

  public static CodeEntity dtoToEntity(Code code) {
    if (code == null) {
      return null;
    }
    return CodeEntity.builder()
            .id(code.getId())
        .groupId(code.getCodeGroup().getId_code_group())
        .mambuIdCode(code.getMambuIdCode())
        .mambuCodeName(code.getMambuCodeName())
            .groupName(code.getCodeGroup().getGroupName())
        .build();
  }
}
