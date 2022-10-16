package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spyrabarber.domain.Servico;

import java.util.Optional;

public interface ServicosRepository extends JpaRepository<Servico, Long> {

    @Query("SELECT s FROM Servico s WHERE s.nome LIKE :nome")
    Optional<Servico> findByName(@Param("nome") String nome);

}
