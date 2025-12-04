package cl.duoc.levelup.productos_service.controller;

import cl.duoc.levelup.productos_service.dto.ProductoDTO;
import cl.duoc.levelup.productos_service.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ProductoDTO> getByCodigo(@PathVariable String codigo) {
        ProductoDTO dto = service.findByCodigo(codigo);
        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ProductoDTO create(@RequestBody ProductoDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<ProductoDTO> update(
            @PathVariable String codigo,
            @RequestBody ProductoDTO dto
    ) {
        ProductoDTO updated = service.update(codigo, dto);
        return (updated != null)
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo) {
        service.delete(codigo);
        return ResponseEntity.noContent().build();
    }
}
