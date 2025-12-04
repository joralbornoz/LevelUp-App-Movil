package cl.duoc.levelup.usuarios_service.service;

import cl.duoc.levelup.usuarios_service.dto.UsuarioDTO;
import cl.duoc.levelup.usuarios_service.dto.UsuarioMapper;
import cl.duoc.levelup.usuarios_service.model.Usuario;
import cl.duoc.levelup.usuarios_service.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
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
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
