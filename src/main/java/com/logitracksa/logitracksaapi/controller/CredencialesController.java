package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.CredencialesDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.CredencialesDtoResponse;
import com.logitracksa.logitracksaapi.service.CredencialesService;
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
@Tag(name = "Administrador de credenciales", description = "CRUD de credenciales — solo accesible por el admin maestro")
@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
public class CredencialesController {

    private final CredencialesService credencialesService;

    // http://localhost:8080/api/credenciales
    @Operation(summary = "Crea credenciales para un usuario", description = "El admin maestro registra las credenciales de un encargado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credenciales creadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<CredencialesDtoResponse> guardar(@Valid @RequestBody CredencialesDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(credencialesService.guardar(dto));
    }

    // http://localhost:8080/api/credenciales
    @Operation(summary = "Lista todas las credenciales", description = "Retorna todas las credenciales registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<CredencialesDtoResponse>> listar() {
        return ResponseEntity.ok(credencialesService.listar());
    }

    // http://localhost:8080/api/credenciales/1
    @Operation(summary = "Busca credenciales por ID", description = "Retorna las credenciales que coincidan con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales encontradas correctamente"),
            @ApiResponse(responseCode = "400", description = "No existen credenciales con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CredencialesDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(credencialesService.buscarId(id));
    }

    // http://localhost:8080/api/credenciales/filtroUsername?username=juan.perez
    @Operation(summary = "Busca credenciales por username", description = "Retorna las credenciales que coincidan con el username indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales encontradas correctamente")
    })
    @GetMapping("/filtroUsername")
    public ResponseEntity<List<CredencialesDtoResponse>> buscarUsername(@RequestParam String username) {
        return ResponseEntity.ok(credencialesService.buscarUsername(username));
    }

    // http://localhost:8080/api/credenciales/usuario/2
    @Operation(summary = "Busca credenciales por ID de usuario", description = "Retorna las credenciales asociadas al usuario indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales encontradas correctamente")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CredencialesDtoResponse>> buscarUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(credencialesService.buscarUsuarioId(usuarioId));
    }

    // http://localhost:8080/api/credenciales/rol/2
    @Operation(summary = "Busca credenciales por ID de rol", description = "Retorna las credenciales asociadas al rol indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales encontradas correctamente")
    })
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<CredencialesDtoResponse>> buscarRolId(@PathVariable Long rolId) {
        return ResponseEntity.ok(credencialesService.buscarRolId(rolId));
    }

    // http://localhost:8080/api/credenciales/1
    @Operation(summary = "Actualiza credenciales por ID", description = "Actualiza las credenciales con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales actualizadas correctamente"),
            @ApiResponse(responseCode = "400", description = "No existen credenciales con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CredencialesDtoResponse> actualizar(@Valid @RequestBody CredencialesDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(credencialesService.actualizar(dto, id));
    }

    // http://localhost:8080/api/credenciales/1
    @Operation(summary = "Elimina credenciales por ID", description = "Elimina las credenciales con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Credenciales eliminadas correctamente"),
            @ApiResponse(responseCode = "400", description = "No existen credenciales con ese ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        credencialesService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
