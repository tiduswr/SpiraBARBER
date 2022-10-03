package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spyrabarber.domain.CargoHistorico;

import java.util.List;

public interface CargoHistoricoRepository extends JpaRepository<CargoHistorico, Long> {
    @Query("SELECT h FROM CargoHistorico h WHERE h.user.id = :userid")
    List<CargoHistorico> findAllByUserId(@Param("userid") Long userid);
}
