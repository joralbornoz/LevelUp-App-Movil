package cl.duoc.levelup.usuarios_service.service;

import cl.duoc.levelup.usuarios_service.dto.CompraDTO;
import cl.duoc.levelup.usuarios_service.dto.CompraMapper;
import cl.duoc.levelup.usuarios_service.model.Compra;
import cl.duoc.levelup.usuarios_service.model.Usuario;
import cl.duoc.levelup.usuarios_service.repository.CompraRepository;
import cl.duoc.levelup.usuarios_service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepo;
    private final UsuarioRepository usuarioRepo;

    public CompraServiceImpl(CompraRepository compraRepo, UsuarioRepository usuarioRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public CompraDTO crearCompra(CompraDTO dto) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepo.findByEmail(dto.getEmailUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Setear fecha en el server
        dto.setFecha(LocalDateTime.now());

        Compra entity = CompraMapper.toEntity(dto, usuario);
        Compra guardada = compraRepo.save(entity);

        return CompraMapper.toDTO(guardada);
    }

    @Override
    public List<CompraDTO> listarComprasPorEmail(String email) {
        List<Compra> compras = compraRepo.findByUsuarioEmailOrderByFechaDesc(email);

        return compras.stream()
                .map(CompraMapper::toDTO)
                .collect(Collectors.toList());
    }
}
