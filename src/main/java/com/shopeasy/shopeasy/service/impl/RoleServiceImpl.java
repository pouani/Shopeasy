package com.shopeasy.shopeasy.service.impl;

import com.shopeasy.shopeasy.dto.request.RoleRequest;
import com.shopeasy.shopeasy.dto.response.RoleResponse;
import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.shopeasy.mapper.RoleMapper;
import com.shopeasy.shopeasy.model.Role;
import com.shopeasy.shopeasy.repository.RoleRepository;
import com.shopeasy.shopeasy.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private  final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public RoleResponse create(RoleRequest dto) {

        if(roleRepository.existsByCode(dto.getCode())) {
            throw new BadRequestException("Le role existe déjà");
        }

        Role role = roleMapper.toEntity(dto);
        roleRepository.save(role);

        log.info("Role created with code: {}", dto.getCode());
        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponse update(Long id, RoleRequest dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role non trouvés"));

        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setCode(dto.getCode());

        roleRepository.save(role);
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponse> findAll() {

        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role non trouvés"));
        return roleMapper.toResponse(role);
    }

    @Override
    public void delete(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role non trouvés");
        }

        roleRepository.deleteById(id);
        log.warn("Role supprimé id={}", id);
    }
}
