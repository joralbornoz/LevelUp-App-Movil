package cl.duoc.levelup.usuarios_service.service;

import cl.duoc.levelup.usuarios_service.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Long id);
    UsuarioDTO findByEmail(String email);
    UsuarioDTO save(UsuarioDTO dto);
    UsuarioDTO update(Long id, UsuarioDTO dto);
    void delete(Long id);
}
