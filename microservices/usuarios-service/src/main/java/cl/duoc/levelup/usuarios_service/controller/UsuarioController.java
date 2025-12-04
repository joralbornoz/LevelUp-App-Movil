package cl.duoc.levelup.usuarios_service.controller;

import cl.duoc.levelup.usuarios_service.dto.UsuarioDTO;
import cl.duoc.levelup.usuarios_service.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        UsuarioDTO dto = service.findById(id);
        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> getByEmail(@PathVariable String email) {
        UsuarioDTO dto = service.findByEmail(email);
        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UsuarioDTO dto) {
        try {
            UsuarioDTO creado = service.save(dto);
            // 201 CREATED con el usuario creado
            return ResponseEntity.status(201).body(creado);
        } catch (RuntimeException ex) {
            if ("EMAIL_DUPLICADO".equals(ex.getMessage())) {
                // 409 CONFLICT con mensaje claro
                return ResponseEntity
                        .status(409)
                        .body("EMAIL_DUPLICADO");
            }
            // otras RuntimeException las dejamos explotar como 500
            throw ex;
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto
    ) {
        UsuarioDTO updated = service.update(id, dto);
        return (updated != null)
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
