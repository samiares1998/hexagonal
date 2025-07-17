package cl.tenpo.repository.jpa.repositories;

import cl.tenpo.repository.jpa.model.Reissuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ReissuanceRepository extends JpaRepository<Reissuance, Long> {
  Page<Reissuance> findByCreatedAtBetweenAndHolderId(
      LocalDateTime startDate, LocalDateTime endDate, UUID holderId, Pageable pageable);
}
