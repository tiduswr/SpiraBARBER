package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spyrabarber.domain.Barbeiro;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
}
