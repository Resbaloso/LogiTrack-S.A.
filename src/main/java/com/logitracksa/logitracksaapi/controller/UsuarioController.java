package com.logitracksa.logitracksaapi.controller;

import com.logitracksa.logitracksaapi.dto.request.UsuarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.service.UsuarioService;
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
@Tag(name = "Administrador de usuarios", description = "CRUD de usuarios del sistema")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // http://localhost:8080/api/usuarios
    @Operation(summary = "Crea un nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<UsuarioDtoResponse> guardar(@Valid @RequestBody UsuarioDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(dto));
    }

    // http://localhost:8080/api/usuarios
    @Operation(summary = "Lista todos los usuarios", description = "Retorna todos los usuarios registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada correctamente")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDtoResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // http://localhost:8080/api/usuarios/1
    @Operation(summary = "Busca un usuario por ID", description = "Retorna el usuario que coincida con el ID proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un usuario con ese ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarId(id));
    }

    // http://localhost:8080/api/usuarios/filtroEmail?email=juan@empresa.com
    @Operation(summary = "Busca usuarios por email", description = "Retorna los usuarios que coincidan con el email indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente")
    })
    @GetMapping("/filtroEmail")
    public ResponseEntity<List<UsuarioDtoResponse>> buscarEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.buscarEmail(email));
    }

    // http://localhost:8080/api/usuarios/filtroActivo?activo=true
    @Operation(summary = "Busca usuarios por estado activo", description = "Retorna los usuarios activos o inactivos según el parámetro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente")
    })
    @GetMapping("/filtroActivo")
    public ResponseEntity<List<UsuarioDtoResponse>> buscarActivos(@RequestParam boolean activo) {
        return ResponseEntity.ok(usuarioService.buscarActivos(activo));
    }

    // http://localhost:8080/api/usuarios/1
    @Operation(summary = "Actualiza un usuario por ID", description = "Actualiza los datos del usuario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe un usuario con ese ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> actualizar(@Valid @RequestBody UsuarioDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.actualizar(dto, id));
    }

    // http://localhost:8080/api/usuarios/1
    @Operation(summary = "Elimina un usuario por ID", description = "Elimina el usuario con el ID indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No existe el usuario o tiene registros asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
