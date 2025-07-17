package cl.tenpo.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.controller.dto.CodeGroupResponse;
import cl.tenpo.controller.mapper.CodeGroupMapper;
import cl.tenpo.controller.mapper.CodeServiceMapper;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/bank/code-group")
@AllArgsConstructor
public class CodeGroupController {

  private final CodeGroupUseCase codeGroupUseCase;
  private final CreateCodeGroupUseCase createCodeGroupUseCase;
  private final FindAllCodeGroupsUseCase findAllCodeGroups;
  private final FindByIdCodeGroupUseCase findByIdCodeGroupUseCase;
  private final UpdateCodeGroupUseCase updateCodeGroupUseCase;
  private final DeleteCodeGroupUseCase deleteCodeGroupUseCase;
  private final GetAllGroupNamesUseCase getAllGroupNames;

  @GetMapping
  public ResponseEntity<List<CodeDTO>> transaction() {
    /** Obtiene los code group desde base de datos para filtros en tabla de transacciones */
    List<CodeDTO> response =
             codeGroupUseCase
            .getCodeGroups()
            .stream()
            .map(CodeServiceMapper::entityToDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok().body(response);
  }

  @PostMapping
  public ResponseEntity<CodeGroupResponse> createCodeGroup(
          @RequestParam(required = true) String codeGroupName, @RequestParam (required = true) String email) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CodeGroupMapper
                .entityToDto(
                         createCodeGroupUseCase
                        .create(codeGroupName, email)));
  }

  @GetMapping("/all")
  public ResponseEntity<PageResult<CodeGroupResponse>> getAllCodeGroups(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "15") @Min(15) int size,
      @RequestParam(required = false) String name) {
    return ResponseEntity.ok(CodeGroupMapper
            .toDtoPage(
                     findAllCodeGroups
                    .find(page, size, name)));
  }

  @GetMapping("/all/groups")
  public ResponseEntity<List<CodeGroupResponse>> getAllGroups() {
    return ResponseEntity.ok(
             getAllGroupNames
            .getAllGroupNames()
            .stream()
            .map(CodeGroupMapper::entityToDto)
            .collect(Collectors.toList())
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<CodeGroupResponse> getCodeGroupById(@PathVariable UUID id) {
    return ResponseEntity.ok(CodeGroupMapper.entityToDto(
             findByIdCodeGroupUseCase
            .findById(id)));
  }

  @PostMapping("/{id}")
  public ResponseEntity<CodeGroupResponse> patchCodeGroup(
      @PathVariable UUID id, @RequestParam String name, @RequestParam String email) {
    updateCodeGroupUseCase.update(id, name, email);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCodeGroup(@PathVariable UUID id, @RequestParam String email) {
    deleteCodeGroupUseCase.delete(id, email);
    return ResponseEntity.noContent().build();
  }
}
