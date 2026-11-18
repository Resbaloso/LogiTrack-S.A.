package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.CredencialesDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.CredencialesDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;
import com.logitracksa.logitracksaapi.dto.response.UsuarioDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.CredencialesMapper;
import com.logitracksa.logitracksaapi.mapper.RolMapper;
import com.logitracksa.logitracksaapi.mapper.UsuarioMapper;
import com.logitracksa.logitracksaapi.model.Credenciales;
import com.logitracksa.logitracksaapi.model.Rol;
import com.logitracksa.logitracksaapi.model.Usuario;
import com.logitracksa.logitracksaapi.repository.CredencialesRepository;
import com.logitracksa.logitracksaapi.repository.RolRepository;
import com.logitracksa.logitracksaapi.repository.UsuarioRepository;
import com.logitracksa.logitracksaapi.service.CredencialesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CredencialesServiceImpl implements CredencialesService {

    private final CredencialesMapper credencialesMapper;
    private final CredencialesRepository credencialesRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public CredencialesDtoResponse guardar(CredencialesDtoRequest dto) {
        if (credencialesRepository.existsByUsername(dto.username())) {
            throw new BusinessRuleException("Ya existe un usuario con el username: " + dto.username());
        }
        if (credencialesRepository.existsByUsuarioId(dto.usuarioId())) {
            throw new BusinessRuleException("El usuario ya tiene credenciales registradas");
        }
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el rol con id: " + dto.rolId()));
        Credenciales credenciales = credencialesMapper.dtoToEntity(dto, usuario, rol);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        RolDtoResponse rolDto = rolMapper.entityToDto(rol);
        return credencialesMapper.entityToDto(credencialesRepository.save(credenciales), usuarioDto, rolDto);
    }

    @Override
    public CredencialesDtoResponse actualizar(CredencialesDtoRequest dto, Long id) {
        Credenciales credenciales = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existen credenciales con id: " + id));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el usuario con id: " + dto.usuarioId()));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new DatabaseRuleException("No existe el rol con id: " + dto.rolId()));
        credencialesMapper.updateEntityFromDto(dto, credenciales, usuario, rol);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        RolDtoResponse rolDto = rolMapper.entityToDto(rol);
        return credencialesMapper.entityToDto(credencialesRepository.save(credenciales), usuarioDto, rolDto);
    }

    @Override
    public void eliminar(Long id) {
        Credenciales credenciales = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existen credenciales con id: " + id));
        credencialesRepository.delete(credenciales);
    }

    @Override
    public List<CredencialesDtoResponse> listar() {
        return credencialesRepository.findAll().stream()
                .map(c -> credencialesMapper.entityToDto(c,
                        usuarioMapper.entityToDto(c.getUsuario()),
                        rolMapper.entityToDto(c.getRol())))
                .toList();
    }

    @Override
    public CredencialesDtoResponse buscarId(Long id) {
        Credenciales c = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existen credenciales con id: " + id));
        return credencialesMapper.entityToDto(c,
                usuarioMapper.entityToDto(c.getUsuario()),
                rolMapper.entityToDto(c.getRol()));
    }

    @Override
    public List<CredencialesDtoResponse> buscarUsername(String username) {
        return credencialesRepository.findByUsername(username).stream()
                .map(c -> credencialesMapper.entityToDto(c,
                        usuarioMapper.entityToDto(c.getUsuario()),
                        rolMapper.entityToDto(c.getRol())))
                .toList();
    }

    @Override
    public List<CredencialesDtoResponse> buscarUsuarioId(Long usuarioId) {
        return credencialesRepository.findByUsuarioId(usuarioId).stream()
                .map(c -> credencialesMapper.entityToDto(c,
                        usuarioMapper.entityToDto(c.getUsuario()),
                        rolMapper.entityToDto(c.getRol())))
                .toList();
    }

    @Override
    public List<CredencialesDtoResponse> buscarRolId(Long rolId) {
        return credencialesRepository.findByRolId(rolId).stream()
                .map(c -> credencialesMapper.entityToDto(c,
                        usuarioMapper.entityToDto(c.getUsuario()),
                        rolMapper.entityToDto(c.getRol())))
                .toList();
    }
}
