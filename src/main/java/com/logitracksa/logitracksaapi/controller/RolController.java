package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.RolDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;
import com.logitracksa.logitracksaapi.service.RolService;
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
@Tag(name = "Administrador de roles", description = "CRUD de roles del sistema")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    // http://localhost:8080/api/roles
    @Operation(summary = "Crea un nuevo rol", description = "Registra un nuevo rol en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RolDtoResponse> guardar(@Valid @RequestBody RolDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.guardar(dto));
    }

    // http://localhost:8080/api/roles
    @Operation(summary = "Lista todos los roles", description = "Retorna todos los roles registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<RolDtoResponse>> listar() {
        return ResponseEntity.ok(rolService.listar());
    }

    // http://localhost:8080/api/roles/1
    @Operation(summary = "Busca un rol por ID", description = "Retorna el rol que coincida con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un rol con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.buscarId(id));
    }

    // http://localhost:8080/api/roles/filtroNombre?nombre=admin_maestro
    @Operation(summary = "Busca roles por nombre", description = "Retorna los roles que coincidan con el nombre indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles encontrados correctamente")
    })
    @GetMapping("/filtroNombre")
    public ResponseEntity<List<RolDtoResponse>> buscarNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(rolService.buscarNombre(nombre));
    }

    // http://localhost:8080/api/roles/1
    @Operation(summary = "Actualiza un rol por ID", description = "Actualiza los datos del rol con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un rol con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RolDtoResponse> actualizar(@Valid @RequestBody RolDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(rolService.actualizar(dto, id));
    }

    // http://localhost:8080/api/roles/1
    @Operation(summary = "Elimina un rol por ID", description = "Elimina el rol con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un rol con ese ID o tiene credenciales asociadas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
