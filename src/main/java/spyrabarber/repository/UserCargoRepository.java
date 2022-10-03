package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spyrabarber.domain.UserCargo;

public interface UserCargoRepository extends JpaRepository<UserCargo, Long> {
}
