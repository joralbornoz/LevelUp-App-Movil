package cl.duoc.levelup.usuarios_service.service;

import cl.duoc.levelup.usuarios_service.dto.UsuarioDTO;
import cl.duoc.levelup.usuarios_service.dto.UsuarioMapper;
import cl.duoc.levelup.usuarios_service.model.Usuario;
import cl.duoc.levelup.usuarios_service.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import cl.duoc.levelup.usuarios_service.repository.CompraRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final CompraRepository compraRepository;

    public UsuarioServiceImpl(
            UsuarioRepository repo,
            CompraRepository compraRepository
    ) {
        this.repo = repo;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(Long id) {
        return repo.findById(id)              // Optional<Usuario>
                .map(UsuarioMapper::toDTO)    // Optional<UsuarioDTO>
                .orElse(null);                // UsuarioDTO o null
    }

    @Override
    public UsuarioDTO findByEmail(String email) {
        return repo.findByEmail(email)        // Optional<Usuario>
                .map(UsuarioMapper::toDTO)    // Optional<UsuarioDTO>
                .orElse(null);                // UsuarioDTO o null
    }

    @Override
    public UsuarioDTO save(UsuarioDTO dto) {
        try {
            Usuario entity = UsuarioMapper.toEntity(dto);

            // 👇 MUY IMPORTANTE:
            // Si el dto trae id 0 o cualquier valor, lo ponemos en null
            // para que Hibernate haga INSERT y no UPDATE.
            entity.setId(null);

            // Por seguridad básica: si no viene rol, lo dejamos USER
            if (entity.getRol() == null || entity.getRol().isBlank()) {
                entity.setRol("USUARIO");
            }

            Usuario saved = repo.save(entity);  // aquí ya hará INSERT
            return UsuarioMapper.toDTO(saved);

        } catch (DataIntegrityViolationException e) {
            // Ej: email duplicado
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "EMAIL_DUPLICADO",
                    e
            );
        }
    }

    @Override
    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        if (!repo.existsById(id)) {
            return null;
        }

        Usuario entity = UsuarioMapper.toEntity(dto);
        entity.setId(id);

        Usuario saved = repo.save(entity);
        return UsuarioMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "USUARIO_NO_EXISTE")
                );

        // Comprobamos si el usuario es un ADMIN
        if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
            // Cambio aquí: personalizado el mensaje
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No se puede eliminar al administrador"
            );
        }

        // Eliminar compras del usuario
        compraRepository.deleteByUsuarioEmail(usuario.getEmail());

        // Eliminar usuario
        repo.deleteById(id);
    }
}

