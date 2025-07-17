package cl.tenpo.repository.jpa.repositories;

import cl.tenpo.repository.jpa.model.CodeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, UUID> {

  boolean existsByGroupName(String name);

  Page<CodeGroup> findAll(Pageable pageable);

  @Query("SELECT c.id_code_group as id_code_group, c.groupName as groupName FROM CodeGroup c")
  List<CodeGroupProjection> findAllProjectedBy();

  Page<CodeGroup> findByGroupNameContainingIgnoreCase(String name, Pageable pageable);
}
