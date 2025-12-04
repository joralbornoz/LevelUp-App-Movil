package cl.duoc.levelup.usuarios_service.repository;

import cl.duoc.levelup.usuarios_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);  // 👈 IMPORTANTE: Optional
}
