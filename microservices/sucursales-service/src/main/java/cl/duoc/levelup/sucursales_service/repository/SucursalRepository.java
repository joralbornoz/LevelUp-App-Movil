package cl.duoc.levelup.sucursales_service.repository;

import cl.duoc.levelup.sucursales_service.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
