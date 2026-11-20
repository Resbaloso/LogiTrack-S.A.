package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.DetalleMovimientoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.DetalleMovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.MovimientoDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.BodegaMapper;
import com.logitracksa.logitracksaapi.mapper.DetalleMovimientoMapper;
import com.logitracksa.logitracksaapi.mapper.MovimientoMapper;
import com.logitracksa.logitracksaapi.mapper.ProductoMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Auditoria;
import com.logitracksa.logitracksaapi.model.DetalleMovimiento;
import com.logitracksa.logitracksaapi.model.Inventario;
import com.logitracksa.logitracksaapi.model.Movimiento;
import com.logitracksa.logitracksaapi.model.Producto;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.AuditoriaRepository;
import com.logitracksa.logitracksaapi.repository.DetalleMovimientoRepository;
import com.logitracksa.logitracksaapi.repository.InventarioRepository;
import com.logitracksa.logitracksaapi.repository.MovimientoRepository;
import com.logitracksa.logitracksaapi.repository.ProductoRepository;
import com.logitracksa.logitracksaapi.service.DetalleMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleMovimientoServiceImpl implements DetalleMovimientoService {

    private final DetalleMovimientoMapper detalleMovimientoMapper;
    private final DetalleMovimientoRepository detalleMovimientoRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final BodegaMapper bodegaMapper;
    private final UsuarioMapper usuarioMapper;
    private final AuditoriaRepository auditoriaRepository;
    private final InventarioRepository inventarioRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // GUARDAR
    // Ajusta el stock según el tipo del movimiento asociado:
    //   ENTRADA       → +cantidad en la bodega DESTINO
    //   SALIDA        → -cantidad en la bodega ORIGEN  (valida stock suficiente)
    //   TRANSFERENCIA → -cantidad en ORIGEN, +cantidad en DESTINO
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public DetalleMovimientoDtoResponse guardar(DetalleMovimientoDtoRequest dto) {
        if (dto.cantidad() <= 0) {
            throw new BusinessRuleException("La cantidad debe ser mayor a cero");
        }
        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + dto.movimientoId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));

        // Captura el estado del inventario ANTES de aplicar el ajuste
        String stockAntes = buildJsonStockActual(movimiento, producto);

        aplicarAjusteStock(movimiento, producto, dto.cantidad());

        // Captura el estado del inventario DESPUÉS del ajuste
        String stockDespues = buildJsonStockActual(movimiento, producto);

        DetalleMovimiento detalle = detalleMovimientoMapper.dtoToEntity(dto, movimiento, producto);
        DetalleMovimiento guardado = detalleMovimientoRepository.save(detalle);

        registrarAuditoria("INSERT", movimiento.getUsuario(), stockAntes, stockDespues);

        return detalleMovimientoMapper.entityToDto(guardado,
                buildMovimientoDto(movimiento),
                productoMapper.entityToDto(producto));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ACTUALIZAR
    // Revierte el efecto anterior en stock y aplica el nuevo.
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public DetalleMovimientoDtoResponse actualizar(DetalleMovimientoDtoRequest dto, Long id) {
        DetalleMovimiento detalle = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));

        Movimiento movimientoAnterior = detalle.getMovimiento();
        Producto productoAnterior     = detalle.getProducto();

        // Estado del inventario antes de cualquier cambio
        String stockAntes = buildJsonStockActual(movimientoAnterior, productoAnterior);

        // Revertir el efecto anterior
        revertirStock(detalle);

        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el movimiento con id: " + dto.movimientoId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el producto con id: " + dto.productoId()));

        // Aplicar el nuevo efecto
        aplicarAjusteStock(movimiento, producto, dto.cantidad());

        // Estado del inventario después del nuevo ajuste
        String stockDespues = buildJsonStockActual(movimiento, producto);

        detalleMovimientoMapper.updateEntityFromDto(dto, detalle, movimiento, producto);
        DetalleMovimiento actualizado = detalleMovimientoRepository.save(detalle);

        registrarAuditoria("UPDATE", movimiento.getUsuario(), stockAntes, stockDespues);

        return detalleMovimientoMapper.entityToDto(actualizado,
                buildMovimientoDto(movimiento),
                productoMapper.entityToDto(producto));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ELIMINAR
    // Revierte el efecto en stock antes de borrar.
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public void eliminar(Long id) {
        DetalleMovimiento detalle = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));

        Movimiento movimiento = detalle.getMovimiento();
        Producto   producto   = detalle.getProducto();

        // Estado antes de revertir
        String stockAntes = buildJsonStockActual(movimiento, producto);

        revertirStock(detalle);

        // Estado después de revertir
        String stockDespues = buildJsonStockActual(movimiento, producto);

        registrarAuditoria("DELETE", movimiento.getUsuario(), stockAntes, stockDespues);

        detalleMovimientoRepository.delete(detalle);
    }

    @Override
    public List<DetalleMovimientoDtoResponse> listar() {
        return detalleMovimientoRepository.findAll().stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public DetalleMovimientoDtoResponse buscarId(Long id) {
        DetalleMovimiento d = detalleMovimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el detalle de movimiento con id: " + id));
        return detalleMovimientoMapper.entityToDto(d,
                buildMovimientoDto(d.getMovimiento()),
                productoMapper.entityToDto(d.getProducto()));
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarMovimiento(Long movimientoId) {
        return detalleMovimientoRepository.findByMovimientoId(movimientoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarProducto(Long productoId) {
        return detalleMovimientoRepository.findByProductoId(productoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarMovimientoYProducto(Long movimientoId, Long productoId) {
        return detalleMovimientoRepository.findByMovimientoIdAndProductoId(movimientoId, productoId).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    @Override
    public List<DetalleMovimientoDtoResponse> buscarCantidadMinima(int cantidad) {
        return detalleMovimientoRepository.findByCantidadGreaterThanEqual(cantidad).stream()
                .map(d -> detalleMovimientoMapper.entityToDto(d,
                        buildMovimientoDto(d.getMovimiento()),
                        productoMapper.entityToDto(d.getProducto())))
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LÓGICA DE STOCK
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Aplica el efecto del movimiento sobre el inventario:
     * ENTRADA       → suma en bodega destino
     * SALIDA        → resta en bodega origen (con validación)
     * TRANSFERENCIA → resta en origen y suma en destino
     */
    private void aplicarAjusteStock(Movimiento movimiento, Producto producto, int cantidad) {
        String tipo = movimiento.getTipoMovimiento().toUpperCase();
        switch (tipo) {
            case "ENTRADA" -> {
                Inventario inv = buscarInventario(movimiento.getBodegaDestino().getId(), producto.getId(),
                        "No hay inventario para ese producto en la bodega destino. Créelo primero en Inventario.");
                inv.setStock(inv.getStock() + cantidad);
                inventarioRepository.save(inv);
            }
            case "SALIDA" -> {
                Inventario inv = buscarInventario(movimiento.getBodegaOrigen().getId(), producto.getId(),
                        "No hay inventario para ese producto en la bodega origen.");
                validarStock(inv, cantidad);
                inv.setStock(inv.getStock() - cantidad);
                inventarioRepository.save(inv);
            }
            case "TRANSFERENCIA" -> {
                Inventario invOrigen = buscarInventario(movimiento.getBodegaOrigen().getId(), producto.getId(),
                        "No hay inventario para ese producto en la bodega origen.");
                validarStock(invOrigen, cantidad);
                invOrigen.setStock(invOrigen.getStock() - cantidad);
                inventarioRepository.save(invOrigen);

                Inventario invDestino = buscarInventario(movimiento.getBodegaDestino().getId(), producto.getId(),
                        "No hay inventario para ese producto en la bodega destino. Créelo primero en Inventario.");
                invDestino.setStock(invDestino.getStock() + cantidad);
                inventarioRepository.save(invDestino);
            }
            default -> throw new BusinessRuleException("Tipo de movimiento desconocido: " + tipo);
        }
    }

    /**
     * Revierte el efecto ya aplicado (operación inversa de aplicarAjusteStock).
     * Usado al actualizar o eliminar un detalle.
     */
    private void revertirStock(DetalleMovimiento detalle) {
        Movimiento movimiento = detalle.getMovimiento();
        Producto producto = detalle.getProducto();
        int cantidad = detalle.getCantidad();
        String tipo = movimiento.getTipoMovimiento().toUpperCase();

        switch (tipo) {
            case "ENTRADA" -> {
                List<Inventario> lista = inventarioRepository.findByBodegaIdAndProductoId(
                        movimiento.getBodegaDestino().getId(), producto.getId());
                if (!lista.isEmpty()) {
                    Inventario inv = lista.get(0);
                    inv.setStock(inv.getStock() - cantidad);
                    inventarioRepository.save(inv);
                }
            }
            case "SALIDA" -> {
                List<Inventario> lista = inventarioRepository.findByBodegaIdAndProductoId(
                        movimiento.getBodegaOrigen().getId(), producto.getId());
                if (!lista.isEmpty()) {
                    Inventario inv = lista.get(0);
                    inv.setStock(inv.getStock() + cantidad);
                    inventarioRepository.save(inv);
                }
            }
            case "TRANSFERENCIA" -> {
                List<Inventario> listaOrigen = inventarioRepository.findByBodegaIdAndProductoId(
                        movimiento.getBodegaOrigen().getId(), producto.getId());
                if (!listaOrigen.isEmpty()) {
                    Inventario inv = listaOrigen.get(0);
                    inv.setStock(inv.getStock() + cantidad);
                    inventarioRepository.save(inv);
                }
                List<Inventario> listaDestino = inventarioRepository.findByBodegaIdAndProductoId(
                        movimiento.getBodegaDestino().getId(), producto.getId());
                if (!listaDestino.isEmpty()) {
                    Inventario inv = listaDestino.get(0);
                    inv.setStock(inv.getStock() - cantidad);
                    inventarioRepository.save(inv);
                }
            }
        }
    }

    private Inventario buscarInventario(Long bodegaId, Long productoId, String mensajeError) {
        List<Inventario> resultados = inventarioRepository.findByBodegaIdAndProductoId(bodegaId, productoId);
        if (resultados.isEmpty()) {
            throw new BusinessRuleException(mensajeError);
        }
        return resultados.get(0);
    }

    private void validarStock(Inventario inv, int cantidad) {
        if (inv.getStock() < cantidad) {
            throw new BusinessRuleException(
                    "Stock insuficiente en la bodega origen. Disponible: "
                    + inv.getStock() + ", solicitado: " + cantidad);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS INTERNOS
    // ─────────────────────────────────────────────────────────────────────────

    private MovimientoDtoResponse buildMovimientoDto(Movimiento m) {
        return movimientoMapper.entityToDto(m,
                usuarioMapper.entityToDto(m.getUsuario()),
                bodegaMapper.entityToDto(m.getBodegaOrigen(), usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuario())),
                bodegaMapper.entityToDto(m.getBodegaDestino(), usuarioMapper.entityToDto(m.getBodegaDestino().getUsuario())));
    }

    /**
     * Captura el stock actual de las bodegas afectadas por el movimiento.
     * Devuelve un JSON con el estado del inventario en el momento de la llamada.
     * Ejemplo:
     * { "producto": "laptop asus", "productoId": 1,
     *   "bodegaOrigen": { "id":1, "nombre":"Bodega BGA", "stockActual": 200 },
     *   "bodegaDestino": { "id":2, "nombre":"Bodega CUC", "stockActual": 50 } }
     */
    private String buildJsonStockActual(Movimiento movimiento, Producto producto) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"productoId\":").append(producto.getId()).append(",");
        sb.append("\"productoNombre\":\"").append(producto.getNombre()).append("\",");
        sb.append("\"tipoMovimiento\":\"").append(movimiento.getTipoMovimiento()).append("\"");

        List<Inventario> listaOrigen = inventarioRepository.findByBodegaIdAndProductoId(
                movimiento.getBodegaOrigen().getId(), producto.getId());
        if (!listaOrigen.isEmpty()) {
            Inventario inv = listaOrigen.get(0);
            sb.append(",\"bodegaOrigen\":{");
            sb.append("\"bodegaId\":").append(movimiento.getBodegaOrigen().getId()).append(",");
            sb.append("\"bodegaNombre\":\"").append(movimiento.getBodegaOrigen().getNombre()).append("\",");
            sb.append("\"stock\":").append(inv.getStock());
            sb.append("}");
        }

        List<Inventario> listaDestino = inventarioRepository.findByBodegaIdAndProductoId(
                movimiento.getBodegaDestino().getId(), producto.getId());
        if (!listaDestino.isEmpty()) {
            Inventario inv = listaDestino.get(0);
            sb.append(",\"bodegaDestino\":{");
            sb.append("\"bodegaId\":").append(movimiento.getBodegaDestino().getId()).append(",");
            sb.append("\"bodegaNombre\":\"").append(movimiento.getBodegaDestino().getNombre()).append("\",");
            sb.append("\"stock\":").append(inv.getStock());
            sb.append("}");
        }

        sb.append("}");
        return sb.toString();
    }

    private String buildJsonDetalle(DetalleMovimiento d) {
        return "{" +
            "\"id\":"              + d.getId()                                                   + "," +
            "\"movimientoId\":"    + (d.getMovimiento() != null ? d.getMovimiento().getId() : "null") + "," +
            "\"tipoMovimiento\":\"" + (d.getMovimiento() != null ? d.getMovimiento().getTipoMovimiento() : "") + "\"," +
            "\"productoId\":"      + (d.getProducto() != null ? d.getProducto().getId() : "null") + "," +
            "\"productoNombre\":\"" + (d.getProducto() != null ? d.getProducto().getNombre() : "") + "\"," +
            "\"cantidad\":"        + d.getCantidad() +
        "}";
    }

    private void registrarAuditoria(String tipoOperacion, Usuario usuario,
                                    String valoresAnteriores, String valoresNuevos) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setFecha(LocalDate.now());
        auditoria.setUsuario(usuario);
        auditoria.setEntidadAfectada("DetalleMovimiento");
        auditoria.setValoresAnteriores(valoresAnteriores);
        auditoria.setValoresNuevos(valoresNuevos);
        auditoriaRepository.save(auditoria);
    }
}
