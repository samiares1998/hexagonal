package cl.tenpo.repository.jpa.mapper;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.repository.jpa.repositories.CodeGroupProjection;
import cl.tenpo.repository.jpa.model.CodeGroup;
import java.util.stream.Collectors;

public class CodeGroupMapper {

  public static CodeGroupEntity dtoToEntity(CodeGroup codeGroup) {
    if (codeGroup == null) {
      return null;
    }
    return CodeGroupEntity.builder()
        .codeGroupName(codeGroup.getGroupName())
        .id(codeGroup.getId_code_group())
            .codes(codeGroup.getCodes()
                    .stream().map(CodeMapper::dtoToEntity)
                    .collect(Collectors.toList()))
        .build();
  }

  public static CodeGroupEntity projectionToEntity(CodeGroupProjection projection) {
    if (projection == null) {
      return null;
    }
    return  CodeGroupEntity.builder()
            .codeGroupName(projection.getGroupName())
            .id(projection.getId_code_group())
            .build();
  }
}
