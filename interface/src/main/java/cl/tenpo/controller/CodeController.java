package cl.tenpo.controller;

import java.util.UUID;

import cl.tenpo.controller.dto.CodeDTO;
import cl.tenpo.controller.mapper.CodeServiceMapper;
import cl.tenpo.entities.CodeEntity;
import cl.tenpo.shared.pagination.PageResult;
import cl.tenpo.usecases.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/bank/code")
@AllArgsConstructor
public class CodeController {

  public final CodeCreateUseCase codeCreateUseCase;
  public final FindByMambuIdUseCase findByMambuIdUseCase;
  public final UpdateCodeByMambuIdUseCase updateCodeByMambuIdUseCase;
  public final DeleteCodeUseCase deleteCodeUseCase;
  public final AllCodesUseCase allCodesUseCase;

  @PostMapping
  public ResponseEntity createCode(@RequestBody CodeDTO codeDto, @RequestParam(required = true) String email) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(codeCreateUseCase
                    .create(CodeServiceMapper.dtoToEntity(codeDto), email));
  }

  @GetMapping("/codes/{codeId}")
  public ResponseEntity<CodeDTO> getCodeByMambuId(@PathVariable("codeId") int mambuIdCode) {
    return ResponseEntity.ok(CodeServiceMapper.entityToDto(findByMambuIdUseCase.find(mambuIdCode)));
  }

  @GetMapping("/all")
  public ResponseEntity<PageResult<CodeDTO>> getAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "15") int size,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String groupName) {
    PageResult<CodeEntity> code =
             allCodesUseCase
            .getAll(page, size, name, groupName);

    return ResponseEntity.ok(CodeServiceMapper.toDtoPage(code));
  }

  @PostMapping("/codes/{MambuId}")
  public ResponseEntity patchCode(
      @PathVariable int MambuId, @RequestBody CodeDTO codeDTO, @RequestParam String email) {
    updateCodeByMambuIdUseCase.update(MambuId, CodeServiceMapper.dtoToEntity(codeDTO), email);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @DeleteMapping("/codes/{codeId}")
  public ResponseEntity<Void> deleteCode(@PathVariable UUID codeId, @RequestParam String email) {
    deleteCodeUseCase.delete(codeId, email);
    return ResponseEntity.noContent().build();
  }
}
