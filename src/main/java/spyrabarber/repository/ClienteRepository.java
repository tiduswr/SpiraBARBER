package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spyrabarber.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
