package cl.duoc.levelup.sucursales_service.controller;

import cl.duoc.levelup.sucursales_service.dto.SucursalDTO;
import cl.duoc.levelup.sucursales_service.service.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@CrossOrigin("*")
public class SucursalController {

    private final SucursalService service;

    public SucursalController(SucursalService service) {
        this.service = service;
    }

    @GetMapping
    public List<SucursalDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> getById(@PathVariable Long id) {
        SucursalDTO dto = service.findById(id);
        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public SucursalDTO create(@RequestBody SucursalDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> update(
            @PathVariable Long id,
            @RequestBody SucursalDTO dto
    ) {
        SucursalDTO updated = service.update(id, dto);
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
