package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.MovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.BodegaMapper;
import com.logitracksa.logitracksaapi.mapper.MovimientoMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.BodegaRepository;
import com.logitracksa.logitracksaapi.repository.DetalleMovimientoRepository;
import com.logitracksa.logitracksaapi.repository.MovimientoRepository;
import com.logitracksa.logitracksaapi.repository.UsuarioRepository;
import com.logitracksa.logitracksaapi.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoMapper movimientoMapper;
    private final MovimientoRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final DetalleMovimientoRepository detalleMovimientoRepository;

    @Override
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto) {
        if (!dto.tipoMovimiento().equals("ENTRADA") &&
                !dto.tipoMovimiento().equals("SALIDA") &&
                !dto.tipoMovimiento().equals("TRANSFERENCIA")) {
            throw new BusinessRuleException("El tipo de movimiento debe ser ENTRADA, SALIDA o TRANSFERENCIA");
        }
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Bodega bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega origen con id: " + dto.bodegaOrigenId()));
        Bodega bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega destino con id: " + dto.bodegaDestinoId()));
        Movimiento movimiento = movimientoMapper.dtoToEntity(dto, usuario, bodegaOrigen, bodegaDestino);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        BodegaDtoResponse bodegaOrigenDto = bodegaMapper.entityToDto(bodegaOrigen, usuarioMapper.entityToDto(bodegaOrigen.getUsuario()));
        BodegaDtoResponse bodegaDestinoDto = bodegaMapper.entityToDto(bodegaDestino, usuarioMapper.entityToDto(bodegaDestino.getUsuario()));
        return movimientoMapper.entityToDto(movimientoRepository.save(movimiento), usuarioDto, bodegaOrigenDto, bodegaDestinoDto);
    }

    @Override
    public MovimientoDtoResponse actualizar(MovimientoDtoRequest dto, Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + id));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Bodega bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega origen con id: " + dto.bodegaOrigenId()));
        Bodega bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega destino con id: " + dto.bodegaDestinoId()));
        movimientoMapper.updateEntityFromDto(dto, movimiento, usuario, bodegaOrigen, bodegaDestino);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        BodegaDtoResponse bodegaOrigenDto = bodegaMapper.entityToDto(bodegaOrigen, usuarioMapper.entityToDto(bodegaOrigen.getUsuario()));
        BodegaDtoResponse bodegaDestinoDto = bodegaMapper.entityToDto(bodegaDestino, usuarioMapper.entityToDto(bodegaDestino.getUsuario()));
        return movimientoMapper.entityToDto(movimientoRepository.save(movimiento), usuarioDto, bodegaOrigenDto, bodegaDestinoDto);
    }

    @Override
    public void eliminar(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + id));
        if (detalleMovimientoRepository.existsByMovimientoId(movimiento.getId())) {
            throw new BusinessRuleException("No se puede eliminar el movimiento porque tiene detalles asociados");
        }
        movimientoRepository.delete(movimiento);
    }

    @Override
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll().stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    public MovimientoDtoResponse buscarId(Long id) {
        Movimiento m = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + id));
        return movimientoMapper.entityToDto(m,
                usuarioMapper.entityToDto(m.getUsuario()),
                bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario())));
    }

    @Override
    public List<MovimientoDtoResponse> buscarUsuario(Long usuarioId) {
        return movimientoRepository.findByUsuarioId(usuarioId).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarTipo(String tipoMovimiento) {
        return movimientoRepository.findByTipoMovimiento(tipoMovimiento).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    // Movimientos entre rangos de fechas
    public List<MovimientoDtoResponse> buscarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarBodegaOrigen(Long bodegaOrigenId) {
        return movimientoRepository.findByBodegaOrigenId(bodegaOrigenId).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarBodegaDestino(Long bodegaDestinoId) {
        return movimientoRepository.findByBodegaDestinoId(bodegaDestinoId).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarUsuarioYTipo(Long usuarioId, String tipoMovimiento) {
        return movimientoRepository.findByUsuarioIdAndTipoMovimiento(usuarioId, tipoMovimiento).stream()
                .map(m -> movimientoMapper.entityToDto(m,
                        usuarioMapper.entityToDto(m.getUsuario()),
                        bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                        bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario()))))
                .toList();
    }
}
