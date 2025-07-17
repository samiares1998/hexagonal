package cl.tenpo.controller.mapper;

import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.controller.dto.CodeGroupResponse;
import cl.tenpo.entities.CodeEntity;
import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.shared.pagination.PageResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CodeGroupMapper {

  public static CodeGroupResponse entityToDto(CodeGroupEntity codeGroup) {
    if (codeGroup == null) {
      return null;
    }
    return CodeGroupResponse.builder()
            .codeGroupName(codeGroup.getCodeGroupName())
            .id(codeGroup.getId())
            .codeDTOS(Optional.ofNullable(codeGroup.getCodes())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(CodeServiceMapper::entityToDto)
                    .collect(Collectors.toList()))
            .build();
  }

  public static PageResult<CodeGroupResponse> toDtoPage(PageResult<CodeGroupEntity> entityPage) {
    List<CodeGroupResponse> content = entityPage.getContent()
            .stream()
            .map(CodeGroupMapper::entityToDto)
            .collect(Collectors.toList());

    return new PageResult<>(
            content,
            entityPage.getPage(),
            entityPage.getSize(),
            entityPage.getTotalElements()
    );
  }

}


