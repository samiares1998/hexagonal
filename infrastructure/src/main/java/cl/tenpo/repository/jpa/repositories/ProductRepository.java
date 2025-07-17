package cl.tenpo.repository.jpa.repositories;

import cl.tenpo.repository.jpa.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductType, Long> {
}