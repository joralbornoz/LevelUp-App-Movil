package cl.duoc.levelup.usuarios_service.dto;

import cl.duoc.levelup.usuarios_service.model.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario u) {
        if (u == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setEdad(u.getEdad());
        dto.setPreferencias(u.getPreferencias());
        dto.setDireccion(u.getDireccion());
        dto.setRol(u.getRol());
        dto.setPassword(u.getPassword());  // 👈 ahora sí mapeamos password
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario u = new Usuario();
        u.setId(dto.getId());
        u.setNombre(dto.getNombre());
        u.setEmail(dto.getEmail());
        u.setEdad(dto.getEdad());
        u.setPreferencias(dto.getPreferencias());
        u.setDireccion(dto.getDireccion());
        u.setRol(dto.getRol());
        u.setPassword(dto.getPassword());  // 👈 y también al revés
        return u;
    }
}

