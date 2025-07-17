package cl.tenpo.repository.jpa;

import cl.tenpo.entities.CodeGroupEntity;
import cl.tenpo.exception.TenpoException;
import cl.tenpo.ports.CodeGroupPort;
import cl.tenpo.repository.jpa.repositories.CodeGroupProjection;
import cl.tenpo.repository.jpa.repositories.CodeGroupRepository;
import cl.tenpo.repository.jpa.mapper.CodeGroupMapper;
import cl.tenpo.repository.jpa.model.CodeGroup;
import cl.tenpo.shared.pagination.PageMapper;
import cl.tenpo.shared.pagination.PageResult;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CodeGroupPortImpl implements CodeGroupPort {

    private final CodeGroupRepository codeGroupRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public CodeGroup crearCodeGroup(CodeGroup codeGroup, String email) {
        try {
            setAuditUsername(email);
            log.info("CodeGroup guardado exitosamente: {}", codeGroup.getGroupName());
            return codeGroupRepository.save(codeGroup);

        } catch (DataAccessException dae) {
            log.error("Error de acceso a datos al guardar CodeGroup: {}", codeGroup.getGroupName(), dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE, "database.error", "Error al acceder a la base de datos");

        } catch (Exception e) {
            log.error("Error inesperado al guardar CodeGroup: {}", codeGroup.getGroupName(), e);
            throw new TenpoException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "codegroup.creation.error",
                    "Error inesperado al crear el grupo de código");
        }
    }

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

    @Override
    public CodeGroupEntity createCodeGroup(String name, String email) {
        CodeGroup codeGroup = new CodeGroup();
        codeGroup.setGroupName(name);
        return CodeGroupMapper.dtoToEntity(crearCodeGroup(codeGroup, email));
    }

    @Override
    public PageResult<CodeGroupEntity> findAll(int page, int size, String name) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CodeGroup> groupsPage;

            if (name != null && !name.isBlank()) {
                groupsPage = codeGroupRepository.findByGroupNameContainingIgnoreCase(name, pageable);
            } else {
                groupsPage = codeGroupRepository.findAll(pageable);
            }
            if (groupsPage.isEmpty()) {
                throw new TenpoException(
                        HttpStatus.NO_CONTENT, "codegroups.empty", "No se encontraron grupos de código");
            }

            return PageMapper.map(groupsPage,CodeGroupMapper::dtoToEntity);

        } catch (DataAccessException dae) {
            log.error("Error al acceder a la base de datos", dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "database.error",
                    "Error al recuperar los grupos de código");
        }
    }

    @Override
    public CodeGroupEntity finById(UUID id) {
        try {
            if (id == null) {
                throw new TenpoException(
                        HttpStatus.BAD_REQUEST, "codegroup.id.null", "El ID no puede ser nulo");
            }

            return codeGroupRepository
                    .findById(id)
                    .map(CodeGroupMapper::dtoToEntity)
                    .orElseThrow(
                            () ->
                                    new TenpoException(
                                            HttpStatus.NOT_FOUND,
                                            "codegroup.not.found",
                                            "No se encontró el grupo con ID: " + id));

        } catch (DataAccessException dae) {
            log.error("Error al buscar grupo por ID: {}", id, dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE, "database.error", "Error al buscar el grupo de código");
        }
    }

    @Override
    public void update(UUID id, String name, String email) {
        try {
            CodeGroup codeGroup =
                    codeGroupRepository
                            .findById(id)
                            .orElseThrow(
                                    () ->
                                            new TenpoException(
                                                    HttpStatus.NOT_FOUND,
                                                    "codegroup.not.found",
                                                    "No se encontró el grupo con ID: " + id));

            codeGroup.setGroupName(name);
            crearCodeGroup(codeGroup, email);

        } catch (TenpoException te) {
            throw te;
        } catch (Exception e) {
            log.error("Error al actualizar grupo con ID: {}", id, e);
            throw new TenpoException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "codegroup.update.error",
                    "Error inesperado al actualizar el grupo de código");
        }
    }

    @Override
    public void delete(UUID id, String email) {
        try {
            setAuditUsername(email);
            codeGroupRepository.deleteById(id);
            log.info("CodeGroup eliminado con ID: {}", id);
        } catch (DataIntegrityViolationException die) {
            String errorMsg =
                    "No se puede eliminar el grupo con ID: " + id + " porque tiene elementos asociados";
            log.error(errorMsg, die);
            throw new TenpoException(HttpStatus.CONFLICT, "codegroup.delete.constraint", errorMsg);

        } catch (DataAccessException dae) {
            log.error("Error de acceso a datos al eliminar grupo con ID: {}", id, dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE, "database.error", "Error al eliminar el grupo de código");

        } catch (Exception e) {
            log.error("Error inesperado al eliminar grupo con ID: {}", id, e);
            throw new TenpoException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "codegroup.delete.error",
                    "Error inesperado al eliminar el grupo de código");
        }
    }

    @Override
    public boolean existsByName(String name) {
        try {
            return codeGroupRepository.existsByGroupName(name);

        } catch (DataAccessException dae) {
            log.error("Error al verificar existencia de grupo por nombre: {}", name, dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "database.error",
                    "Error al verificar el grupo de código");
        }
    }

    @Override
    public List<CodeGroupEntity> findAllNames() {
        try {
            List<CodeGroupProjection> group = codeGroupRepository.findAllProjectedBy();

            if (group.isEmpty()) {
                throw new TenpoException(
                        HttpStatus.NO_CONTENT, "codegroups.empty", "No se encontraron grupos de código");
            }

            return group.stream().map(CodeGroupMapper::projectionToEntity).collect(Collectors.toList());

        } catch (DataAccessException dae) {
            log.error("Error al acceder a la base de datos", dae);
            throw new TenpoException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "database.error",
                    "Error al recuperar los grupos de código");
        }
    }
}
