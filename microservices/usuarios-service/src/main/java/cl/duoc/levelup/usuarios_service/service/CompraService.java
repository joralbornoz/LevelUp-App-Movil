package cl.duoc.levelup.usuarios_service.service;

import cl.duoc.levelup.usuarios_service.dto.CompraDTO;

import java.util.List;

public interface CompraService {

    CompraDTO crearCompra(CompraDTO dto);
    List<CompraDTO> listarComprasPorEmail(String email);
}
