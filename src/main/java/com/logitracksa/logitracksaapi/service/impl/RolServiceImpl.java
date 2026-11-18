package com.logitracksa.logitracksaapi.service.impl;

import com.logitracksa.logitracksaapi.dto.request.RolDtoRequest;
import com.logitracksa.logitracksaapi.dto.response.RolDtoResponse;
import com.logitracksa.logitracksaapi.exception.BusinessRuleException;
import com.logitracksa.logitracksaapi.exception.DatabaseRuleException;
import com.logitracksa.logitracksaapi.mapper.RolMapper;
import com.logitracksa.logitracksaapi.model.Rol;
import com.logitracksa.logitracksaapi.repository.CredencialesRepository;
import com.logitracksa.logitracksaapi.repository.RolRepository;
import com.logitracksa.logitracksaapi.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RolServiceImpl implements RolService {

    private final RolMapper rolMapper;
    private final RolRepository rolRepository;
    private final CredencialesRepository credencialesRepository;

    @Override
    public RolDtoResponse guardar(RolDtoRequest dto) {
        if (rolRepository.existsByNombre(dto.nombre())) {
            throw new BusinessRuleException("Ya existe un rol con el nombre: " + dto.nombre());
        }
        Rol rol = rolMapper.dtoToEntity(dto);
        return rolMapper.entityToDto(rolRepository.save(rol));
    }

    @Override
    public RolDtoResponse actualizar(RolDtoRequest dto, Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el rol con id: " + id));
        rolMapper.updateEntityFromDto(dto, rol);
        return rolMapper.entityToDto(rolRepository.save(rol));
    }

    @Override
    public void eliminar(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el rol con id: " + id));
        if (!credencialesRepository.findByRolId(rol.getId()).isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar el rol porque tiene credenciales asociadas");
        }
        rolRepository.delete(rol);
    }

    @Override
    public List<RolDtoResponse> listar() {
        return rolRepository.findAll().stream()
                .map(rolMapper::entityToDto)
                .toList();
    }

    @Override
    public RolDtoResponse buscarId(Long id) {
        return rolMapper.entityToDto(rolRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("No existe el rol con id: " + id)));
    }

    @Override
    public List<RolDtoResponse> buscarNombre(String nombre) {
        return rolRepository.findByNombre(nombre).stream()
                .map(rolMapper::entityToDto)
                .toList();
    }
}
