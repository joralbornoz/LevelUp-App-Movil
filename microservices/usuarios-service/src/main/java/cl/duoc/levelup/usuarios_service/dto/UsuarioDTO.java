package cl.duoc.levelup.usuarios_service.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;
    private Integer edad;
    private String preferencias;
    private String direccion;
    private String rol;
    private String password;

    public UsuarioDTO() {}
}
