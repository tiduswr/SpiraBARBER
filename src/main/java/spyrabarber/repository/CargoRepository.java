package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spyrabarber.domain.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
