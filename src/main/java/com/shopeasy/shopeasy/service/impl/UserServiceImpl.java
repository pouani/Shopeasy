package com.shopeasy.shopeasy.service.impl;

import com.shopeasy.shopeasy.dto.request.CreateUserRequest;
import com.shopeasy.shopeasy.dto.response.UserResponse;
import com.shopeasy.shopeasy.exception.BadRequestException;
import com.shopeasy.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.shopeasy.mapper.UserMapper;
import com.shopeasy.shopeasy.model.Role;
import com.shopeasy.shopeasy.model.User;
import com.shopeasy.shopeasy.repository.RoleRepository;
import com.shopeasy.shopeasy.repository.UserRepository;
import com.shopeasy.shopeasy.service.UserService;
import com.shopeasy.shopeasy.util.RoleCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest dto) {

        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("L'email existe déjà");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        RoleCode roleCode = dto.getRoleCodeEnum();
        Role role = roleRepository.findByCode(roleCode)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Rôle " + roleCode + " introuvable. Assurez-vous que les rôles sont initialisés."
                        ));

        user.setRole(role);
        userRepository.save(user);

        log.info("User created: {} with role {}", dto.getEmail(), roleCode);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse update(String publicId, CreateUserRequest dto) {

        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvés"));

        user.setName(dto.getName());

        // Mettre à jour le rôle si fourni
        if (dto.getRoleCode() != null && !dto.getRoleCode().isEmpty()) {
            RoleCode roleCode = dto.getRoleCodeEnum();
            Role role = roleRepository.findByCode(roleCode)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Rôle " + roleCode + " introuvable"
                    ));
            user.setRole(role);
        }

        userRepository.save(user);
        log.info("User updated: {}", user.getEmail());

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(()-> new ResourceNotFoundException("Utilisateur non trouvés"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        log.debug("User loaded: {} with role {}", email, user.getRole().getCode());

        // Spring Security utilisera directement l'objet User car il implémente UserDetails
        return user;
    }

    @Override
    @Transactional
    public void delete(String publicId) {
        if(userRepository.existsByPublicId(publicId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvés");
        }

        userRepository.deleteByPublicId(publicId);

        log.info("User deleted: {}", publicId);
    }

    @Transactional
    public void disable(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        user.setEnabled(false);
        userRepository.save(user);

        log.info("User disabled: {}", user.getEmail());
    }

    @Transactional
    public void enable(String publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        user.setEnabled(true);
        userRepository.save(user);

        log.info("User enabled: {}", user.getEmail());
    }
}
