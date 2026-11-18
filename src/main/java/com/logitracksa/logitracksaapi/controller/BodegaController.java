package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.BodegaDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.BodegaDtoResponse;
import com.logitracksa.logitracksaapi.service.BodegaService;
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

import java.util.List;

@Validated
@Tag(name = "Administrador de bodegas", description = "CRUD de bodegas del sistema")
@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    // http://localhost:8080/api/bodegas
    @Operation(summary = "Crea una nueva bodega", description = "Registra una nueva bodega en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<BodegaDtoResponse> guardar(@Valid @RequestBody BodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.guardar(dto));
    }

    // http://localhost:8080/api/bodegas
    @Operation(summary = "Lista todas las bodegas", description = "Retorna todas las bodegas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<BodegaDtoResponse>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    // http://localhost:8080/api/bodegas/1
    @Operation(summary = "Busca una bodega por ID", description = "Retorna la bodega que coincida con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe una bodega con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarId(id));
    }

    // http://localhost:8080/api/bodegas/filtroNombre?nombre=Norte
    @Operation(summary = "Busca bodegas por nombre", description = "Retorna las bodegas cuyo nombre contenga el texto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodegas encontradas correctamente")
    })
    @GetMapping("/filtroNombre")
    public ResponseEntity<List<BodegaDtoResponse>> buscarNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(bodegaService.buscarNombre(nombre));
    }

    // http://localhost:8080/api/bodegas/filtroUbicacion?ubicacion=Bucaramanga
    @Operation(summary = "Busca bodegas por ubicación", description = "Retorna las bodegas cuya ubicación contenga el texto indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodegas encontradas correctamente")
    })
    @GetMapping("/filtroUbicacion")
    public ResponseEntity<List<BodegaDtoResponse>> buscarUbicacion(@RequestParam String ubicacion) {
        return ResponseEntity.ok(bodegaService.buscarUbicacion(ubicacion));
    }

    // http://localhost:8080/api/bodegas/encargado/2
    @Operation(summary = "Busca bodegas por encargado", description = "Retorna las bodegas asignadas al usuario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodegas encontradas correctamente")
    })
    @GetMapping("/encargado/{usuarioId}")
    public ResponseEntity<List<BodegaDtoResponse>> buscarEncargado(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(bodegaService.buscarEncargado(usuarioId));
    }

    // http://localhost:8080/api/bodegas/filtroCapacidad?capacidad=1000
    @Operation(summary = "Busca bodegas por capacidad mínima", description = "Retorna las bodegas con capacidad mayor o igual al valor indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodegas encontradas correctamente")
    })
    @GetMapping("/filtroCapacidad")
    public ResponseEntity<List<BodegaDtoResponse>> buscarCapacidad(@RequestParam int capacidad) {
        return ResponseEntity.ok(bodegaService.buscarCapacidadMinima(capacidad));
    }

    // http://localhost:8080/api/bodegas/1
    @Operation(summary = "Actualiza una bodega por ID", description = "Actualiza los datos de la bodega con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe una bodega con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> actualizar(@Valid @RequestBody BodegaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.actualizar(dto, id));
    }

    // http://localhost:8080/api/bodegas/1
    @Operation(summary = "Elimina una bodega por ID", description = "Elimina la bodega con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe la bodega o tiene registros asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
