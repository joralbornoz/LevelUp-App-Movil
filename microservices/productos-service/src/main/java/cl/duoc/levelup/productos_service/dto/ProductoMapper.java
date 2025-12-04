package cl.duoc.levelup.productos_service.dto;

import cl.duoc.levelup.productos_service.model.Producto;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo(p.getCodigo());
        dto.setNombre(p.getNombre());
        dto.setCategoria(p.getCategoria());
        dto.setPrecio(p.getPrecio());
        dto.setDescripcion(p.getDescripcion());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setStock(p.getStock());
        return dto;
    }

    public static Producto toEntity(ProductoDTO dto) {
        Producto p = new Producto();
        p.setCodigo(dto.getCodigo());
        p.setNombre(dto.getNombre());
        p.setCategoria(dto.getCategoria());
        p.setPrecio(dto.getPrecio());
        p.setDescripcion(dto.getDescripcion());
        p.setImagenUrl(dto.getImagenUrl());
        p.setStock(dto.getStock());
        return p;
    }
}
