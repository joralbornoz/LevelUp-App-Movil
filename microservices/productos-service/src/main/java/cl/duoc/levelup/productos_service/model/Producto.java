package cl.duoc.levelup.productos_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    private String codigo;
    private String nombre;
    private String categoria;
    private Integer precio;
    private String descripcion;
    private String imagenUrl;
    private Integer stock;
}
