package cl.duoc.levelup.productos_service.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private String codigo;
    private String nombre;
    private String categoria;
    private Integer precio;
    private String descripcion;
    private String imagenUrl;
    private Integer stock;
}
