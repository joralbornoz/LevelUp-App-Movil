package cl.duoc.levelup.sucursales_service.dto;

import cl.duoc.levelup.sucursales_service.model.Sucursal;

public class SucursalMapper {

    public static SucursalDTO toDTO(Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setDireccion(s.getDireccion());
        dto.setCiudad(s.getCiudad());
        dto.setTelefono(s.getTelefono());
        dto.setHorario(s.getHorario());
        return dto;
    }

    public static Sucursal toEntity(SucursalDTO dto) {
        Sucursal s = new Sucursal();
        s.setId(dto.getId());
        s.setNombre(dto.getNombre());
        s.setDireccion(dto.getDireccion());
        s.setCiudad(dto.getCiudad());
        s.setTelefono(dto.getTelefono());
        s.setHorario(dto.getHorario());
        return s;
    }
}
