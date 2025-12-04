package cl.duoc.levelup.usuarios_service.dto;

import cl.duoc.levelup.usuarios_service.model.Compra;
import cl.duoc.levelup.usuarios_service.model.Usuario;

public class CompraMapper {

    public static CompraDTO toDTO(Compra c) {
        CompraDTO dto = new CompraDTO();
        dto.setId(c.getId());
        dto.setFecha(c.getFecha());
        dto.setTotal(c.getTotal());
        dto.setDetalle(c.getDetalle());
        if (c.getUsuario() != null) {
            dto.setEmailUsuario(c.getUsuario().getEmail());
        }
        return dto;
    }

    public static Compra toEntity(CompraDTO dto, Usuario usuario) {
        Compra c = new Compra();
        c.setId(dto.getId());         // normalmente null en creación
        c.setUsuario(usuario);
        c.setFecha(dto.getFecha());
        c.setTotal(dto.getTotal());
        c.setDetalle(dto.getDetalle());
        return c;
    }
}
