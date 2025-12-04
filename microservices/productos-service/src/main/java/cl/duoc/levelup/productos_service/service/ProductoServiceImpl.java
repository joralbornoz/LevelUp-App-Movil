package cl.duoc.levelup.productos_service.service;

import cl.duoc.levelup.productos_service.dto.ProductoDTO;
import cl.duoc.levelup.productos_service.dto.ProductoMapper;
import cl.duoc.levelup.productos_service.model.Producto;
import cl.duoc.levelup.productos_service.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;

    public ProductoServiceImpl(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ProductoDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO findByCodigo(String codigo) {
        return repo.findById(codigo)
                .map(ProductoMapper::toDTO)
                .orElse(null);
    }

    @Override
    public ProductoDTO save(ProductoDTO dto) {
        Producto entity = ProductoMapper.toEntity(dto);
        return ProductoMapper.toDTO(repo.save(entity));
    }

    @Override
    public ProductoDTO update(String codigo, ProductoDTO dto) {
        return repo.findById(codigo)
                .map(existente -> {
                    existente.setNombre(dto.getNombre());
                    existente.setCategoria(dto.getCategoria());
                    existente.setPrecio(dto.getPrecio());
                    existente.setDescripcion(dto.getDescripcion());
                    existente.setImagenUrl(dto.getImagenUrl());
                    existente.setStock(dto.getStock());
                    return ProductoMapper.toDTO(repo.save(existente));
                })
                .orElse(null);
    }

    @Override
    public void delete(String codigo) {
        repo.deleteById(codigo);
    }
}
