package cl.duoc.levelup.sucursales_service.dto;

import lombok.Data;

@Data
public class SucursalDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String horario;
}
