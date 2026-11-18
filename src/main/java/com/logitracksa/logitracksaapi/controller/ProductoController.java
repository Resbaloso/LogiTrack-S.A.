package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.ProductoDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.ProductoDtoResponse;
import com.logitracksa.logitracksaapi.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Validated
@Tag(name = "Administrador de productos", description = "CRUD de productos del sistema")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // http://localhost:8080/api/productos
    @Operation(summary = "Crea un nuevo producto", description = "Registra un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<ProductoDtoResponse> guardar(@Valid @RequestBody ProductoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(dto));
    }

    // http://localhost:8080/api/productos
    @Operation(summary = "Lista todos los productos", description = "Retorna todos los productos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoDtoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // http://localhost:8080/api/productos/1
    @Operation(summary = "Busca un producto por ID", description = "Retorna el producto que coincida con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un producto con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarId(id));
    }

    // http://localhost:8080/api/productos/filtroCategoria?categoria=Materiales
    @Operation(summary = "Busca productos por categoría", description = "Retorna los productos que pertenezcan a la categoría indicada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/filtroCategoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(productoService.buscarCategoria(categoria));
    }

    // http://localhost:8080/api/productos/filtroNombre?nombre=Pallet
    @Operation(summary = "Busca productos por nombre", description = "Retorna los productos cuyo nombre contenga el texto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/filtroNombre")
    public ResponseEntity<List<ProductoDtoResponse>> buscarNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarNombre(nombre));
    }

    // http://localhost:8080/api/productos/filtroPrecioMaximo?precio=50000
    @Operation(summary = "Busca productos por precio máximo", description = "Retorna los productos con precio menor o igual al indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/filtroPrecioMaximo")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPrecioMaximo(@RequestParam BigDecimal precio) {
        return ResponseEntity.ok(productoService.buscarPrecioMaximo(precio));
    }

    // http://localhost:8080/api/productos/filtroPrecioMinimo?precio=10000
    @Operation(summary = "Busca productos por precio mínimo", description = "Retorna los productos con precio mayor o igual al indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/filtroPrecioMinimo")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPrecioMinimo(@RequestParam BigDecimal precio) {
        return ResponseEntity.ok(productoService.buscarPrecioMinimo(precio));
    }

    // http://localhost:8080/api/productos/1
    @Operation(summary = "Actualiza un producto por ID", description = "Actualiza los datos del producto con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un producto con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> actualizar(@Valid @RequestBody ProductoDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizar(dto, id));
    }

    // http://localhost:8080/api/productos/1
    @Operation(summary = "Elimina un producto por ID", description = "Elimina el producto con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe el producto o tiene registros asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
