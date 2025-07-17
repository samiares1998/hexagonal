package cl.tenpo.controller.mapper;

import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.entities.CodeEntity;
import cl.tenpo.shared.pagination.PageResult;

import java.util.List;
import java.util.stream.Collectors;

public class CodeServiceMapper {

  public static CodeEntity dtoToEntity(CodeDTO code) {
    if (code == null) {
      return null;
    }
    return CodeEntity.builder()
        .groupId(code.getGroupId())
        .mambuIdCode(code.getMambuIdCode())
        .mambuCodeName(code.getMambuCodeName())
        .groupName(code.getGroupName())
        .build();
  }

  public static CodeDTO entityToDto(CodeEntity code) {
    if (code == null) {
      return null;
    }
    return CodeDTO.builder()
            .id(code.getId())
        .groupId(code.getGroupId())
        .mambuIdCode(code.getMambuIdCode())
        .mambuCodeName(code.getMambuCodeName())
        .groupName(code.getGroupName())
        .build();
  }
  public static PageResult<CodeDTO> toDtoPage(PageResult<CodeEntity> entityPage) {
    List<CodeDTO> content = entityPage.getContent()
            .stream()
            .map(CodeServiceMapper::entityToDto)
            .collect(Collectors.toList());

    return new PageResult<>(
            content,
            entityPage.getPage(),
            entityPage.getSize(),
            entityPage.getTotalElements()
    );
  }
}


