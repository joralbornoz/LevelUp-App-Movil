package cl.duoc.levelup.productos_service.service;

import cl.duoc.levelup.productos_service.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> findAll();
    ProductoDTO findByCodigo(String codigo);
    ProductoDTO save(ProductoDTO dto);
    ProductoDTO update(String codigo, ProductoDTO dto);
    void delete(String codigo);
}
