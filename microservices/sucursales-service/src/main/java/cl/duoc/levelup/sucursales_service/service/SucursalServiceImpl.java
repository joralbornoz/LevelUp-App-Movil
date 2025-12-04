package cl.duoc.levelup.sucursales_service.service;

import cl.duoc.levelup.sucursales_service.dto.SucursalDTO;
import cl.duoc.levelup.sucursales_service.dto.SucursalMapper;
import cl.duoc.levelup.sucursales_service.model.Sucursal;
import cl.duoc.levelup.sucursales_service.repository.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository repo;

    public SucursalServiceImpl(SucursalRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<SucursalDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(SucursalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SucursalDTO findById(Long id) {
        return repo.findById(id)
                .map(SucursalMapper::toDTO)
                .orElse(null);
    }

    @Override
    public SucursalDTO save(SucursalDTO dto) {
        Sucursal entity = SucursalMapper.toEntity(dto);
        return SucursalMapper.toDTO(repo.save(entity));
    }

    @Override
    public SucursalDTO update(Long id, SucursalDTO dto) {
        return repo.findById(id)
                .map(existente -> {
                    existente.setNombre(dto.getNombre());
                    existente.setDireccion(dto.getDireccion());
                    existente.setCiudad(dto.getCiudad());
                    existente.setTelefono(dto.getTelefono());
                    existente.setHorario(dto.getHorario());
                    return SucursalMapper.toDTO(repo.save(existente));
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
