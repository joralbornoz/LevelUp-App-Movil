package cl.duoc.levelup.usuarios_service.repository;

import cl.duoc.levelup.usuarios_service.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioEmailOrderByFechaDesc(String email);
    void deleteByUsuarioEmail(String email);
}