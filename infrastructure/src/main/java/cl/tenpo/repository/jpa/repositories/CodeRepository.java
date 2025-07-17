package cl.tenpo.repository.jpa.repositories;

import cl.tenpo.repository.jpa.model.Code;
import cl.tenpo.repository.jpa.model.CodeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeRepository extends JpaRepository<Code, UUID> {

  @Query(
      value =
          "SELECT g.group_name FROM CODE c JOIN CODE_GROUP g ON c.id_code_group = g.id_code_group"
              + " WHERE c.mambu_id_code = :id_code",
      nativeQuery = true)
  Code findGroupNameByIdCode(@Param("id_code") int idCode);

  List<Code> findAll();

  Optional<Code> findByMambuIdCode(int mambuIdCode);

  List<Code> findByCodeGroup(CodeGroup codeGroup);

  Page<Code> findAll(Pageable pageable);

  Page<Code> findByMambuCodeNameContainingIgnoreCase(String name, Pageable pageable);

  Page<Code> findByMambuCodeNameContainingIgnoreCaseAndCodeGroup_groupNameContainingIgnoreCase(
      String mambuCodeName, String codeGroupName, Pageable pageable);

  Page<Code> findByCodeGroup_groupNameContainingIgnoreCase(String codeGroupName, Pageable pageable);
}
