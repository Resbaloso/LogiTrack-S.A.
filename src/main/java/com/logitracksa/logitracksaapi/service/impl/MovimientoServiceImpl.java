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
import com.logitracksa.logitracksaapi.model.Auditoria;
import com.logitracksa.logitracksaapi.model.Bodega;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.AuditoriaRepository;
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
    private final AuditoriaRepository auditoriaRepository;

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
        Movimiento guardado = movimientoRepository.save(movimiento);

        // ── Auditoría automática INSERT ──────────────────────────────────────
        registrarAuditoria("INSERT", usuario, null, buildJsonMovimiento(guardado));

        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        BodegaDtoResponse bodegaOrigenDto = bodegaMapper.entityToDto(bodegaOrigen, usuarioMapper.entityToDto(bodegaOrigen.getUsuario()));
        BodegaDtoResponse bodegaDestinoDto = bodegaMapper.entityToDto(bodegaDestino, usuarioMapper.entityToDto(bodegaDestino.getUsuario()));
        return movimientoMapper.entityToDto(guardado, usuarioDto, bodegaOrigenDto, bodegaDestinoDto);
    }

    @Override
    public MovimientoDtoResponse actualizar(MovimientoDtoRequest dto, Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + id));

        // Captura el estado ANTERIOR antes de modificar
        String valoresAnteriores = buildJsonMovimiento(movimiento);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Bodega bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega origen con id: " + dto.bodegaOrigenId()));
        Bodega bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe la bodega destino con id: " + dto.bodegaDestinoId()));

        movimientoMapper.updateEntityFromDto(dto, movimiento, usuario, bodegaOrigen, bodegaDestino);
        Movimiento actualizado = movimientoRepository.save(movimiento);

        // ── Auditoría automática UPDATE ──────────────────────────────────────
        registrarAuditoria("UPDATE", usuario, valoresAnteriores, buildJsonMovimiento(actualizado));

        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        BodegaDtoResponse bodegaOrigenDto = bodegaMapper.entityToDto(bodegaOrigen, usuarioMapper.entityToDto(bodegaOrigen.getUsuario()));
        BodegaDtoResponse bodegaDestinoDto = bodegaMapper.entityToDto(bodegaDestino, usuarioMapper.entityToDto(bodegaDestino.getUsuario()));
        return movimientoMapper.entityToDto(actualizado, usuarioDto, bodegaOrigenDto, bodegaDestinoDto);
    }

    @Override
    public void eliminar(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + id));
        if (detalleMovimientoRepository.existsByMovimientoId(movimiento.getId())) {
            throw new BusinessRuleException("No se puede eliminar el movimiento porque tiene detalles asociados");
        }

        // ── Auditoría automática DELETE ──────────────────────────────────────
        registrarAuditoria("DELETE", movimiento.getUsuario(), buildJsonMovimiento(movimiento), null);

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

    // ─────────────────────────────────────────────────────────────────────────
    // Métodos auxiliares de auditoría
    // ─────────────────────────────────────────────────────────────────────────

    /** Serializa los campos relevantes de un Movimiento a JSON. */
    private String buildJsonMovimiento(Movimiento m) {
        return "{" +
            "\"id\":"              + m.getId()                                                        + "," +
            "\"fecha\":\""         + (m.getFecha() != null ? m.getFecha().toString() : "")           + "\"," +
            "\"tipoMovimiento\":\"" + m.getTipoMovimiento()                                           + "\"," +
            "\"usuarioId\":"       + (m.getUsuario() != null ? m.getUsuario().getId() : "null")       + "," +
            "\"bodegaOrigenId\":"  + (m.getBodegaOrigen() != null ? m.getBodegaOrigen().getId() : "null")   + "," +
            "\"bodegaDestinoId\":" + (m.getBodegaDestino() != null ? m.getBodegaDestino().getId() : "null") +
        "}";
    }

    /** Persiste un registro de auditoría directamente en el repositorio. */
    private void registrarAuditoria(String tipoOperacion, Usuario usuario,
                                    String valoresAnteriores, String valoresNuevos) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setFecha(LocalDate.now());
        auditoria.setUsuario(usuario);
        auditoria.setEntidadAfectada("Movimiento");
        auditoria.setValoresAnteriores(valoresAnteriores);
        auditoria.setValoresNuevos(valoresNuevos);
        auditoriaRepository.save(auditoria);
    }
}
