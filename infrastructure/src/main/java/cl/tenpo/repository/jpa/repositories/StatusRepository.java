package cl.tenpo.repository.jpa.repositories;


import cl.tenpo.repository.jpa.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findById(int id);
}
