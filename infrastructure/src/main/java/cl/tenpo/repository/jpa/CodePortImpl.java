package cl.tenpo.repository.jpa;

import cl.tenpo.entities.CodeEntity;
import cl.tenpo.exception.TenpoException;
import cl.tenpo.ports.CodePort;
import cl.tenpo.repository.jpa.repositories.CodeGroupRepository;
import cl.tenpo.repository.jpa.repositories.CodeRepository;
import cl.tenpo.repository.jpa.mapper.CodeMapper;
import cl.tenpo.repository.jpa.model.Code;
import cl.tenpo.repository.jpa.model.CodeGroup;
import cl.tenpo.shared.pagination.PageMapper;
import cl.tenpo.shared.pagination.PageResult;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CodePortImpl implements CodePort {

  private final CodeRepository codeRepository;
  private final CodeGroupRepository codeGroupRepository;
  private final JdbcTemplate jdbcTemplate;

  private void setAuditUsername(String email) {
    try {
      if (StringUtils.isBlank(email)) {
        log.warn("Email vacío recibido para auditoría");
        return;
      }
      jdbcTemplate.execute("SET audit.username = '" + email + "'");
      log.debug("Usuario de auditoría seteado: {}", email);
    } catch (DataAccessException dae) {
      log.warn("No se pudo setear audit.username, operación sin trazabilidad de usuario", dae);
    }
  }

  public CodeEntity create(CodeEntity codeEntity, String email) {
    try {
      setAuditUsername(email);
      Code code =
          Code.builder()
              .codeGroup(CodeGroup.builder().id_code_group(codeEntity.getGroupId()).build())
              .mambuCodeName(codeEntity.getMambuCodeName())
              .mambuIdCode(codeEntity.getMambuIdCode())
              .build();

      return CodeMapper.dtoToEntity(codeRepository.save(code));
    } catch (DataAccessException e) {
      log.error("Error al crear código en la base de datos", e);
      throw new TenpoException(
          HttpStatus.SERVICE_UNAVAILABLE,
          "database.error",
          "Error al crear el código en la base de datos");
    } catch (Exception e) {
      log.error("Error inesperado al crear código", e);
      throw new TenpoException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "unexpected.error",
          "Error inesperado al crear el código");
    }
  }

  @Override
  public Optional<CodeEntity> findbyMambuId(int mambuIdCode) {
    try {
      return Optional.ofNullable(
          codeRepository.findByMambuIdCode(mambuIdCode).map(CodeMapper::dtoToEntity).orElse(null));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public List<CodeEntity> findbyGroupId(UUID id) {
    try {
      List<Code> codes =
          codeRepository.findByCodeGroup(CodeGroup.builder().id_code_group(id).build());
      if (codes.isEmpty()) {
        log.warn("No se encontraron códigos para el grupo con ID: {}", id);
        return Collections.emptyList();
      }
      return codes.stream().map(CodeMapper::dtoToEntity).collect(Collectors.toList());
    } catch (DataAccessException e) {
      log.error("Error al buscar códigos por groupId: {}", id, e);
      throw new TenpoException(
          HttpStatus.SERVICE_UNAVAILABLE, "database.error", "Error al acceder a la base de datos");
    }
  }

  @Override
  public void update(int mambuId, CodeEntity codeDTO, String email) {
    try {
      setAuditUsername(email);
      Code existingCode =
          codeRepository
              .findByMambuIdCode(mambuId)
              .orElseThrow(() -> new TenpoException("Code not found with mambuId: " + mambuId));

      if (codeDTO == null) {
        throw new TenpoException("El DTO de código no puede ser nulo");
      }

      Optional.ofNullable(codeDTO.getMambuCodeName()).ifPresent(existingCode::setMambuCodeName);
      Optional.ofNullable(codeDTO.getGroupId())
          .ifPresent(
              groupId -> {
                CodeGroup codeGroup =
                    codeGroupRepository
                        .findById(groupId)
                        .orElseThrow(
                            () ->
                                new EntityNotFoundException(
                                    "CodeGroup no encontrado con ID: " + groupId));
                existingCode.setCodeGroup(codeGroup);
              });
      if (codeDTO.getMambuIdCode() != 0) {
        existingCode.setMambuIdCode(codeDTO.getMambuIdCode());
      }

      codeRepository.save(existingCode);
    } catch (DataAccessException e) {
      log.error("Error al actualizar código con mambuId: {}", mambuId, e);
      throw new TenpoException(
          HttpStatus.SERVICE_UNAVAILABLE,
          "database.error",
          "Error al actualizar el código en la base de datos");
    } catch (EntityNotFoundException e) {
      log.warn("Intento de actualizar código no existente con mambuId: {}", mambuId);
      throw e;
    }
  }

  @Override
  @Transactional
  public void delete(UUID codeId, String email) {
    try {
      setAuditUsername(email);
      Code code =
          codeRepository
              .findById(codeId)
              .orElseThrow(
                  () -> {
                    log.warn("Intento de eliminar código no existente con ID: {}", codeId);
                    return new TenpoException("Código no encontrado con ID: " + codeId);
                  });

      if (code.getCodeGroup() != null) {
        code.getCodeGroup().getCodes().remove(code);
      }

      codeRepository.delete(code);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Intento de eliminar código no existente con ID: {}", codeId);
      throw new TenpoException("Código no encontrado con ID: " + codeId);
    } catch (DataAccessException e) {
      log.error("Error al eliminar código con ID: {}", codeId, e);
      throw new TenpoException(
          HttpStatus.SERVICE_UNAVAILABLE,
          "database.error",
          "Error al eliminar el código de la base de datos");
    }
  }

  @Override
  public CodeEntity findCodeById(UUID codeId) {
    Optional<Code> code = codeRepository.findById(codeId);
    return CodeMapper.dtoToEntity(
        code.orElseThrow(
            () -> {
              log.warn("Intento de operación con código no existente ID: {}", codeId);
              return new TenpoException("Código no encontrado con ID: " + codeId);
            }));
  }

  @Override
  public PageResult<CodeEntity> getAll(int page, int size, String name, String groupName) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Page<Code> codesPage;

      boolean hasName = name != null && !name.isBlank();
      boolean hasGroupName = groupName != null && !groupName.isBlank();

      if (hasName && hasGroupName) {
        codesPage =
            codeRepository
                .findByMambuCodeNameContainingIgnoreCaseAndCodeGroup_groupNameContainingIgnoreCase(
                    name, groupName, pageable);
      } else if (hasName) {
        codesPage = codeRepository.findByMambuCodeNameContainingIgnoreCase(name, pageable);
      } else if (hasGroupName) {
        codesPage =
            codeRepository.findByCodeGroup_groupNameContainingIgnoreCase(groupName, pageable);
      } else {
        codesPage = codeRepository.findAll(pageable);
      }

      if (codesPage.isEmpty()) {
        throw new TenpoException(
            HttpStatus.NO_CONTENT, "codegroups.empty", "No se encontraron grupos de código");
      }
      return PageMapper.map(codesPage,CodeMapper::dtoToEntity);

    } catch (DataAccessException dae) {
      log.error("Error al obtener todos los channel codes");
      throw new TenpoException(
          HttpStatus.SERVICE_UNAVAILABLE,
          "database.error",
          "Error al obtener todos los datos de la base de datos");
    }
  }

  @Override
  public List<CodeEntity> findCodeGroups() {
    return  codeRepository.findAll()
            .stream().map(CodeMapper::dtoToEntity)
            .collect(Collectors.toList());
  }
}
