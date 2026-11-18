package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.UsuarioDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.BodegaRepository;
import com.logitracksa.logitracksaapi.repository.CredencialesRepository;
import com.logitracksa.logitracksaapi.repository.UsuarioRepository;
import com.logitracksa.logitracksaapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final CredencialesRepository credencialesRepository;
    private final BodegaRepository bodegaRepository;

    @Override                                                                                                           
    public UsuarioDtoResponse guardar(UsuarioDtoRequest dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("Ya existe un usuario con el email: " + dto.email());
        }
        Usuario usuario = usuarioMapper.dtoToEntity(dto);
        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + id));
        usuarioMapper.updateEntityFromDto(dto, usuario);
        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + id));
        if (credencialesRepository.existsByUsuarioId(usuario.getId())) {
            throw new BusinessRuleException("No se puede eliminar el usuario porque tiene credenciales asociadas");
        }
        if (bodegaRepository.existsByUsuarioId(usuario.getId())) {
            throw new BusinessRuleException("No se puede eliminar el usuario porque está asignado como encargado de una bodega");
        }
        usuarioRepository.delete(usuario);
    }

    @Override
    public List<UsuarioDtoResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }

    @Override
    public UsuarioDtoResponse buscarId(Long id) {
        return usuarioMapper.entityToDto(usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + id)));
    }

    @Override
    public List<UsuarioDtoResponse> buscarEmail(String email) {
        return usuarioRepository.findByEmail(email).stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }

    @Override
    public List<UsuarioDtoResponse> buscarActivos(boolean activo) {
        return usuarioRepository.findByActivo(activo).stream()
                .map(usuarioMapper::entityToDto)
                .toList();
    }
}
