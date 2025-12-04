package cl.duoc.levelup.usuarios_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CompraDTO {

    private Long id;
    private String emailUsuario;
    private LocalDateTime fecha;
    private Integer total;
    private String detalle;

}
