package cl.duoc.levelup.usuarios_service.controller;

import cl.duoc.levelup.usuarios_service.dto.CompraDTO;
import cl.duoc.levelup.usuarios_service.service.CompraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin("*")
public class CompraController {

    private final CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    @PostMapping
    public CompraDTO crear(@RequestBody CompraDTO dto) {
        return service.crearCompra(dto);
    }

    @GetMapping("/usuario/{email}")
    public List<CompraDTO> listarPorUsuario(@PathVariable String email) {
        return service.listarComprasPorEmail(email);
    }
}
