package spyrabarber.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spyrabarber.domain.Servico;
import spyrabarber.domain.projections.SimpleServicoProjection;

import java.util.List;
import java.util.Optional;

public interface ServicosRepository extends JpaRepository<Servico, Long> {

    @Query("SELECT s FROM Servico s WHERE s.nome LIKE :nome")
    Optional<Servico> findByName(@Param("nome") String nome);

    @Query("SELECT DISTINCT s FROM Servico s " +
            "INNER JOIN s.users u " +
            "WHERE u.id = :id")
    List<Servico> getServicosByUserId(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Servico s WHERE s.nome LIKE %:keyword%")
    Page<SimpleServicoProjection> getSimpleServicoProjectionByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
