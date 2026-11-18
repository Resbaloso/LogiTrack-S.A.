package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.BodegaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.BodegaMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.BodegaRepository;
import com.logitracksa.logitracksaapi.repository.InventarioRepository;
import com.logitracksa.logitracksaapi.repository.MovimientoRepository;
import com.logitracksa.logitracksaapi.repository.UsuarioRepository;
import com.logitracksa.logitracksaapi.service.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BodegaServiceImpl implements BodegaService {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final InventarioRepository inventarioRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        if (dto.capacidad() <= 0) {
            throw new BusinessRuleException("La capacidad de la bodega debe ser mayor a cero");
        }
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Bodega bodega = bodegaMapper.dtoToEntity(dto, usuario);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        return bodegaMapper.entityToDto(bodegaRepository.save(bodega), usuarioDto);
    }

    @Override
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega con id: " + id));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        bodegaMapper.updateEntityFromDto(dto, bodega, usuario);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        return bodegaMapper.entityToDto(bodegaRepository.save(bodega), usuarioDto);
    }

    @Override
    public void eliminar(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega con id: " + id));
        if (inventarioRepository.existsByBodegaId(bodega.getId())) {
            throw new BusinessRuleException("No se puede eliminar la bodega porque tiene inventario registrado");
        }
        if (movimientoRepository.existsByBodegaOrigenId(bodega.getId()) ||
                movimientoRepository.existsByBodegaDestinoId(bodega.getId())) {
            throw new BusinessRuleException("No se puede eliminar la bodega porque está asociada a movimientos");
        }
        bodegaRepository.delete(bodega);
    }

    @Override
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream()
                .map(b -> bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario())))
                .toList();
    }

    @Override
    public BodegaDtoResponse buscarId(Long id) {
        Bodega b = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega con id: " + id));
        return bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario()));
    }

    @Override
    public List<BodegaDtoResponse> buscarNombre(String nombre) {
        return bodegaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(b -> bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario())))
                .toList();
    }

    @Override
    public List<BodegaDtoResponse> buscarUbicacion(String ubicacion) {
        return bodegaRepository.findByUbicacionContainingIgnoreCase(ubicacion).stream()
                .map(b -> bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario())))
                .toList();
    }

    @Override
    public List<BodegaDtoResponse> buscarEncargado(Long usuarioId) {
        return bodegaRepository.findByUsuarioId(usuarioId).stream()
                .map(b -> bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario())))
                .toList();
    }

    @Override
    public List<BodegaDtoResponse> buscarCapacidadMinima(int capacidad) {
        return bodegaRepository.findByCapacidadGreaterThanEqual(capacidad).stream()
                .map(b -> bodegaMapper.entityToDto(b, usuarioMapper.entityToDto(b.getUsuario())))
                .toList();
    }
}
