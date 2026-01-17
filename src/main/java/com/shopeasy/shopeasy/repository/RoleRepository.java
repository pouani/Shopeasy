package com.shopeasy.shopeasy.repository;

import com.shopeasy.shopeasy.model.Role;
import com.shopeasy.shopeasy.util.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByCode(RoleCode code);

    Optional<Role> findByCode(RoleCode code);
}
