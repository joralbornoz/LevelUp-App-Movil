package cl.duoc.levelup.sucursales_service.service;

import cl.duoc.levelup.sucursales_service.dto.SucursalDTO;

import java.util.List;

public interface SucursalService {

    List<SucursalDTO> findAll();
    SucursalDTO findById(Long id);
    SucursalDTO save(SucursalDTO dto);
    SucursalDTO update(Long id, SucursalDTO dto);
    void delete(Long id);
}
