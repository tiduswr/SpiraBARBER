package spyrabarber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spyrabarber.domain.Servico;
import spyrabarber.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.email LIKE :email AND u.ativo = true")
    Optional<Usuario> findByEmailAndAtivo(@Param("email") String username);
}
