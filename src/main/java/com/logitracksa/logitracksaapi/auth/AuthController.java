package com.logitracksa.logitracksaapi.auth;

import com.logitracksa.logitracksaapi.config.JwtService;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.model.Credenciales;
import com.logitracksa.logitracksaapi.repository.CredencialesRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoint de login para obtener el token JWT")
public class AuthController {

    private final JwtService jwtService;
    private final CredencialesRepository credencialesRepository;

    @Operation(
            summary = "Login",
            description = "Autentica al usuario (admin_maestro o encargado) y retorna un token JWT"
    )
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        List<Credenciales> resultados = credencialesRepository.findByUsername(request.username());

        if (resultados.isEmpty()) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        Credenciales credenciales = resultados.get(0);

        if (!credenciales.getPassword().equals(request.password())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        if (!credenciales.getUsuario().isActivo()) {
            throw new BusinessRuleException("El usuario se encuentra inactivo");
        }

        String rol = credenciales.getRol().getNombre();
        String token = jwtService.generateToken(credenciales.getUsername(), rol);
        return new LoginResponse(token);
    }
}
